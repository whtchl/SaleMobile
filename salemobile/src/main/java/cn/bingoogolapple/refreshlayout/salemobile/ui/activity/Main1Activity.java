package cn.bingoogolapple.refreshlayout.salemobile.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cm.hardwarereport.example.Constants;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import android.util.Log;
import android.content.Intent;

import com.login.example.LoginActivity;
import android.content.Context;
import cn.bingoogolapple.refreshlayout.salemobile.App;
import android.os.AsyncTask;
import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.update.example.*;

public class Main1Activity extends AppCompatActivity {
    static String TAG="Main1Activity";
    private Class<?> mLastStartedClass;
    private Context context;
    private String updateInfoResult;
    private VersionInfo versionInfo;

    App getApp() {
        return (App) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "tchl onResume");
        initAppUpdate();  //update apk
       // jumpToNext();
    }

    void jumpToNext() {

        if(mLastStartedClass != MainActivity.class) {
            mLastStartedClass = MainActivity.class;
            // We don't want to come back here, so remove from the
            // activity stack
            Log.i(TAG,"tchl mLastStartedClass != MainActivity.class !!");
            finish();
            Class<?> nextClass = MainActivity.class;
            if (getApp()!=null && !getApp().getLoginCompleted()) {
                Log.i(TAG,"tchl getApp().getLoginCompleted()="+getApp().getLoginCompleted());
                nextClass = LoginActivity.class;
            }
            Intent intent = new Intent(getApplicationContext(),
                    nextClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the update info of app from server.
     *
     * @return
     */
    private void getUpdateInfo() {
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        syncHttpClient.get(context, Constants.CHECK_VERSION_URL, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                Log.i(TAG,"updateInforesult:"+arg2);
                updateInfoResult = arg2;
            }

            @Override
            public void onFailure(int arg0, Header[] arg1,String arg2, Throwable arg3) {

            }
        });
    }

    /**
     * Parse the new version code from update info.
     *
     * @param
     * @return
     */
    private int getNewVersionCode() {
        Gson gson = new Gson();
        versionInfo = gson.fromJson(updateInfoResult, VersionInfo.class);
        return versionInfo.getVersionCode();
    }

    /**
     * Get the current version code of app.
     *
     * @return
     */
    private int getCurrentVersionCode() {
        int versionCode = 0;
        PackageManager packageManager = this.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            Log.i(TAG,"tchl current versionCode:"+versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * Check whether the network is available.
     *
     * @param context
     * @return
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    private void showUpdateDialog() {
        Log.i(TAG,"tchl showUpdateDialog");
        // 弹出版本更新提示框
        Dialog dialog = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle(getString(R.string.update_info)).setMessage(updateMessage()).setPositiveButton(getString(R.string.background_update), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // 下载应用
                new ApkDownLoad(getApplicationContext(), versionInfo.getURL(), getDownloadApkName(),getString(R.string.configure), getString(R.string.version_upgrade)).execute();
                jumpToNext();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                arg0.cancel();
                jumpToNext();
            }
        }).create();
        dialog.show();

    }

    private String getDownloadApkName() {
        StringBuilder sb = new StringBuilder();
        sb.append("Yunzi_v");
        sb.append(versionInfo.getVersion());
        sb.append(".apk");
        return sb.toString();
    }

    private String updateMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(versionInfo.getReleaseNote());
        return sb.toString();
    }

    private class CheckVersionTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            if (isNetworkAvailable(context)) {
                getUpdateInfo();
            }
            if (updateInfoResult == null) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                int currentVersionCode = getCurrentVersionCode();
                int newVersionCode = getNewVersionCode();
                Log.i(TAG, "tchl newVersionCode:" + newVersionCode);
                if (currentVersionCode < newVersionCode) {
                    showUpdateDialog();
                }else{
                    Log.i(TAG,"tchl call jumpToText");
                    jumpToNext();
                }
            }
            super.onPostExecute(result);
        }


        void jumpToNext() {

            if(mLastStartedClass != MainActivity.class) {
                mLastStartedClass = MainActivity.class;
                // We don't want to come back here, so remove from the
                // activity stack
                Log.i(TAG,"tchl mLastStartedClass != MainActivity.class !!");
                finish();
                Class<?> nextClass = MainActivity.class;
                if (getApp()!=null && !getApp().getLoginCompleted()) {
                    Log.i(TAG,"tchl getApp().getLoginCompleted()="+getApp().getLoginCompleted());
                    nextClass = LoginActivity.class;
                }
                Intent intent = new Intent(getApplicationContext(),
                        nextClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        }
    }

    private void initAppUpdate() {
        context = this;
        new CheckVersionTask().execute();
    }

}

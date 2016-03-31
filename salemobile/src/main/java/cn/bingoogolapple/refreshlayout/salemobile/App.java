package cn.bingoogolapple.refreshlayout.salemobile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.*;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import  com.parse.*;

import cm.hardwarereport.example.Constants;

import android.content.SharedPreferences;

import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import org.mobile.db.*;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/21 下午10:13
 * 描述:
 */
public class App extends Application {
    private static App sInstance;

    private SharedPreferences mPreferences;
    SharedPreferences.Editor editor;
    public DaoSession daoSession;
    public SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initImageLoader(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Constants.ApplicationId, Constants.ClientKey);

        mPreferences = getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
        DBLoader.init();
/*        if(!CheckNetwork.isConnected(this)) {
            Log.i("tchl","there is no network");
            //Toast.makeText(this,getApplicationContext().getText(R.string.no_network),Toast.LENGTH_LONG);
            ToastUtil.show(R.string.no_network);
        }else {

            //ParseObject.registerSubclass(HardwareModel.class);
            Log.i("tchl","there is network");

        }*/
    }

    public static App getInstance() {
        return sInstance;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getImgOptions())
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static DisplayImageOptions getImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .showImageOnLoading(R.mipmap.holder)
//                .showImageForEmptyUri(R.mipmap.holder)
//                .showImageOnFail(R.mipmap.holder)
                .build();
        return imgOptions;
    }


    public boolean getLoginCompleted() {
        SharedPreferences mySharedPreferences = getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if (mySharedPreferences.getString(Constants.NAME, null) == null || mySharedPreferences.getString(Constants.SHORTPHONE, null) == null) {
            return false;
        }
        return true;

    }

    public boolean getIsManager(){
        Log.i("App.java","tchl getIsManager: "+mPreferences.getBoolean(Constants.ISMANAGER, false));
        return mPreferences.getBoolean(
                Constants.ISMANAGER, false);
    }

    public String getNames(){
        return mPreferences.getString(
                Constants.NAME, null);
    }
    public String getPhone(){
        return mPreferences.getString(
                Constants.SHORTPHONE, null);
    }

    public void setIsManager(String name){
        SharedPreferences mySharedPreferences = getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.USERINFO);
        query.whereEqualTo(Constants.PARSEUSERS, name);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList.size() > 0) {
                        Log.i("App.java", "tchl set ismanager true");
                        editor.putBoolean(Constants.ISMANAGER, true);
                        editor.commit();
                    } else {
                        Log.i("App.java", "tchl set ismanager ｆａｌｓｅ");
                        editor.putBoolean(Constants.ISMANAGER, false);
                        editor.commit();
                    }
                } else {
                    Log.d("score", "tchl Error: " + e.getMessage());
                    editor.putBoolean(Constants.ISMANAGER, false);
                    editor.commit();
                }
            }
        });

    }


    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
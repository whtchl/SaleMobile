package cn.bingoogolapple.refreshlayout.salemobile.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cm.hardwarereport.example.Constants;
import cm.hardwarereport.example.ValidateUserInfo;
import cn.bingoogolapple.refreshlayout.salemobile.App;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.adapter.SwipeRecyclerViewAdapter;
import cn.bingoogolapple.refreshlayout.salemobile.util.ToastUtil;

import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

/**
 * Created by tchl on 11/12/15.
 */
/*public class SettingFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,  BGAOnItemChildClickListener {*/
public class SettingFragment extends Fragment{
    EditText edit_name;
    EditText edit_phone;
    String mname,mphone;
    Button btn_recover;
    protected String TAG;
    protected App mApp;
    private RecyclerView mDataRv;
    private SwipeRecyclerViewAdapter mAdapter;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        getInfo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)    {
        View view = inflater.inflate(R.layout.settings, container, false);
        edit_name = (EditText) view.findViewById(R.id.edit_name);
        edit_phone = (EditText) view.findViewById(R.id.edit_phone);
        btn_recover = (Button) view.findViewById(R.id.btn_recover);
        edit_name.setText(mname);
        edit_phone.setText(mphone);
       // edit_name=mname;
        btn_recover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                getInfo();
            }
        });
        return view;
    }

    private void  getInfo() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences =mApp.getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = mySharedPreferences.edit();
        mname = mySharedPreferences.getString(Constants.NAME, null);
        mphone = mySharedPreferences.getString(Constants.SHORTPHONE, null);
        Log.i(TAG, "tchl " + mname + "  ;" + mphone+"  isManager:"+mySharedPreferences.getBoolean(Constants.ISMANAGER,false));
        Log.i(TAG,"tchl ismanager:"+mApp.getIsManager());
       //Toast.makeText(mApp.getApplicationContext(),"name and phone:"+mname+"  "+mphone,Toast.LENGTH_LONG);
    }

    private void  saveInfo() {
        boolean cancel = false;

        View focusView = null;

        ValidateUserInfo validateUserInfo = new ValidateUserInfo();
        String name = edit_name.getText().toString();
        String phone = edit_phone.getText().toString();
        if(TextUtils.isEmpty(name)){
            edit_name.setError(getString(R.string.error_field_required));
            focusView = edit_name;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(phone) && !validateUserInfo.isPasswordValid(phone)) {
            edit_phone.setError(getString(R.string.error_invalid_password));
            focusView = edit_phone;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            SharedPreferences mySharedPreferences = mApp.getSharedPreferences(Constants.SHAREPRE, Activity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString(Constants.NAME, edit_name.getText().toString());
            editor.putString(Constants.SHORTPHONE, edit_phone.getText().toString());
            editor.commit();
            mApp.setIsManager(edit_name.getText().toString());
            ToastUtil.show(mApp.getText(R.string.reset_name_phone));

        }
    }
}

package cn.bingoogolapple.refreshlayout.salemobile.activitypulllist;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import android.view.View;
import android.content.Intent;
import cm.hardwarereport.example.*;
import cn.bingoogolapple.refreshlayout.salemobile.engine.DataEngine;
import cn.bingoogolapple.refreshlayout.salemobile.model.StorageModel;
import cn.bingoogolapple.refreshlayout.salemobile.util.ToastUtil;

import android.util.Log;

import com.google.gson.Gson;
import com.jinlin.zxing.example.activity.CaptureActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.*;

import android.widget.TextView;
import android.widget.Toast;

public class Model_storagescan extends AppCompatActivity implements OnClickListener{
    private static final String TAG = Model_storagescan.class.getSimpleName();
    EditText mModelName,mModelStorage,mBarCode;
    Button mScan,mSave,mCancel;
    TextView text_model;
    StorageModel hw;
    Boolean mIsSave;
    private  String brandNum,barCode,model,ObjectId,modelStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //intent.


        setContentView(R.layout.activity_model_storagescan3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text_model = (TextView)this.findViewById(R.id.textView_model);
        mModelName = (EditText) this.findViewById(R.id.edit_model);
        mBarCode = (EditText) this.findViewById(R.id.edit_barcode);
        mModelStorage  = (EditText) this.findViewById(R.id.edit_modelStorage);

        mSave = (Button)this.findViewById(R.id.btn_save);
        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        Bundle bundle = intent.getBundleExtra("tag");
        brandNum = bundle.getString("brandNumber");
        model = bundle.getString("model", "");
        barCode = bundle.getString("barcode","");
        ObjectId=bundle.getString("ObjectId","");
        modelStorage = bundle.getString("modelStorage","");
        Log.d(TAG, "tchl barCode:" + barCode+";model"+model+";brandNum:"+brandNum+";ObjectId:"+ObjectId);
        setmBrandNum(brandNum);
        if(model.equals("") && barCode.equals("")){
            Log.d(TAG,"tchl model:"+model+"  barcode:"+barCode);
            mIsSave = true;
        }else{
            this.setTitle(R.string.title_modify_model);
            mSave.setText(R.string.button_modify);
            mModelName.setText(model);
            mBarCode.setText(barCode);
            mModelStorage.setText(modelStorage);

            mIsSave = false;


        }

    }

    public String getmBrandNum() {
        return brandNum;
    }

    public void setmBrandNum(String brandNum) {
        this.brandNum = brandNum;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, Constants.REQUEST_MODEL);
                break;
            case R.id.btn_cancel:
                mModelName.setText("");
                mBarCode.setText("");
                mModelStorage.setText("");
                mModelStorage.setText("");
                mModelName.setFocusable(true);
                break;
            case R.id.btn_save:
                Log.d(TAG,"tchl @@@@@@@@@ model:"+mBarCode.getText().toString()+"  barcode:"+mModelName.getText().toString());

               if(TextUtils.isEmpty(mBarCode.getText().toString())
                       ||TextUtils.isEmpty(mModelName.getText().toString())){
                   ToastUtil.show(R.string.info_enter_model_barcode);
                   Log.d(TAG, "tchl @@@@@@@@@ model:");
               }else {
                       mSave.setEnabled(false);
                   hw = new StorageModel(ObjectId, getmBrandNum(), mModelName.getText().toString(),
                           mBarCode.getText().toString(),mModelStorage.getText().toString());
                   //ng brandNum, String model,String barCode))
                   if (mIsSave) {
                       SubmitInfo(hw);
                   } else {
                       ModifyInfo(hw);
                   }
                   //  Log.d(TAG,"tchl get barcode:"+str);
                   //Intent intent = new Intent();
                   //把返回数据存入Intent
                   // intent.putExtra("result", str);
                   //设置返回数据
                   this.setResult(Activity.RESULT_OK, new Intent());
                   finish();
               }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_MODEL&&data!=null)
        {

            Bundle b=data.getExtras(); //data为B中回传的Intent
          /*  String str = b.getString("result");//str即为回传的
            Log.d("tchl","tchl get result:"+str);*/
            if(DataEngine.IsDuplicateBarcode(b.getString("result"))){
               ToastUtil.show(R.string.duplicate_barcode);
            }else{
                mBarCode.setText( b.getString("result"));
            }


        }
    }

    void SubmitInfo(StorageModel hw){
        //StorageModel hw = new Gson().fromJson(str,StorageModel.class);
        Log.i("CaptureActivity", "tchl1 storage mode:" + hw.toString());
        ParseObject object = new ParseObject(Constants.MOBILESALEINFO);//SubmitCode
        object.put(Constants.BRANDNUM, hw.getBrandNum());
        object.put(Constants.BARCODE,hw.getBarCode());
        object.put(Constants.MODEL,hw.getModel());
        object.put(Constants.MODELSTORAGE,hw.getModelStorage());
        object.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ToastUtil.show(R.string.save_info_success);
                    mSave.setEnabled(true);
                } else {
                    ToastUtil.show(R.string.save_info_failed);
                    mSave.setEnabled(true);
                }
            }
        });
    }

    void ModifyInfo(StorageModel hw){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.MOBILESALEINFO);
/*        query.whereEqualTo(Constants.OBJECTID,hw.getObjectId());
        query.*/
        query.getInBackground(ObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    //gameScore.put(Constants.BRANDNUM, getmBrandNum());
                    gameScore.put(Constants.BARCODE, mBarCode.getText().toString());
                    gameScore.put(Constants.MODEL,mModelName.getText().toString());
                    gameScore.put(Constants.MODELSTORAGE,mModelStorage.getText().toString());
                    gameScore.saveInBackground();
                    ToastUtil.show(R.string.save_info_success);
                }else {
                    ToastUtil.show(R.string.save_info_failed);
                    mSave.setEnabled(true);
                }

            }
        });
    }

}

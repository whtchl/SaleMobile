package cn.bingoogolapple.refreshlayout.salemobile.shipment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.*;
import com.jinlin.zxing.example.activity.CaptureActivity;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.mobile.db.MobileSaleInfoDao;

import cn.bingoogolapple.refreshlayout.salemobile.engine.DataEngine;
import cn.bingoogolapple.refreshlayout.salemobile.model.DetailMobileSaleInfo;
import cn.bingoogolapple.refreshlayout.salemobile.model.StorageModel;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.List;

import cm.hardwarereport.example.Constants;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.util.ToastUtil;
import android.util.Log;
import org.mobile.db.*;
public class shipment extends AppCompatActivity implements OnClickListener {
    EditText mBrand, mModel, mBarCode, mPrice;
    Button mScan, mSave, mCancel;
    DetailMobileSaleInfo dmsi;
    String brandNum;
    //更新已售品牌的手机数量 begin
    String objectId,mmBrand;
    int mcount;
    //end
    Bundle bundle;

    private static final String TAG = shipment.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_shipment);

        mBarCode = (EditText) this.findViewById(R.id.edit_barcode);
        mModel = (EditText) this.findViewById(R.id.edit_model);
        mBrand = (EditText) this.findViewById(R.id.edit_shipment_brand);
        mPrice = (EditText) this.findViewById(R.id.edit_price);

        mSave = (Button) this.findViewById(R.id.btn_shipment_save);
        mScan = (Button) this.findViewById(R.id.btn_scan_shipment);
        mCancel = (Button) this.findViewById(R.id.btn_shipment_cancel);

        mBarCode.setEnabled(false);
        mModel.setEnabled(false);
        mBrand.setEnabled(false);
        //mPrice.setFocusable(true);
        findViewById(R.id.btn_scan_shipment).setOnClickListener(this);
        findViewById(R.id.btn_shipment_save).setOnClickListener(this);
        findViewById(R.id.btn_shipment_cancel).setOnClickListener(this);

        bundle = intent.getBundleExtra(Constants.SHIPMENT);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e(TAG, "brand:" + bundle.getString("brand"));
        mBrand.setText(bundle.getString("brand"));
        brandNum = bundle.getString("brandNum");
        objectId = bundle.getString("objectId");
        Log.d(TAG,"tch onResume objectId:"+objectId);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan_shipment:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, Constants.REQUEST_MODEL);
                break;
            case R.id.btn_shipment_cancel:
                mModel.setText("");
                mBarCode.setText("");
                mPrice.setText("");
                mBrand.setText("");
                mPrice.setFocusable(true);
                break;
            case R.id.btn_shipment_save:
                if(TextUtils.isEmpty(mBarCode.getText().toString())
                        ||TextUtils.isEmpty(mBrand.getText().toString())
                        ||TextUtils.isEmpty(mModel.getText().toString())
                        ||TextUtils.isEmpty(mPrice.getText().toString())){
                    ToastUtil.show(R.string.info_enter_model_barcode_brand_price);
                }else {
                    mSave.setEnabled(false);
                   // DetailMobileSaleInfo(String ObjectId, String brandNum, String model,String barCode,String modelStorage,int price ) {
                    dmsi = new DetailMobileSaleInfo(null,
                            mBrand.getText().toString(),
                            mModel.getText().toString(),
                            mBarCode.getText().toString(),
                            Integer.parseInt(mPrice.getText().toString()));
                    //ng brandNum, String model,String barCode))

                    //  Log.d(TAG,"tchl get barcode:"+str);
                    //Intent intent = new Intent();
                    //把返回数据存入Intent
                    // intent.putExtra("result", str);
                    //设置返回数据
                    SubmitInfo(dmsi);
                    this.setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_MODEL && data != null) {
            Bundle b = data.getExtras();
            mBarCode.setText(b.getString("result"));
            mModel.setText(search(b.getString("result"),brandNum));
        }
    }


    public String  search (String barcode,String brand) {
        String mobile = "";
        if (barcode == null || barcode.equals("")) {
            ToastUtil.show(getResources().getString(R.string.no_query));
        } else {
            // Query 类代表了一个可以被重复执行的查询
            Query query = DataEngine.getMobileInfoDao().queryBuilder()
                    .where(MobileSaleInfoDao.Properties.BarCode.eq(barcode))
                    .where(MobileSaleInfoDao.Properties.BrandNum.eq(brandNum))
                    .orderAsc(MobileSaleInfoDao.Properties.Date)
                    .build();
            // 查询结果以 List 返回
            Log.d("tchl", "tchl size" + query.list().size());
            List notes = query.list();
            if (notes.size() == 1) {
                MobileSaleInfo msi = (MobileSaleInfo)notes.get(0);
                Log.d(TAG,"tchl ********** MobileSaleInfo:"+msi.getModel());
                mobile =  msi.getModel();
            } else {
                ToastUtil.show(this.getString(R.string.mulit_barcode));
                //mModel.setText((String) notes.get(0));
                //Log.d("tchl","tchl tetst"+ (String) notes.get(0));

            }

            //ToastUtils.show(this, "There have " + notes.size() + " records");
        }
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return mobile;
    }
    void SubmitInfo(DetailMobileSaleInfo dmsi){
        //StorageModel hw = new Gson().fromJson(str,StorageModel.class);
        Log.i(TAG, "tchl1 DetailMobileSaleInfo:" + dmsi.toString());
        ParseObject object = new ParseObject(Constants.DETAILMOBILESALEINFO);//SubmitCode
        object.put(Constants.BRAND, dmsi.getBrand());
        object.put(Constants.BARCODE,dmsi.getBarCode());
        object.put(Constants.MODEL,dmsi.getModel());
        object.put(Constants.PRICE,dmsi.getPrice());
        mmBrand = dmsi.getBrand();
        object.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    getMobileTotalSale(mmBrand);

                } else {
                     ;
                }
            }
        });
    }

    private void getMobileTotalSale(String mBrand){
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.DETAILMOBILESALEINFO);
            query.whereEqualTo(Constants.BRAND, mBrand);
            query.countInBackground(new CountCallback() {
                public void done(int count, ParseException e) {
                    if (e == null) {
                        // The count request succeeded. Log the count
                        Log.d(TAG, "Brand has soled " + count+"  ojbectId:"+objectId);
                        mcount = count;
                        updateBrandTotalSale(objectId);
                    } else {
                        // The request failed
                    }
                }
            });
    }

    private void updateBrandTotalSale(String objectId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.BRANDINFO);
        // Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    gameScore.put(Constants.TOTALSALE, String.valueOf(mcount));
                    gameScore.saveInBackground();
                    ToastUtil.show(R.string.save_info_success);
                    mSave.setEnabled(true);
                }else{
                    ToastUtil.show(R.string.save_info_failed);
                    mSave.setEnabled(true);
                }

            }
        });
    }

}

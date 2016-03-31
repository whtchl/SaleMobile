package cn.bingoogolapple.refreshlayout.salemobile.engine;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

/*import org.apache.http.conn.*/
import java.util.ArrayList;
import java.util.List;

import cm.hardwarereport.example.Constants;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.model.BannerModel;
import cn.bingoogolapple.refreshlayout.salemobile.model.RefreshModel;
import cn.bingoogolapple.refreshlayout.salemobile.model.StaggeredModel;
import cn.bingoogolapple.refreshlayout.salemobile.model.StorageModel;
import android.util.Log;
import com.parse.ParseObject;
import  com.parse.*;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import java.util.*;
import org.mobile.db.*;
import cn.bingoogolapple.refreshlayout.salemobile.util.*;
import de.greenrobot.dao.query.Query;

public class DataEngine {
    static String TAG = "DateEngine";
    static int number = 0;
    static int begin = 0;
    static  private BrandInfoDao brandDao;
    static private MobileSaleInfoDao mobileDao;
    static  Boolean mDuplicate = true;
    /*public static  SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");*/
    /* public static Date dt = null;*/
    static private Cursor cursor;

    public  static void BrandDaoSize(){
        Query query = DataEngine.getBrandDao().queryBuilder()
                .orderAsc(BrandInfoDao.Properties.Date)
                .build();
        Log.d(TAG,"tchl BrandDao size:"+ query.list().size());
    }

    public  static void MobileDaoSize(){
        Query query = DataEngine.getMobileInfoDao().queryBuilder()
                .orderAsc(MobileSaleInfoDao.Properties.Date)
                .build();
        Log.d(TAG,"tchl MobileDao size:"+ query.list().size());
    }

    //没有这个品牌就-》加入到数据库；没有这个品牌-》不加入到数据库
    private static void addBrandInfo(String brandNum, String mbrand, java.util.Date date, String objectId) {
        Query query = DataEngine.getBrandDao().queryBuilder()
                .where(BrandInfoDao.Properties.BrandNum.eq(brandNum))
                .orderAsc(BrandInfoDao.Properties.Date)
                .build();
        Log.d(TAG,"tchl Brand size:"+ query.list().size()+"  "+query.list().toString());

        if(query.list().size()==0){
            Log.d(TAG,"tchl add BrandInfo:"+mbrand);
            BrandInfo brand = new BrandInfo(null, brandNum, mbrand, new Date(),objectId);
            brandDao = DBLoader.getDaoSession().getBrandInfoDao();
            brandDao.insert(brand);
        }
    }

    private static void addMobileInfo(String brandNum,String model,String barCode,String modelStorage,String objectId,Date date){
        Query query = DataEngine.getMobileInfoDao().queryBuilder()
                .where(MobileSaleInfoDao.Properties.Model.eq(model))
                .orderAsc(MobileSaleInfoDao.Properties.Date)
                .build();
        Log.d(TAG, "tchl Mobile size:" + query.list().size() + "  " + query.list().toString());
        if(query.list().size()==0) {
            Log.d(TAG,"tchl add MobileInfo:"+model);
            MobileSaleInfo mobileInfo = new MobileSaleInfo(null,brandNum,model,barCode,modelStorage,objectId,new Date());
            mobileDao = DBLoader.getDaoSession().getMobileSaleInfoDao();
            mobileDao.insert(mobileInfo);
        }

    }

    public static  boolean IsDuplicateBarcode(String barcode){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.MOBILESALEINFO);
        query.whereEqualTo(Constants.BARCODE, barcode);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    Log.d(TAG, "Sean has played " + count + " games");
                    if (count == 0) {
                        mDuplicate = false;
                        ToastUtil.show(Constants.ApplicationId);
                    }

                } else {
                    // The request failed
                    mDuplicate = true;
                }
            }
        });
        Log.d(TAG,"Have duplicate barcode");
        return mDuplicate;
    }

    public static MobileSaleInfoDao  getMobileInfoDao(){
        return DBLoader.getDaoSession().getMobileSaleInfoDao();

    }

    public static BrandInfoDao  getBrandDao(){
        return DBLoader.getDaoSession().getBrandInfoDao();

    }

    private static AsyncHttpClient sAsyncHttpClient = new AsyncHttpClient();

   /* public static void loadInitDatas(final RefreshModelResponseHandler responseHandler){
        Log.i(TAG, "tchl");
        GetAllObject(responseHandler);

    }*/
/*   public static void getTotalSale(String mBrand){
       ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.DETAILMOBILESALEINFO);
       query.whereEqualTo(Constants.BRAND, mBrand);
       query.countInBackground(new CountCallback() {
           public void done(int count, ParseException e) {
               if (e == null) {
                   // The count request succeeded. Log the count
                   Log.d("score", "Sean has played " + count + " games");
               } else {
                   // The request failed
               }
           }
       });

   }*/


    public static  void loadInitStorageDatasForDB() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.MOBILESALEINFO);
        //query.whereEqualTo(Constants.BRANDNUM, brandNum);
        query.addAscendingOrder(Constants.CREATEDAT);
        query.setLimit(Constants.init_num);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "tchl Retrieved brand" + scoreList.size() + " scores");
                    Iterator iter = scoreList.iterator();
                    for (int i = scoreList.size() - 1; i >= 0; i--) {
                        addMobileInfo(scoreList.get(i).getString(Constants.BRANDNUM), scoreList.get(i).getString(Constants.MODEL),
                                scoreList.get(i).getString(Constants.BARCODE), scoreList.get(i).getString(Constants.MODELSTORAGE),
                                scoreList.get(i).getObjectId(), null);
                    }
                    //Log.i(TAG, "initdata  loadInitStorageDatasForDB size:" + i);
                    //responseHandler.onSuccess(storageModel);
                } else {
                    Log.d("loadInitStorageDatasForDB", "Error: " + e.getMessage());
                    //responseHandler.onFailure();
                }
            }
        });

        if(Constants.DEBUG){
            MobileDaoSize();
        }

    }


    //入库
    public static  void loadInitStorageDatas(final StorageModelResponseHandler responseHandler,String brandNum) {
        number = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.MOBILESALEINFO);
        query.whereEqualTo(Constants.BRANDNUM, brandNum);
        query.addAscendingOrder(Constants.CREATEDAT);
        query.setLimit(Constants.init_num);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "tchl Retrieved brand" + scoreList.size() + " scores");
                    StorageModel rm = null;
                    List<StorageModel> storageModel = new ArrayList<StorageModel>();
                    Iterator iter = scoreList.iterator();
                    for (int i = scoreList.size()-1 ; i >=0; i--) {
                        //(String ObjectId,String Bu,String Ro,String Eq,String Ph,String Ac,String Ap,String Em,String St,String report,String other) {
                        rm = new StorageModel(scoreList.get(i).getObjectId(),
                                scoreList.get(i).getString(Constants.BRANDNUM),
                                scoreList.get(i).getString(Constants.MODEL),
                                scoreList.get(i).getString(Constants.BARCODE),
                                scoreList.get(i).getString(Constants.MODELSTORAGE));

                        Log.i(TAG, "tchl storageModel brand info:" + rm.toString());
                        storageModel.add(rm);
                    }
                    Log.i(TAG, "initdata  tchl StorageModel size:" + storageModel.size());
                    responseHandler.onSuccess(storageModel);
                } else {
                    Log.d("scoretchl", "Error: " + e.getMessage());
                    Log.d("post", "retrieved a related post");
                    responseHandler.onFailure();
                }
            }
        });
    }

    public static void loadStorageNewData( final StorageModelResponseHandler responseHandler,String brandNum) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.MOBILESALEINFO);
        query.whereEqualTo(Constants.BRANDNUM, brandNum);
        query.addAscendingOrder(Constants.CREATEDAT);

        query.setLimit(Constants.init_num);
        //query.setSkip(number);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "tchl Retrieved " + scoreList.size() + " scores");
                    StorageModel rm = null;
                    //begin = (scoreList.size() >= Constants.init_num) ? Constants.init_num:scoreList.size();
                    List<StorageModel> storageModel = new ArrayList<StorageModel>();
                    Iterator iter = scoreList.iterator();
                    for (int i =scoreList.size()-1; i >=0; i-- ) {
                        //Log.i(TAG, "tchl" + scoreList.get(i).getString("Building") + "DATE: " + df.format(scoreList.get(i).getDate(Constants.CREATEDAT)));
                        rm = new StorageModel(scoreList.get(i).getObjectId(),
                                scoreList.get(i).getString(Constants.BRANDNUM),
                                scoreList.get(i).getString(Constants.MODEL),
                                scoreList.get(i).getString(Constants.BARCODE),
                                scoreList.get(i).getString(Constants.MODELSTORAGE));
                        Log.i(TAG, "tchl StorageModel info:" + rm.toString());
                        storageModel.add(rm);
                    }
                    // number = number+begin;
                    Log.i(TAG, "newdata tchl StorageModel size:" + storageModel.size());
                    responseHandler.onSuccess(storageModel);
                } else {
                    Log.d("scoretchl", "Error: " + e.getMessage());
                    Log.d("post", "retrieved a related post");
                    responseHandler.onFailure();
                }
            }
        });

    }




    public static  int getNumber(){
        return number;
    }
    public static void  setNumber(int i){ number = i; }



    //下载品牌和型号
    public static void LoadBrandAndModel(SharedPreferences mySharedPreferences ) {

             ;

    }


    public static  void loadInitDatas(final RefreshModelResponseHandler responseHandler) {
       number = 0;
       ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.BRANDINFO);
       query.addAscendingOrder(Constants.CREATEDAT);
       query.setLimit(Constants.init_num);
       query.findInBackground(new FindCallback<ParseObject>() {
           public void done(List<ParseObject> scoreList, ParseException e) {
               if (e == null) {
                   Log.d(TAG, "tchl Retrieved brand" + scoreList.size() + " scores");
                   RefreshModel rm = null;
                   List<RefreshModel> refreshModels = new ArrayList<RefreshModel>();
                   Iterator iter = scoreList.iterator();
                   for (int i = scoreList.size()-1 ; i >=0; i--) {
             //(String ObjectId,String Bu,String Ro,String Eq,String Ph,String Ac,String Ap,String Em,String St,String report,String other) {
                       rm = new RefreshModel(scoreList.get(i).getObjectId(),
                               scoreList.get(i).getString(Constants.BRAND),
                               scoreList.get(i).getString(Constants.TOTALSALE),
                               scoreList.get(i).getString(Constants.BRANDNUM));
                       Log.i(TAG, "tchl refreshmode brand info:" + rm.toString());

                       addBrandInfo(scoreList.get(i).getString(Constants.BRANDNUM), scoreList.get(i).getString(Constants.BRAND),
                               null, scoreList.get(i).getObjectId());
                       refreshModels.add(rm);
                   }
                   Log.i(TAG, "initdata  tchl refreshModels size:" + refreshModels.size());
                   responseHandler.onSuccess(refreshModels);
               } else {
                   Log.d("scoretchl", "Error: " + e.getMessage());
                   Log.d("post", "retrieved a related post");
                   responseHandler.onFailure();
               }
           }
       });

        loadInitStorageDatasForDB();
        if(Constants.DEBUG){
            MobileDaoSize();
            BrandDaoSize();
        }
   }

    public static void loadNewData(final int pageNumber, final RefreshModelResponseHandler responseHandler) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.BRANDINFO);
        query.addAscendingOrder(Constants.CREATEDAT);
        query.setLimit(Constants.init_num);
        //query.setSkip(number);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "tchl Retrieved " + scoreList.size() + " scores");
                    RefreshModel rm = null;
                    //begin = (scoreList.size() >= Constants.init_num) ? Constants.init_num:scoreList.size();
                    List<RefreshModel> refreshModels = new ArrayList<RefreshModel>();
                    Iterator iter = scoreList.iterator();
                    for (int i =scoreList.size()-1; i >=0; i-- ) {
                        //Log.i(TAG, "tchl" + scoreList.get(i).getString("Building") + "DATE: " + df.format(scoreList.get(i).getDate(Constants.CREATEDAT)));
                        rm = new RefreshModel(scoreList.get(i).getObjectId(),
                                scoreList.get(i).getString(Constants.BRAND),
                                scoreList.get(i).getString(Constants.TOTALSALE),
                                scoreList.get(i).getString(Constants.BRANDNUM));
                        Log.i(TAG, "tchl refreshmode info:" + rm.toString());
                        refreshModels.add(rm);
                    }
                   // number = number+begin;
                    Log.i(TAG, "newdata tchl refreshModels size:" + refreshModels.size());
                    responseHandler.onSuccess(refreshModels);
                } else {
                    Log.d("scoretchl", "Error: " + e.getMessage());
                    Log.d("post", "retrieved a related post");
                    responseHandler.onFailure();
                }
            }
        });

        loadInitStorageDatasForDB();
        if(Constants.DEBUG){
            MobileDaoSize();
            BrandDaoSize();
        }

    }
    public static void loadMoreData(final int pageNumber, final RefreshModelResponseHandler responseHandler) {
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时1秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xly3m.com1.z0.glb.clouddn.com/moredata" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i(TAG,"tchl loadMoreData onSuccess:"+responseString);
                        List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 1000);
    }


    public static void loadDefaultStaggeredData(final StaggeredModelResponseHandler responseHandler) {
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_default.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });
    }

    public static void loadNewStaggeredData(final int pageNumber, final StaggeredModelResponseHandler responseHandler) {
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时2秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_new" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 2000);
    }

    public static void loadMoreStaggeredData(final int pageNumber, final StaggeredModelResponseHandler responseHandler) {
        // 测试数据放到七牛云存储的，再加上WiFi环境，加载数据非常快，看不出效果，这里延时1秒再请求的网络数据
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/staggered_more" + pageNumber + ".json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        responseHandler.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<StaggeredModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<StaggeredModel>>() {
                        }.getType());
                        responseHandler.onSuccess(refreshModels);
                    }
                });
            }
        }, 1000);
    }

    public static View getCustomHeaderView(Context context) {
        View headerView = View.inflate(context, R.layout.view_custom_header, null);
        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        final List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            views.add(View.inflate(context, R.layout.view_image, null));
        }
        banner.setViews(views);
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/5item.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BannerModel bannerModel = new Gson().fromJson(responseString, BannerModel.class);
                ImageLoader imageLoader = ImageLoader.getInstance();
                for (int i = 0; i < views.size(); i++) {
                    imageLoader.displayImage(bannerModel.imgs.get(i), (ImageView) views.get(i));
                }
                banner.setTips(bannerModel.tips);
            }
        });
        return headerView;
    }

     //AddStorageModel
    public static View AddStorageModel(Context context) {
        View headerView = View.inflate(context, R.layout.view_add_storagescan, null);
      //  final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
       // final List<View> views = new ArrayList<>();
       /// for (int i = 0; i < 5; i++) {
       //     views.add(View.inflate(context, R.layout.view_image, null));
       // }
        //banner.setViews(views);
/*        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/5item.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BannerModel bannerModel = new Gson().fromJson(responseString, BannerModel.class);
                ImageLoader imageLoader = ImageLoader.getInstance();
                for (int i = 0; i < views.size(); i++) {
                    imageLoader.displayImage(bannerModel.imgs.get(i), (ImageView) views.get(i));
                }
                banner.setTips(bannerModel.tips);
            }
        });*/
        return headerView;
    }
/*
    public interface HardwareModelResponseHandler {
        void onFailure();

        void onSuccess(List<ParseObject> hardwareModels);
    }*/
    public interface RefreshModelResponseHandler {
        void onFailure();

        void onSuccess(List<RefreshModel> refreshModels);

    }
    public interface StorageModelResponseHandler {
        void onFailure();

        void onSuccess(List<StorageModel> storageModels);

    }

    public interface StaggeredModelResponseHandler {
        void onFailure();

        void onSuccess(List<StaggeredModel> staggeredModels);
    }
}
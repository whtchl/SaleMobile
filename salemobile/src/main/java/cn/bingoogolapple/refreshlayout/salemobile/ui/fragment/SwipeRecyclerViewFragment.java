package cn.bingoogolapple.refreshlayout.salemobile.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cm.hardwarereport.example.Constants;
import cm.hardwarereport.example.StatusDialog;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.activitypulllist.Model_storagescan;
import cn.bingoogolapple.refreshlayout.salemobile.adapter.SwipeRecyclerViewAdapter;
import cn.bingoogolapple.refreshlayout.salemobile.engine.DataEngine;
import cn.bingoogolapple.refreshlayout.salemobile.model.RefreshModel;
import cn.bingoogolapple.refreshlayout.salemobile.widget.Divider;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import  android.widget.Toast;
import android.content.Intent;
import com.parse.*;
import cn.bingoogolapple.refreshlayout.salemobile.shipment.*;
/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class SwipeRecyclerViewFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private SwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    private int singleSelectedId;
    private String[] items = {"item0", "item1", "item2", "item3", "item4", "item5"};

    public static final String STATUS_DIALOG ="status_dialog";
    public static final int REQUEST_STATUS = 0X110;

    private int mCurrentPos ;




    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_recyclerview);
        mRefreshLayout = getViewById(R.id.rl_recyclerview_refresh);
        mDataRv = getViewById(R.id.rv_recyclerview_data);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);

        mAdapter = new SwipeRecyclerViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mApp), false);
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(mApp, true));

        mDataRv.addItemDecoration(new Divider(mApp));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mApp);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mDataRv.setAdapter(mAdapter);
    }

    @Override
    protected void onUserVisible() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;
        DataEngine.loadInitDatas(new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mAdapter.setDatas(refreshModels);
            }
        });
    }
    /*
    * return false：there is no data
    *        true:there is data.
    * */
    public  boolean  hasNewData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SubmitCode");
        query.addAscendingOrder(Constants.CREATEDAT);

        try{
            Log.i(TAG,"tchl count:"+query.count()+"  number:"+DataEngine.getNumber());
            if(query.count()<=mAdapter.getItemCount()){
                   return  false;
            }else{
                return true;
            }
        }catch (ParseException e){
            Log.d(TAG,"tchl "+e.getMessage());
            return  false;
        }

    }

   /* 每次指更新新的数据，旧的数据不更新
    * return false：there is no data
    *        true:there is data.
    * *//*
    public  boolean  hasNewData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SubmitCode");
        query.addAscendingOrder(Constants.CREATEDAT);

        try{
            Log.i(TAG,"tchl count:"+query.count()+"  number:"+DataEngine.getNumber());
            if(query.count()<=DataEngine.getNumber()){
                return  false;
            }else{
                return true;
            }
        }catch (ParseException e){
            Log.d(TAG,"tchl "+e.getMessage());
            return  false;
        }

    }*/
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "tchl onBGARefreshLayoutBeginRefreshing");
/*        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了!");
            return;
        }*/
/*        if(hasNewData()==false){
            showToast("没有最新数据了");
            return;
        }*/
        DataEngine.loadNewData(mAdapter.getItemCount(), new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mRefreshLayout.endRefreshing();
                //mAdapter.addNewDatas(refreshModels);
                mAdapter.clear();
                mAdapter.setDatas(refreshModels);
                mDataRv.smoothScrollToPosition(0);
            }
        });

        Log.i(TAG, "tchl list count = " + mAdapter.getItemCount());
       // mAdapter.setItem();
       // mAdapter.setItem();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endLoadingMore();
        return false;
/*        mMorePageNumber++;
        if (mMorePageNumber > 5) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }
        DataEngine.loadMoreData(mMorePageNumber, new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mRefreshLayout.endLoadingMore();
                mAdapter.addMoreDatas(refreshModels);
            }
        });*/
        //return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_shipment) {

            Intent intent = new Intent();
            intent.setClass(this.getActivity(), shipment.class);

            Bundle bundle = new Bundle();
            bundle.putString("brand", mAdapter.getItem(position).getBrand());
            bundle.putString("brandNum",mAdapter.getItem(position).getBrandNum());
            bundle.putString("objectId",mAdapter.getItem(position).getObjectId());
/*            bundle.putString("model", mAdapter.getItem(position).getModel());
            bundle.putString("barcode", mAdapter.getItem(position).getBarCode());
            bundle.putString("ObjectId",mAdapter.getItem(position).getObjectId());
            bundle.putString("modelStorage",mAdapter.getItem(position).getModelStorage());
            Log.e(TAG,"tchl objectId:"+mAdapter.getItem(position).getObjectId());*/
            intent.putExtra(Constants.SHIPMENT, bundle);
            //mAdapter.removeItem(position);

            startActivityForResult(intent, Constants.REQUEST_MODEL_SCAN);
/*            mAdapter.closeOpenedSwipeItemLayoutWithAnim();
            mAdapter.removeItem(position);*/
        }
    }


    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
/*        if (childView.getId() == R.id.tv_item_swipe_shipment) {
            showToast("长按了删除 " + mAdapter.getItem(position).getBrand());
            return true;
        }*/
        return false;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        ;
/*        if(mApp.getIsManager()) {
            showToast("点击了条目 " + mAdapter.getItem(position).getBrand());
            mCurrentPos = position;
            StatusDialog dialog = new StatusDialog();
            //注意setTargetFragment
            //dialog.setArguments();
            dialog.setTargetFragment(SwipeRecyclerViewFragment.this, REQUEST_STATUS);
            dialog.show(getFragmentManager(), STATUS_DIALOG);
        }*/

    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
       // showToast("长按了条目 " + mAdapter.getItem(position).getBu());
        return true;
    }

    //接收返回回来的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_STATUS)
        {
            final String evaluate = data.getStringExtra(StatusDialog.RESPONSE_STATUS);
            Toast.makeText(getActivity(), evaluate, Toast.LENGTH_SHORT).show();
            //Log.i(TAG,"tchl!:"+mAdapter.getItem(mCurrentPos).getOb()+"  pos:"+mCurrentPos+"  "+mAdapter.getItem(mCurrentPos).getBu());

            ParseQuery<ParseObject> query = ParseQuery.getQuery(getActivity().getString(R.string.parse_object));
            query.getInBackground(mAdapter.getItem(mCurrentPos).getBrand(), new GetCallback<ParseObject>() {
                public void done(ParseObject gameScore, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        gameScore.put(Constants.STATUS,evaluate);
                        gameScore.put(Constants.ACCETPER,mApp.getNames());
                        gameScore.put(Constants.ACCEPTERPHONE,mApp.getPhone());
                        gameScore.saveInBackground();
                    }
                }
            });

            /*Intent intent = new Intent();
            intent.putExtra(RESPONSE, evaluate);
            getActivity().setResult(ListTitleFragment.REQUEST_DETAIL, intent);*/
        }

    }


}
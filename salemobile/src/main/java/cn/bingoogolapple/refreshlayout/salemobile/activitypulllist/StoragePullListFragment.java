package cn.bingoogolapple.refreshlayout.salemobile.activitypulllist;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import cm.hardwarereport.example.Constants;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.adapter.NormalAdapterViewAdapter;
import cn.bingoogolapple.refreshlayout.salemobile.adapter.NormalScanAdapterViewAdapter;
import cn.bingoogolapple.refreshlayout.salemobile.engine.DataEngine;
import cn.bingoogolapple.refreshlayout.salemobile.model.RefreshModel;
import cn.bingoogolapple.refreshlayout.salemobile.model.StorageModel;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.BaseFragment;
import android.widget.ImageView;
/**
 * A placeholder fragment containing a simple view.
 */
public class StoragePullListFragment extends BaseFragment implements
        BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private static final String TAG = StoragePullListFragment.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private ListView mDataLv;
    private NormalScanAdapterViewAdapter mAdapter;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    ImageView mStoragescan;

    Button btn_sale;

    public StoragePullListFragment() {
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        Log.i(TAG, "tchl initView");
        setContentView(R.layout.fragment_storage_pull_list);//fragment_listview
        mRefreshLayout = getViewById(R.id.rl_storageview_refresh);
        mDataLv = getViewById(R.id.lv_storageview_data);
        mStoragescan = (ImageView) getViewById(R.id.iv_add_storagescan);
     //   btn_sale= (Button)

    }
    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);
        // 设置正在加载更多时不显示加载更多控件
        mRefreshLayout.setIsShowLoadingMoreView(false);

        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);
        mDataLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        mAdapter = new NormalScanAdapterViewAdapter(mApp);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(mApp, true);
        moocStyleRefreshViewHolder.setUltimateColor(getResources().getColor(R.color.custom_imoocstyle));
        moocStyleRefreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.custom_mooc_icon));
//        moocStyleRefreshViewHolder.setLoadMoreBackgroundColorRes(R.color.custom_imoocstyle);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
//        moocStyleRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.custom_imoocstyle);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        mRefreshLayout.setCustomHeaderView(DataEngine.AddStorageModel(mApp), true);

        mDataLv.setAdapter(mAdapter);
    }

    @Override
    protected void onUserVisible() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;
        DataEngine.loadInitStorageDatas(new DataEngine.StorageModelResponseHandler() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess(List<StorageModel> storageModels) {
                mAdapter.setDatas(storageModels);
            }
        }, ((StoragePullList) getActivity()).getmBrandNum());
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "tchl *********** onBGARefreshLayoutBeginRefreshing");
        DataEngine.loadStorageNewData(new DataEngine.StorageModelResponseHandler() {
            //mAdapter.getItemCount(),
            @Override
            public void onFailure() {
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(List<StorageModel> StorageModel) {
                mRefreshLayout.endRefreshing();
                mAdapter.addNewDatas(StorageModel);
                mAdapter.clear();
                mAdapter.setDatas(StorageModel);
                mDataLv.smoothScrollToPosition(0);

            }
        }, ((StoragePullList) getActivity()).getmBrandNum());
        Log.i(TAG, "tchl list count = " + mAdapter.getCount());
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endLoadingMore();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //showToast("点击了条目 " + mAdapter.getItem(position).getBrandNum());
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), Model_storagescan.class);

        Bundle bundle = new Bundle();
        bundle.putString("brandNumber", mAdapter.getItem(position).getBrandNum());
        bundle.putString("model", mAdapter.getItem(position).getModel());
        bundle.putString("barcode", mAdapter.getItem(position).getBarCode());
        bundle.putString("ObjectId",mAdapter.getItem(position).getObjectId());
        bundle.putString("modelStorage",mAdapter.getItem(position).getModelStorage());
        Log.e(TAG,"tchl objectId:"+mAdapter.getItem(position).getObjectId());
        intent.putExtra("tag", bundle);

        startActivityForResult(intent, Constants.REQUEST_MODEL_SCAN);
        //startActivity(new Intent(StoragePullList.this, Model_storagescan.class));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //showToast("长按了" + mAdapter.getItem(position).getBrand());
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
/*        if (childView.getId() == R.id.tv_item_normal_scan) {
            Log.i(TAG, "tchl:******** "+  ((StoragePullList)getActivity()).getmBrandNum());

        }*/
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
/*        if (childView.getId() == R.id.tv_item_normal_scan) {
           // showToast("长按了删除 " + mAdapter.getItem(position).getBrand());
            return true;
        }*/
        return false;
    }


}

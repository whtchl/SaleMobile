package cn.bingoogolapple.refreshlayout.salemobile.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.engine.DataEngine;
import cn.bingoogolapple.refreshlayout.salemobile.ui.activity.MainActivity;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public class NormalViewFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = NormalViewFragment.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private TextView mClickableLabelTv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_normalview);
        mRefreshLayout = getViewById(R.id.rl_normalview_refresh);
        mClickableLabelTv = getViewById(R.id.tv_normalview_clickablelabel);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);

        mClickableLabelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了测试文本");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mRefreshLayout.setRefreshViewHolder(new BGAStickinessRefreshViewHolder(mApp, true));
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mApp), false);
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(MainActivity.LOADING_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mClickableLabelTv.setText("加载最新数据完成");
            }
        }.execute();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(MainActivity.LOADING_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endLoadingMore();
                Log.i(TAG, "上拉加载更多完成");
            }
        }.execute();
        return true;
    }
}
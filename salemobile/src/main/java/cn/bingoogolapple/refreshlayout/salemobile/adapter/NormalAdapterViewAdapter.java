package cn.bingoogolapple.refreshlayout.salemobile.adapter;

import android.content.Context;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.model.RefreshModel;
import android.util.Log;
public class NormalAdapterViewAdapter extends BGAAdapterViewAdapter<RefreshModel> { //BGARecyclerViewAdapter

    public NormalAdapterViewAdapter(Context context) {
        super(context, R.layout.item_normal);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_normal_storage);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_normal_storage);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, RefreshModel model) {
        Log.d("NormalAdapter","tchl call fillData");
        viewHolderHelper.setText(R.id.item_brand, model.getBrand());
    }
}
package cn.bingoogolapple.refreshlayout.salemobile.adapter;

import android.content.Context;
import android.util.Log;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.model.StorageModel;

public class NormalScanAdapterViewAdapter extends BGAAdapterViewAdapter<StorageModel> { //BGARecyclerViewAdapter

    public NormalScanAdapterViewAdapter(Context context) {
        super(context, R.layout.item_scan);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
       // viewHolderHelper.setItemChildClickListener(R.id.tv_item_normal_scan);
       // viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_normal_scan);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, StorageModel model) {
        Log.d("NormalAdapter","tchl call fillData");
        viewHolderHelper.setText(R.id.item_model, model.getModel());
        viewHolderHelper.setText(R.id.item_barcode, model.getBarCode());
        viewHolderHelper.setText(R.id.item__ObjectId,model.getObjectId());
        viewHolderHelper.setText(R.id.item_modelStorage,model.getModelStorage());

    }
}
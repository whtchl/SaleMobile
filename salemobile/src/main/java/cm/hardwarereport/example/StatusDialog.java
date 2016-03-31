package cm.hardwarereport.example;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.SwipeRecyclerViewFragment;

/**
 * Created by tchl on 11/2/15.
 */
public class StatusDialog extends DialogFragment
{
    private String[] mStatus = new String[] { "接受", "完成" };
    public static final String RESPONSE_STATUS = "response_status";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getActivity().getString(R.string.choose_status)).setItems(mStatus,
                new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        setResult(which);
                    }
                });
        return builder.create();
    }

    // 设置返回数据
    protected void setResult(int which)
    {
        // 判断是否设置了targetFragment
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(RESPONSE_STATUS, mStatus[which]);
        getTargetFragment().onActivityResult(SwipeRecyclerViewFragment.REQUEST_STATUS,
                Activity.RESULT_OK, intent);

    }
}
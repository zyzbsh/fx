package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/20.
 */
public class ClosePositionDialog extends Dialog implements View.OnClickListener{

    private String mContractName;

    private DialogListener mDialogListener;

    public ClosePositionDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
    }

    public ClosePositionDialog(Context context, String contractName) {
        super(context);
        mContractName = contractName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_position);
        TextView contractTv = (TextView) findViewById(R.id.dialog_close_position_contract_tv);
        contractTv.setText(getContext().getString(R.string.contract_name, mContractName));
        findViewById(R.id.dialog_close_position_ok_tv).setOnClickListener(this);
        findViewById(R.id.dialog_close_position_cancel_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_close_position_ok_tv:
                if (mDialogListener != null) {
                    dismiss();
                    mDialogListener.ok();
                }
                break;
            case R.id.dialog_close_position_cancel_tv:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setDialogListener(DialogListener listener) {
        mDialogListener = listener;
    }

    public interface DialogListener{
        public void ok();
    }
}

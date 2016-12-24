package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.tools.UIUtil;

/**
 * Created by pc on 2016/12/24.
 */
public class WithdrawInputDialog extends Dialog{

    private TextView mTitleTv;
    private TextView mRemindTv;
    private EditText mEdit;
    private String mTitle;
    private OnOkListener mListener;

    public WithdrawInputDialog(Context context, String title, OnOkListener listener) {
        super(context);
        mTitle = title;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw_input);
        initViews();
        initParams();;
    }

    private void initParams() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_withdraw_input_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 40);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        layout.setLayoutParams(params);
    }

    private void initViews(){
        mTitleTv = (TextView) findViewById(R.id.dialog_withdraw_input_title_tv);
        mTitleTv.setText(mTitle);
        mRemindTv = (TextView) findViewById(R.id.dialog_withdraw_input_remind_tv);
        mRemindTv.setText("请输入"+ mTitle);
        mEdit = (EditText) findViewById(R.id.dialog_withdraw_input_edt);
        findViewById(R.id.dialog_withdraw_confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null) {
                    mListener.onConfirm(mEdit.getText().toString().trim());
                }
            }
        });
        findViewById(R.id.dialog_withdraw_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnOkListener{
        public void onConfirm(String name);
    }

}

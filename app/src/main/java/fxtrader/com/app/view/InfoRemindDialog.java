package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.tools.UIUtil;

/**
 * 信息提示
 * Created by zhangyuzhu on 2016/11/29.
 */
public class InfoRemindDialog extends Dialog implements View.OnClickListener{

    private DialogInfoRemindListener mListener;

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.dialog_info_remind_close_tv:
                this.dismiss();
                mListener.close();
                break;
            case R.id.dialog_info_remind_bind_tv:
                mListener.bind();
                break;
            default:
                break;
        }
    }

    public interface DialogInfoRemindListener{
        public void close();
        public void bind();
    }

    public InfoRemindDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_remind);
        initParams();
        findViewById(R.id.dialog_info_remind_close_tv).setOnClickListener(this);
        findViewById(R.id.dialog_info_remind_bind_tv).setOnClickListener(this);
    }

    private void initParams() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_info_remind_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 40);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        layout.setLayoutParams(params);
    }

    public void setOnDialogListener(DialogInfoRemindListener listener) {
        mListener = listener;
    }

}

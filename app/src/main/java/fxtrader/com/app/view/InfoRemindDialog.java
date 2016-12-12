package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import fxtrader.com.app.R;

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
        findViewById(R.id.dialog_info_remind_close_tv).setOnClickListener(this);
        findViewById(R.id.dialog_info_remind_bind_tv).setOnClickListener(this);
    }

    public void setOnDialogListener(DialogInfoRemindListener listener) {
        mListener = listener;
    }

}

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
public class SignSuccessDialog extends Dialog implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_info_remind_close_tv:
                this.dismiss();
                break;
            case R.id.dialog_info_remind_bind_tv:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public SignSuccessDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign_success);
        initParams();
        findViewById(R.id.dialog_sign_success_close_tv).setOnClickListener(this);
        findViewById(R.id.dialog_sign_success_get_tv).setOnClickListener(this);
    }

    private void initParams() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_info_remind_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 40);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        layout.setLayoutParams(params);
    }


}

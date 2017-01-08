package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.entity.SystemBulletinEntity;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.UIUtil;

/**
 * 信息提示
 * Created by zhangyuzhu on 2016/11/29.
 */
public class SystemBulletinDialog extends Dialog implements View.OnClickListener{

    private SystemBulletinEntity mEntity;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.system_bulletin_close_btn:
            case R.id.system_bulletin_close_tv:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public SystemBulletinDialog(Context context, SystemBulletinEntity entity) {
        super(context);
        mEntity = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_system_bulletin);

        if (mEntity != null && mEntity.getObject() != null) {
            initParams();
            findViewById(R.id.system_bulletin_close_btn).setOnClickListener(this);
            findViewById(R.id.system_bulletin_close_tv).setOnClickListener(this);
            TextView titleTv = (TextView) findViewById(R.id.system_bulletin_title_tv);
            TextView timeTv = (TextView) findViewById(R.id.system_bulletin_time_tv);
            TextView contentTv = (TextView) findViewById(R.id.system_bulletin_content_tv);
            timeTv.setText("时间:" + DateTools.changeToDate2(mEntity.getObject().getCreateDate()));
            contentTv.setText(mEntity.getObject().getBulletin());
        } else {
            dismiss();
        }

    }

    private void initParams() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.dialog_info_remind_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 20);
        int h = UIUtil.dip2px(getContext(), 100);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        params.height = UIUtil.getScreenHeight(AppApplication.getInstance().getActivity()) - h;
        layout.setLayoutParams(params);
    }

}

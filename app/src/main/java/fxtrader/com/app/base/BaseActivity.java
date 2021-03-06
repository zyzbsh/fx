package fxtrader.com.app.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fxtrader.com.app.R;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.DefaultProgressDialog;

/**
 * activity基类
 * Created by pc on 2016/11/17.
 */
public abstract class BaseActivity extends FragmentActivity {

    private TextView mTitleContentTv;

    private DefaultProgressDialog mLoadingProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.dismissProgressDialog();
    }

    protected void setContentLayout(int layoutRes) {
        setContentView(layoutRes);
        initTitleLayout();
    }

    protected void initTitleLayout() {
        View view = findViewById(R.id.title_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        mTitleContentTv = (TextView) findViewById(R.id.title_content_tv);
    }

    protected void setBackListener(View.OnClickListener listener) {
        View view = findViewById(R.id.title_back);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    protected void setTitleContent(String content) {
        if (mTitleContentTv != null) {
            mTitleContentTv.setText(content);
        }
    }

    protected void setTitleContent(int res) {
        if (mTitleContentTv != null) {
            mTitleContentTv.setText(res);
        }
    }

    protected void openActivity(Class<?> clazz) {
        UIUtil.openActivity(this, clazz);
    }

    protected void showToastLong(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    protected void showToastShort(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }

    protected void showProgressDialog(){
        if (mLoadingProgress == null) {
            mLoadingProgress = new DefaultProgressDialog(this);
        }
        mLoadingProgress.show();
    }

    protected void dismissProgressDialog(){
        if (mLoadingProgress != null && mLoadingProgress.isShowing()){
            mLoadingProgress.dismiss();
        }
    }

    protected int getCompactColor(int resId) {
        return getResources().getColor(resId);
    }

    protected boolean isLogin(){
        return LoginConfig.getInstance().isLogin();
    }

}


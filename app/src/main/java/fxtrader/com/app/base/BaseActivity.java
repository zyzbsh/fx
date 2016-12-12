package fxtrader.com.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.tools.UIUtil;

/**
 * activity基类
 * Created by pc on 2016/11/17.
 */
public abstract class BaseActivity extends FragmentActivity {

    private TextView mTitleContentTv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
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



}


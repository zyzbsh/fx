package fxtrader.com.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.UserRecordConfig;

/**
 * Created by pc on 2016/12/25.
 */
public class StartActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserRecordConfig.getInstance().isGuided()) {
                    openActivity(MainActivity.class);
                } else {
                    openActivity(GuideActivity.class);
                }
                finish();
            }
        }, 2000);
    }
}

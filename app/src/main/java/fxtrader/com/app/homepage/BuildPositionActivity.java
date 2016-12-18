package fxtrader.com.app.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * Created by pc on 2016/12/18.
 */
public class BuildPositionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.dialog_build);
    }
}

package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * Created by pc on 2017/2/19.
 * http://blog.csdn.net/gao36951/article/details/41149049
 * http://blog.csdn.net/zhangphil/article/details/44917901
 * http://blog.csdn.net/coder_yao/article/details/52230289
 * http://blog.csdn.net/u012702547/article/details/51501350
 */
public class QRCodeWeiXinActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_qr_code_wx);
    }
}

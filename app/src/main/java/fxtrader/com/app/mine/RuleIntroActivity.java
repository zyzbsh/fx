package fxtrader.com.app.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.tools.UIUtil;

/**
 * Created by pc on 2016/12/28.
 */
public class RuleIntroActivity extends BaseActivity{

    private static final String URL = HttpConstant.BASE_URL + "img/rules.png";

    private ImageView mRuleIntroIm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_trade_rule_intro);
        mRuleIntroIm = (ImageView) findViewById(R.id.rules_intro_im);
        Glide.with(this).load(URL).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        ViewGroup.LayoutParams lp = mRuleIntroIm.getLayoutParams();
                        lp.width = UIUtil.getScreenWidth(RuleIntroActivity.this);
                        float tempHeight=height * ((float)lp.width / width);
                        lp.height =(int)tempHeight ;
                        mRuleIntroIm.setLayoutParams(lp);
                        mRuleIntroIm.setImageBitmap(bitmap);
                    }
                });
    }
}

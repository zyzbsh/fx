package fxtrader.com.app.view.ctr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.AdEntity;
import fxtrader.com.app.homepage.WebVideoActivity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页广告ViewPager 控制类
 *
 * @author zhangyuzhu
 */
public class BannerHpController {

    private static final int DEFAULT_PAGE_COUNT = 1;

    private Activity mActivity;

    private ViewPager mViewPager;

    private LinearLayout mTipLayout;

    private ImageView[] mTipImgs;// 提示性点点数组

    private int mCurPage = 0;// 当前展示的页码

    public BannerHpController(Activity activity, View bannerView) {
        mActivity = activity;
        mViewPager = (ViewPager) bannerView.findViewById(R.id.banner_view_pager);
        mTipLayout = (LinearLayout) bannerView.findViewById(R.id.banner_tip_layout);
    }

    public void init() {
        addTips(DEFAULT_PAGE_COUNT);
        initViewPager();
        requestAd();
    }

    private void addTips(int num) {
        if (num <= DEFAULT_PAGE_COUNT) {
            return;
        }
        mTipImgs = new ImageView[num];
        mTipLayout.removeAllViews();
        for (int i = 0; i < mTipImgs.length; i++) {
            ImageView img = getTipIm(i);
            mTipImgs[i] = img;
            mTipLayout.addView(img, getTipLayoutParams()); // 把点点添加到容器中
        }
    }

    private ImageView getTipIm(int position) {
        ImageView img = new ImageView(mActivity);
        img.setLayoutParams(new LayoutParams(10, 10));
        if (position == 0) {
            img.setBackgroundResource(R.drawable.shape_full_holo);
        } else {
            img.setBackgroundResource(R.drawable.shape_empty_holo);
        }
        return img;
    }

    private LayoutParams getTipLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        params.leftMargin = 5;
        params.rightMargin = 5;
        return params;
    }

    private void initViewPager() {
        List<AdEntity.ObjectBean> list = new ArrayList<AdEntity.ObjectBean>();
        list.add(new AdEntity.ObjectBean());
        PagerAdapter adapter = new MyPageAdapter(list);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void setViewPager(List<AdEntity.ObjectBean> list) {
        if (list != null && list.size() > 0) {
            addTips(list.size());
            PagerAdapter adapter = new MyPageAdapter(list);
            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        }
    }

    private class MyPageAdapter extends PagerAdapter {

        private List<AdEntity.ObjectBean> mList;

        public MyPageAdapter(List<AdEntity.ObjectBean> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object o) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final AdEntity.ObjectBean entity = mList.get(position);
            final ImageView im = new ImageView(mActivity);
            im.setScaleType(ScaleType.FIT_XY);
            final String url = entity.getBackgroundImageUrl();
            if (TextUtils.isEmpty(url)) {
                im.setImageResource(R.mipmap.bg_homepage_banner);
            } else {
                Glide.with(mActivity).load(url).asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                LayoutParams lp = im.getLayoutParams();
                                lp.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity());
                                float tempHeight = height * ((float) lp.width / width);
                                lp.height = (int) tempHeight;
                                im.setLayoutParams(lp);
                                im.setImageBitmap(bitmap);
                            }
                        });
                im.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String videoUrl = entity.getContentUrl();
                        LogZ.i("videoUrl = " + videoUrl);
                        if (!TextUtils.isEmpty(videoUrl)) {
                            Intent intent = new Intent(mActivity, WebVideoActivity.class);
                            intent.putExtra(IntentItem.VIDEO_URL, videoUrl);
                            mActivity.startActivity(intent);
                        }
                    }
                });
            }
            container.addView(im);
            return im;
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mTipImgs == null || mTipImgs.length <= DEFAULT_PAGE_COUNT) {
                return;
            }
            mTipImgs[mCurPage].setBackgroundResource(R.drawable.shape_empty_holo);
            mCurPage = position;
            mTipImgs[position].setBackgroundResource(R.drawable.shape_full_holo);
        }

    }


    private void requestAd() {
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<AdEntity> request = communityApi.banner(getAdParams());
        request.enqueue(new Callback<AdEntity>() {
            @Override
            public void onResponse(Call<AdEntity> call, Response<AdEntity> response) {
                AdEntity entity = response.body();
                if (entity.isSuccess() && entity.getObject() != null && !entity.getObject().isEmpty()) {
                    setViewPager(entity.getObject());
                }
            }

            @Override
            public void onFailure(Call<AdEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getAdParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.banner");
        params.put("organ_id", "-4");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}

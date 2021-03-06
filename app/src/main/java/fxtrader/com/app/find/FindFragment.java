package fxtrader.com.app.find;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.AdEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.ctr.BannerController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 发现
 * Created by zhangyuzhu on 2016/12/1.
 */
public class FindFragment extends BaseFragment implements View.OnClickListener, HeadListener{

    private BannerController mBannerController;

    private ImageView mAdIm;

    private MasterHotFragment mMasterHotFragment;

    private YesterdayRankingFragment mProfitFragment;

    private RedEnvelopeFragment mRedEnvelopeFragment;

    private enum TabIndex {
        INDEX_WIN_STREAM, INDEX_PROFIT, INDEX_RED_ENVELOPE
    }

    private View[] mTabBgViews = new View[2];

    private RadioButton[] mTabBtns = new RadioButton[2];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBannerView(view);
        initAdIm(view);
        initTabLayout(view);
    }

    private void initBannerView(View view) {
        View bannerView = view.findViewById(R.id.banner_layout);
        mBannerController = new BannerController(getActivity(), bannerView);
        mBannerController.init();
    }

    private boolean mMasterListState = false;
    public void setMasterListState(){
        mMasterListState = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mMasterListState) {
                mMasterListState = false;
                if (mTabBtns[0].isChecked()) {
                    mMasterHotFragment.refresh();
                } else {
                    mMasterView.performClick();
                }
            }
        }
    }

    @Override
    public void show() {
        if (mAdIm != null) {
//            ObjectAnimator animator = ObjectAnimator.ofFloat(mAdIm, View.TRANSLATION_Y, -mAdImHeight, 0);
//            animator.setDuration(500);
//            animator.start();
            mAdIm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hide() {
        if (mAdIm != null) {
//            ObjectAnimator animator = ObjectAnimator.ofFloat(mAdIm, View.TRANSLATION_Y, 0, -mAdImHeight);
//            animator.setDuration(500);
//            animator.start();
            mAdIm.setVisibility(View.GONE);
        }
    }

    private int mAdImHeight;
    private void initAdIm(View view) {
        mAdIm = (ImageView) view.findViewById(R.id.find_ad_im);
        int width = UIUtil.getScreenWidth(getActivity());
        int height = width * 257 / 640;
        ViewGroup.LayoutParams params = mAdIm.getLayoutParams();
        params.width = width;
        params.height = height;
        mAdImHeight = height;
        mAdIm.setLayoutParams(params);
    }

    private View mMasterView;

    private void initTabLayout(View view) {
        mTabBgViews[TabIndex.INDEX_WIN_STREAM.ordinal()] = view.findViewById(R.id.find_tab_bg_win_stream);
        mTabBgViews[TabIndex.INDEX_PROFIT.ordinal()] = view.findViewById(R.id.find_tab_bg_profit);
//        mTabBgViews[TabIndex.INDEX_RED_ENVELOPE.ordinal()] = view.findViewById(R.id.find_tab_bg_red_envelope);

        mTabBtns[TabIndex.INDEX_WIN_STREAM.ordinal()] = (RadioButton) view.findViewById(R.id.find_tab_win_stream_btn);
        mTabBtns[TabIndex.INDEX_PROFIT.ordinal()] = (RadioButton) view.findViewById(R.id.find_tab_profit_btn);
//        mTabBtns[TabIndex.INDEX_RED_ENVELOPE.ordinal()] = (RadioButton) view.findViewById(R.id.find_tab_red_envelope_btn);

        TabOnClickListener listener = new TabOnClickListener();
        mMasterView = view.findViewById(R.id.find_tab_win_stream_layout);
        mMasterView.setOnClickListener(listener);
        view.findViewById(R.id.find_tab_profit_layout).setOnClickListener(listener);
        view.findViewById(R.id.find_tab_red_envelope_layout).setOnClickListener(listener);

//        mTabBgViews[TabIndex.INDEX_WIN_STREAM.ordinal()].setVisibility(View.VISIBLE);
//        mTabBtns[TabIndex.INDEX_WIN_STREAM.ordinal()].setChecked(true);
        mMasterView.performClick();
    }

    private class TabOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int index = 0;
            switch (v.getId()) {
                case R.id.find_tab_win_stream_layout:
                    index = TabIndex.INDEX_WIN_STREAM.ordinal();
                    break;
                case R.id.find_tab_profit_layout:
                    index = TabIndex.INDEX_PROFIT.ordinal();
                    break;
                case R.id.find_tab_red_envelope_layout:
                    index = TabIndex.INDEX_RED_ENVELOPE.ordinal();
                    break;
                default:
                    break;
            }
            Log.i("zyu", index + "");
            checkTabBg(index);
            onFragmentChange(index);
        }
    }


    private void checkTabBg(int index) {
        for (int i = 0; i < mTabBgViews.length; i++) {
            if (index == i) {
                mTabBgViews[i].setVisibility(View.VISIBLE);
                mTabBtns[i].setChecked(true);
            } else {
                mTabBgViews[i].setVisibility(View.INVISIBLE);
                mTabBtns[i].setChecked(false);
            }
        }
    }

    private void onFragmentChange(int index) {
       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (index == TabIndex.INDEX_WIN_STREAM.ordinal()) {
            if (mMasterHotFragment == null) {
                mMasterHotFragment = new MasterHotFragment();
                transaction.add(R.id.find_content_layout, mMasterHotFragment);
            } else {
                transaction.show(mMasterHotFragment);
            }
        } else if (index == TabIndex.INDEX_PROFIT.ordinal()) {
            if (mProfitFragment == null) {
                mProfitFragment = new YesterdayRankingFragment();
                transaction.add(R.id.find_content_layout, mProfitFragment);
            } else {
                transaction.show(mProfitFragment);
            }
        } else if (index == TabIndex.INDEX_RED_ENVELOPE.ordinal()) {
            if (mRedEnvelopeFragment == null) {
                mRedEnvelopeFragment = new RedEnvelopeFragment();
                mRedEnvelopeFragment.addHeadListener(this);
                transaction.add(R.id.find_content_layout, mRedEnvelopeFragment);
            } else {
                transaction.show(mRedEnvelopeFragment);
            }
        }
        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mMasterHotFragment != null) fragmentTransaction.hide(mMasterHotFragment);
        if (mProfitFragment != null) fragmentTransaction.hide(mProfitFragment);
        if (mRedEnvelopeFragment != null) fragmentTransaction.hide(mRedEnvelopeFragment);
    }


    @Override
    public void onClick(View v) {

    }


}

package fxtrader.com.app.homepage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.config.SignConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.MasterEntity;
import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.ParticipantsEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.entity.RankResponse;
import fxtrader.com.app.entity.SystemBulletinEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.http.manager.MasterListManager;
import fxtrader.com.app.http.manager.ResponseListener;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.login.LoginNewActivity;
import fxtrader.com.app.mine.RechargeActivity;
import fxtrader.com.app.mine.WithdrawActivity;
import fxtrader.com.app.service.PositionService;
import fxtrader.com.app.service.PriceService;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.ProfitListPop;
import fxtrader.com.app.view.SignSuccessDialog;
import fxtrader.com.app.view.SystemBulletinDialog;
import fxtrader.com.app.view.ctr.BannerController;
import fxtrader.com.app.view.ctr.BannerHpController;
import fxtrader.com.app.view.ctr.MainTitleProfitCtr;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页
 * Created by zhangyuzhu on 2016/12/1.
 */
public class HomepageFragment extends BaseFragment implements View.OnClickListener {

    private MainTitleProfitCtr mTitleProfitCtr;

    private ProfitListPop mProfitListPop;

    private TextView mLoginTv;

    private LinearLayout mCoinsLayout;

    private TextView mCoinsNumTv;

    private TextView mExpectRaiseDesTv;

    private TextView mExpectFallDesTv;

    private TextView mRankTv;

    private ViewPager mViewPager;

    private final static int[] LINE_ID = {R.id.btn_timeline, R.id.btn_kline5,
            R.id.btn_kline30, R.id.btn_kline60, R.id.btn_day,};

    private final static int[] LINE_BG_ID = {R.id.bg_btn_timeline, R.id.bg_btn_kline5,
            R.id.bg_btn_kline15, R.id.bg_btn_kline30, R.id.bg_btn_kline60};

    private Button[] btnLineType = new Button[LINE_ID.length];

    private View[] btnLineBg = new View[LINE_BG_ID.length];

    private DataLineFragment mCurDataLineFragment;

    private boolean mExpect;

    private List<ContractInfoEntity> mContractList;

    private ContractEntity mCurrentContract;

    private String mLatestPrice = "";

    private ImageButton mSignBtn;

    private BannerHpController mBannerController;

    private LinearLayout mRankLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getContactList();
        if (isLogin()) {
            mLoginTv.setVisibility(View.GONE);
            mCoinsLayout.setVisibility(View.VISIBLE);
            mRankLayout.setVisibility(View.VISIBLE);
            startPositionListService();
            registerPositionListResevier();
            startPositionListService();
            startUserTimer();
            requestProfitRank();
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mCoinsLayout.setVisibility(View.GONE);
            mRankLayout.setVisibility(View.GONE);
            mRankTv.setText("--");
        }
        registerNetworkChangeReceiver();
        registerLoginReceiver();
        requestParticipant();
        requestSystemBulletin();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mPriceReceiver != null) {
                getActivity().unregisterReceiver(mPriceReceiver);
            }
            if (mLoginReceiver != null) {
                getActivity().unregisterReceiver(mLoginReceiver);
            }
            if (mPositionListReceiver != null) {
                getActivity().unregisterReceiver(mPositionListReceiver);
            }
            if (mNetworkChangeReceiver != null) {
                getActivity().unregisterReceiver(mNetworkChangeReceiver);
            }
        } catch (Exception e) {
            LogZ.e(e.getMessage());
        } finally {
            getActivity().stopService(new Intent(getActivity(), PriceService.class));
            getActivity().stopService(new Intent(getActivity(), PositionService.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogZ.i("requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == IntentItem.REQUEST_LOGIN) {
            LogZ.i("request_login_onActivityResult");
            logined();
        } else if (requestCode == IntentItem.REQUEST_LOGIN_FROM_BUILD_POSITION) {
            logined();
            openBuildPositionActivity(mExpect);
        } else if (requestCode == IntentItem.REQUEST_BUILD_POSITION) {
            LogZ.i("REQUEST_BUILD_POSITION_onActivityResult");
            startPositionListService();
        } else if (requestCode == IntentItem.REQUEST_RECHARGE) {
            LogZ.i("REQUEST_RECHARGE_onActivityResult");
            logined();
            openRechargeActivity();
        } else if (requestCode == IntentItem.REQUEST_WITHDRAW) {
            LogZ.i("REQUEST_WITHDRAW_onActivityResult");
            logined();
            openWithdrawActivity();
        } else if (requestCode == IntentItem.REQUEST_SIGN) {
            logined();
            sign();
        }
        setLoginView();
    }

    private void logined() {
        startUserTimer();
        startPositionListService();
        requestProfitRank();
    }

    public void logOut() {
        getActivity().stopService(new Intent(getActivity(), PositionService.class));
        mIsLogOut = true;
        stopUserTimer();
        mLoginTv.setVisibility(View.VISIBLE);
        mCoinsLayout.setVisibility(View.GONE);
        mRankLayout.setVisibility(View.GONE);
        mRankTv.setText("--");
    }

    private void setLoginView() {
        LogZ.i("setLoginView");
        if (isLogin()) {
            mLoginTv.setVisibility(View.GONE);
            mCoinsLayout.setVisibility(View.VISIBLE);
            mRankLayout.setVisibility(View.VISIBLE);
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mCoinsLayout.setVisibility(View.GONE);
            mRankLayout.setVisibility(View.GONE);
            mRankTv.setText("--");
        }
    }

    private void initViews(View view) {
        initTitleProfitLayout(view);
        initSystemBulletinLayout(view);
        initAccountInfoLayout(view);
        initExpectLayout(view);
        initViewPager(view);
        initLineBtn(view);
        initBannerView(view);
    }

    private void initTitleProfitLayout(View view) {
        final View v = view.findViewById(R.id.homepage_title_profit_layout);
        mTitleProfitCtr = new MainTitleProfitCtr(v);
        mTitleProfitCtr.setProfitListListener(new MainTitleProfitCtr.ProfitListListener() {
            @Override
            public void showList() {
                if (mProfitListPop == null) {
                    mProfitListPop = new ProfitListPop(getActivity());
                }
                mProfitListPop.show(v);
            }
        });
    }

    private void initAccountInfoLayout(View view) {
        mLoginTv = (TextView) view.findViewById(R.id.homepage_login_tv);
        mLoginTv.setOnClickListener(this);
        mCoinsLayout = (LinearLayout) view.findViewById(R.id.homepage_coins_layout);
        mCoinsNumTv = (TextView) view.findViewById(R.id.homepage_coins_num_tv);
        mRankLayout = (LinearLayout) view.findViewById(R.id.homepage_rank_layout);
        mRankTv = (TextView) view.findViewById(R.id.homepage_rank_num_tv);
        mSignBtn = (ImageButton) view.findViewById(R.id.homepage_sign_btn);
        mSignBtn.setOnClickListener(this);
        if (SignConfig.getInstance().isSigned()){
            mSignBtn.setVisibility(View.GONE);
        } else {
            mSignBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initSystemBulletinLayout(View view) {
//        mSystemBulletinContentTv = (TextView) view.findViewById(R.id.homepage_sys_announcement);
//        mSystemBulletinContentTv.setText("系统公告：今日已有30374人参与交易");
    }

    private void initExpectLayout(View view) {
        mExpectRaiseDesTv = (TextView) view.findViewById(R.id.homepage_expect_raise_des_tv);
        mExpectFallDesTv = (TextView) view.findViewById(R.id.homepage_expect_fall_des_tv);
        view.findViewById(R.id.homepage_expect_raise_layout).setOnClickListener(this);
        view.findViewById(R.id.homepage_expect_fall_layout).setOnClickListener(this);
    }

    private void initViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.homepage_view_pager);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        int width = UIUtil.getScreenWidth(getActivity()) - UIUtil.dip2px(getActivity(), 26) * 2;
        int height = width * 4 / 5 + UIUtil.dip2px(getActivity(), 55);
        params.width = width;
        params.height = height;
        mViewPager.setLayoutParams(params);
        List<Fragment> fragments = new ArrayList<>();
        FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
    }

    private void initLineBtn(View view) {
        for (int i = 0; i < LINE_ID.length; ++i) {
            btnLineType[i] = (Button) view.findViewById(LINE_ID[i]);
            btnLineType[i].setOnClickListener(this);
        }
        for (int i = 0; i < LINE_BG_ID.length; ++i) {
            btnLineBg[i] = view.findViewById(LINE_BG_ID[i]);
        }

        btnLineType[0].performClick();
        btnLineBg[0].setVisibility(View.VISIBLE);
    }

    private void initBannerView(View view) {
        View bannerView = view.findViewById(R.id.banner_layout);
        mBannerController = new BannerHpController(getActivity(), bannerView);
        mBannerController.init();
    }

    /**
     * 获取合约列表
     */
    private void getContactList() {
        ContractApi contractApi = RetrofitUtils.createApi(ContractApi.class);
        Call<ContractListEntity> repos = contractApi.list(getContractListParams());
        repos.enqueue(new Callback<ContractListEntity>() {
            @Override
            public void onResponse(Call<ContractListEntity> call, Response<ContractListEntity> response) {
                ContractListEntity entity = response.body();
                mContractList = entity.getObject();
                if (mContractList == null || mContractList.isEmpty()) {
                    LogZ.i("合约列表没有数据。");
                    return;
                }
                ContractUtil.clear();

                for (ContractInfoEntity info : mContractList) {
                    if (info.getQueryParam().equals("YDHF") || info.getQueryParam().equals("YDCL")) {
                        continue;
                    }
                    ContractUtil.addContract(info);
                }
                List<Fragment> fragments = new ArrayList<>();
                for (String type : ContractUtil.getContractMap().keySet()) {
                    ContractEntity contract = ContractUtil.getContractMap().get(type);
                    DataLineFragment fragment = new DataLineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentItem.CONTRACT_NAME, contract.getName());
                    bundle.putString(IntentItem.CONTRACT_TYPE, contract.getType());
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                if (getActivity() != null) {
                    FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
                    mViewPager.setAdapter(adapter);
                    mViewPager.setOffscreenPageLimit(mContractList.size() - 1);
                }
                registerPriceReceiver();
                getActivity().startService(new Intent(getActivity(), PriceService.class));
            }

            @Override
            public void onFailure(Call<ContractListEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getContractListParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.contract.list");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void openBuildPositionActivity(boolean up) {
        if (mCurDataLineFragment == null) {
            return;
        }
        String dataType = mCurDataLineFragment.getDataType();
        Intent intent;
//        if (dataType.equals(HttpConstant.PriceCode.YDHF)) {
//            intent = new Intent(getActivity(), HFBuildPositionActivity.class);
//        } else {
        intent = new Intent(getActivity(), BuildPositionActivity.class);
//        }
        ContractEntity entity = ContractUtil.getContractMap().get(dataType);
        intent.putExtra(IntentItem.PRICE, mLatestPrice);
        intent.putExtra(IntentItem.CONTARCT_INFO, entity);
        intent.putExtra(IntentItem.EXCEPTION, up);
        intent.putExtra(IntentItem.DATA_TYPE, mCurDataLineFragment.getDataType());
        startActivityForResult(intent, IntentItem.REQUEST_BUILD_POSITION);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_login_tv:
                openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_LOGIN);
                break;
            case R.id.homepage_expect_raise_layout://看涨
                expect(true);
                break;
            case R.id.homepage_expect_fall_layout://看跌
                expect(false);
                break;
            case R.id.btn_timeline:
            case R.id.btn_kline5:
            case R.id.btn_day:
            case R.id.btn_kline30:
            case R.id.btn_kline60:
                dataLineBtnOnClicked(v);
                break;
            case R.id.homepage_sign_btn://签到
                if (isLogin()) {
                    sign();
                } else {
                    openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_SIGN);
                }
                break;
            default:
                break;
        }
    }

    private void recharge() {
        if (isLogin()) {
            openRechargeActivity();
        } else {
            openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_RECHARGE);
        }
    }

    private void openRechargeActivity() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        intent.putExtra(IntentItem.USER_INFO, mUserEntity);
        startActivity(intent);
    }

    private void withdraw() {
        if (isLogin()) {
            openWithdrawActivity();
        } else {
            openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_WITHDRAW);
        }
    }

    private void openWithdrawActivity() {
        Intent intent = new Intent(getActivity(), WithdrawActivity.class);
        intent.putExtra(IntentItem.USER_INFO, mUserEntity);
        startActivity(intent);
    }

    private void expect(boolean raise) {
        mExpect = raise;
        if (isLogin()) {
            openBuildPositionActivity(raise);
        } else {
            openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_LOGIN_FROM_BUILD_POSITION);
        }
    }

    private final static int[] LINE_TYPE = {HttpConstant.KType.MIN_1, HttpConstant.KType.MIN_5, HttpConstant.KType.MIN_30, HttpConstant.KType.HOUR_1, HttpConstant.KType.DAY};

    private void dataLineBtnOnClicked(View v) {
        for (int i = 0; i < LINE_ID.length; ++i) {
            if (LINE_ID[i] == v.getId()) {
                if (!btnLineType[i].isSelected()) {
                    btnLineType[i].setSelected(true);
                    btnLineBg[i].setVisibility(View.VISIBLE);
                    if (mCurDataLineFragment != null) {
                        mCurDataLineFragment.setLineType(LINE_TYPE[i]);
                    }
                }
            } else {
                btnLineType[i].setSelected(false);
                btnLineBg[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isLogin() {
        return LoginConfig.getInstance().isLogin();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mCurDataLineFragment != null) {
            if (hidden) {
            }
        }

    }

    class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurDataLineFragment = (DataLineFragment) object;
        }
    }

    private BroadcastReceiver mPriceReceiver;

    private void registerPriceReceiver() {
        mPriceReceiver = new PriceReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_PRICE);
        getActivity().registerReceiver(mPriceReceiver, filter);
    }

    private MarketEntity mMarketentity;

    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity vo = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            if (vo != null) {
                vo.init();
                mMarketentity = vo;
                AppApplication.getInstance().setMarketEntity(vo);
                if (mCurDataLineFragment != null) {
                    String dataType = mCurDataLineFragment.getDataType();
                    String data = vo.getData(dataType);
                    PriceEntity price = new PriceEntity(data);
                    mLatestPrice = price.getLatestPrice();
                    mCurDataLineFragment.setPriceTvs(getActivity(), price);

                }

            }
        }
    }

    private Timer positionTimer = null;
    private TimerTask positionTimerTask = null;

    private void startPositionListService() {
        getActivity().startService(new Intent(getActivity(), PositionService.class));
//        if (null != positionTimer || null != positionTimerTask) {
//            stopPositionTimer();
//        }
//        positionTimer = new Timer();
//        positionTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                getPositionList();
//            }
//        };
//        positionTimer.schedule(positionTimerTask, 0, HttpConstant.REFRESH_POSITION_LIST);
    }

    private BroadcastReceiver mPositionListReceiver;

    private void registerPositionListResevier() {
        mPositionListReceiver = new PositionListReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_POSITION_LIST);
        getActivity().registerReceiver(mPositionListReceiver, filter);
    }

    class PositionListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPositionInfoList = (List<PositionInfoEntity>) intent.getSerializableExtra(IntentItem.POSITION_LIST);
            if (mPositionInfoList != null && !mPositionInfoList.isEmpty()) {

                if (mProfitListPop == null) {
                    mProfitListPop = new ProfitListPop(getActivity());
                }
                if (mMarketentity != null) {
                    List<PositionInfoEntity> list = getPopProfitList(mMarketentity, mPositionInfoList);
                    mTitleProfitCtr.show();
                    if (mProfitListPop != null) {
                        mProfitListPop.addData(list, mPositionInfoList.size());
                    }
                }
            } else {
                mTitleProfitCtr.hide();
            }
        }
    }


    private void stopPositionTimer() {
        if (null != positionTimer) {
            positionTimer.cancel();
            positionTimer = null;
        }
        if (null != positionTimerTask) {
            positionTimerTask.cancel();
            positionTimerTask = null;
        }
    }

    private List<PositionInfoEntity> mPositionInfoList;

    private void getPositionList() {
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        String token = ParamsUtil.getToken();
        Call<PositionListEntity> respon = dataApi.positionList(token, getPositionListParams());
        respon.enqueue(new Callback<PositionListEntity>() {
            @Override
            public void onResponse(Call<PositionListEntity> call, Response<PositionListEntity> response) {
                PositionListEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    mPositionInfoList = entity.getObject().getContent();
                    if (mPositionInfoList != null && !mPositionInfoList.isEmpty()) {
                        mTitleProfitCtr.show();
                        if (mProfitListPop == null) {
                            mProfitListPop = new ProfitListPop(getActivity());
                        }
                    } else {
                        mTitleProfitCtr.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<PositionListEntity> call, Throwable t) {
                LogZ.i(t.toString());
            }
        });
    }

    private List<PositionInfoEntity> getPopProfitList(MarketEntity vo, List<PositionInfoEntity> data) {
        if (vo == null || data == null) {
            return null;
        }
        double sum = ContractUtil.initProfitInfoList(vo, data);
        mTitleProfitCtr.setProfit(String.valueOf(sum));

        int size = data.size();
        List<PositionInfoEntity> list = new ArrayList<>();
        int count = size <= ProfitListPop.MAX_SHOW_COUNT ? size : ProfitListPop.MAX_SHOW_COUNT;
        for (int i = 0; i < count; i++) {
            PositionInfoEntity positionInfoEntity = data.get(i);
            list.add(positionInfoEntity);
        }
        LogZ.i(list.toString());
        return list;
    }


    private Map<String, String> getPositionListParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.list");
        params.put("sale", "false");
        params.put("page", "0");
        params.put("size", String.valueOf(Integer.MAX_VALUE));
        params.put("sort", "buyingDate,DESC");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private Timer userTimer = null;
    private TimerTask userTimerTask = null;

    private void startUserTimer() {
        if (null != userTimer || null != userTimerTask) {
            stopUserTimer();
        }
        mIsLogOut = false;
        userTimer = new Timer();
        userTimerTask = new TimerTask() {
            @Override
            public void run() {
                getUserInfo();
            }
        };
        userTimer.schedule(userTimerTask, 0, HttpConstant.REFRESH_USER_INFO);
    }

    private void stopUserTimer() {
        if (null != userTimer) {
            userTimer.cancel();
            userTimer = null;
        }
        if (null != userTimerTask) {
            userTimerTask.cancel();
            userTimerTask = null;
        }
    }

    private UserEntity mUserEntity;

    private boolean mIsLogOut = false;

    private void getUserInfo() {
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUserEntity = user;
                if (user != null && user.getObject() != null && !mIsLogOut) {
                    mCoinsLayout.setVisibility(View.VISIBLE);
                    mRankLayout.setVisibility(View.VISIBLE);
                    mCoinsNumTv.setText(String.valueOf(user.getObject().getFunds()));
                }
            }

            @Override
            public void onError(String msg) {
            }

            @Override
            public void onHttpFailure() {

            }
        });
    }

    private BroadcastReceiver mLoginReceiver;

    private void registerLoginReceiver() {
        mLoginReceiver = new mLoginReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_LOGIN);
        getActivity().registerReceiver(mLoginReceiver, filter);
    }


    class mLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogZ.i("login receiver onReceive");
            registerPositionListResevier();
            getActivity().startService(new Intent(getActivity(), PositionService.class));
            startUserTimer();
            startPositionListService();
            setLoginView();
            requestProfitRank();
        }
    }

    private void requestParticipant() {
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        final Call<ParticipantsEntity> respon = dataApi.participants(getParticipantParams());
        respon.enqueue(new Callback<ParticipantsEntity>() {
            @Override
            public void onResponse(Call<ParticipantsEntity> call, Response<ParticipantsEntity> response) {
                ParticipantsEntity entity = response.body();
                if (entity != null && entity.isSuccess()) {
                    if (entity.getObject() != null) {
                        mExpectRaiseDesTv.setText(getActivity().getString(R.string.expect_raise_num, entity.getObject().getUp()));
                        mExpectFallDesTv.setText(getActivity().getString(R.string.expect_fall_num, entity.getObject().getDrop()));
                    }
                }
//                mExpectRaiseDesTv
            }

            @Override
            public void onFailure(Call<ParticipantsEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getParticipantParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getRiseOrFall");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private SystemBulletinDialog mSystemBulletinDialog;

    private void requestSystemBulletin() {
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<SystemBulletinEntity> request = communityApi.systembulletin(getSystemBulletinParams());
        request.enqueue(new Callback<SystemBulletinEntity>() {
            @Override
            public void onResponse(Call<SystemBulletinEntity> call, Response<SystemBulletinEntity> response) {
                SystemBulletinEntity entity = response.body();
                if (entity != null && entity.isSuccess()) {
                    Context context = getContext();
                    if (context == null) {
                        context = AppApplication.getInstance().getApplicationContext();
                    }
                    if (mSystemBulletinDialog != null && mSystemBulletinDialog.isShowing()) {
                        mSystemBulletinDialog.dismiss();
                    }
                    mSystemBulletinDialog = new SystemBulletinDialog(context, entity);
                    mSystemBulletinDialog.show();
                }
            }

            @Override
            public void onFailure(Call<SystemBulletinEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getSystemBulletinParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getSystemBulletin");
        params.put("organ_id", LoginConfig.getInstance().getOrganId() + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void requestProfitRank() {
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<RankResponse> request = communityApi.profitRank(token, getProfitRankParams());
        request.enqueue(new Callback<RankResponse>() {
            @Override
            public void onResponse(Call<RankResponse> call, Response<RankResponse> response) {
                RankResponse rankResponse = response.body();
                if (rankResponse != null && rankResponse.getObject() != null) {
                    mRankTv.setText(rankResponse.getObject().getRankNo() + "");
                }
            }

            @Override
            public void onFailure(Call<RankResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getProfitRankParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.rankNo");
        params.put("organ_id", LoginConfig.getInstance().getOrganId() + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void sign() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> request = userApi.sign(token, getSignParams());
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if (commonResponse != null) {
                    if (commonResponse.isSuccess()) {
                        showSignSuccessDialog();
                    } else {
                        Toast.makeText(getContext(), commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    mSignBtn.setVisibility(View.GONE);
                    SignConfig.getInstance().save();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private void showSignSuccessDialog(){
        SignSuccessDialog dialog = new SignSuccessDialog(getContext());
        dialog.show();
    }


    private Map<String, String> getSignParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.attendance");
        params.put("organ_id", LoginConfig.getInstance().getOrganId() + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private MasterMoreListener mMasterMoreListener;

    public void setMasterMoreListener(MasterMoreListener listener) {
        mMasterMoreListener = listener;
    }

    public interface MasterMoreListener {
        public void more();
    }

    private BroadcastReceiver mNetworkChangeReceiver;

    private void registerNetworkChangeReceiver() {
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_NETWORK_CHANGE);
        getActivity().registerReceiver(mLoginReceiver, filter);
    }


    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogZ.i("NetworkChangeReceiver onReceive");
            boolean connected = intent.getBooleanExtra(IntentItem.NETWORK_CONNECTED, false);
            if (connected) {
                getContactList();
            }
        }
    }
}

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.ParticipantsEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.login.LoginActivity;
import fxtrader.com.app.mine.RechargeActivity;
import fxtrader.com.app.mine.WithdrawActivity;
import fxtrader.com.app.service.PriceService;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.ProfitListPop;
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

    private LinearLayout mBalanceLayout;

    private TextView mSystemBulletinContentTv;

    private RelativeLayout mAccountInfoLayout;

    private TextView mBalanceAmountTv;

    private TextView mCashCouponTv;

    private TextView mExpectRaiseDesTv;

    private TextView mExpectFallDesTv;

    private RelativeLayout mMasterLayout;

    private ImageView mMasterAvatarIm;

    private TextView mMasterNameTv;

    private TextView mMastarProfitTv;

    private ViewPager mViewPager;

    private DataLineFragment getmCurDataLineFragment;

    private final static int[] LINE_ID = {R.id.btn_timeline, R.id.btn_kline5,
            R.id.btn_kline15, R.id.btn_kline30, R.id.btn_kline60};

    private final static int[] LINE_BG_ID = {R.id.bg_btn_timeline, R.id.bg_btn_kline5,
            R.id.bg_btn_kline15, R.id.bg_btn_kline30, R.id.bg_btn_kline60};

    private Button[] btnLineType = new Button[LINE_ID.length];

    private View[] btnLineBg = new View[LINE_BG_ID.length];

    private DataLineFragment mCurDataLineFragment;

    private boolean mExpect;

    private List<ContractInfoEntity> mContractList;

    private ContractEntity mCurrentContract;

    private String mLatestPrice = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (isLogin()) {
            mLoginTv.setVisibility(View.GONE);
            mBalanceLayout.setVisibility(View.VISIBLE);
            startPositionTimer();
            startUserTimer();
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mBalanceLayout.setVisibility(View.GONE);
            mCashCouponTv.setText(getActivity().getString(R.string.cash_coupon_num, " -"));
        }
        registerLoginReceiver();
        requestParticipant();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPriceReceiver != null) {
            getActivity().unregisterReceiver(mPriceReceiver);
        }
        if (mLoginReceiver != null) {
            getActivity().unregisterReceiver(mLoginReceiver);
        }
        getActivity().stopService(new Intent(getActivity(), PriceService.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogZ.i("requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == IntentItem.REQUEST_LOGIN) {
            startUserTimer();
            startPositionTimer();
            openBuildPositionActivity(mExpect);
        } else if (requestCode == IntentItem.REQUEST_BUILD_POSITION) {
            startPositionTimer();
        } else if (requestCode == IntentItem.REQUEST_RECHARGE) {
            startUserTimer();
            startPositionTimer();
            openRechargeActivity();
        } else if (requestCode == IntentItem.REQUEST_WITHDRAW) {
            startUserTimer();
            startPositionTimer();
            openWithdrawActivity();
        }
        setLoginView();
    }

    private void setLoginView() {
        if (isLogin()) {
            mLoginTv.setVisibility(View.GONE);
            mBalanceLayout.setVisibility(View.VISIBLE);
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mBalanceLayout.setVisibility(View.GONE);
            mCashCouponTv.setText(getActivity().getString(R.string.cash_coupon_num, "-"));
        }
    }

    private void initViews(View view) {
        initTitleProfitLayout(view);
        initSystemBulletinLayout(view);
        initAccountInfoLayout(view);
        initExpectLayout(view);
        initMasterLayout(view);
        initViewPager(view);
        initLineBtn(view);
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
        mBalanceLayout = (LinearLayout) view.findViewById(R.id.homepage_balance_layout);
        mAccountInfoLayout = (RelativeLayout) view.findViewById(R.id.homepage_account_info_layout);
        mBalanceAmountTv = (TextView) view.findViewById(R.id.homepage_balance_amount_tv);
        mCashCouponTv = (TextView) view.findViewById(R.id.homepage_cash_coupon_tv);
        view.findViewById(R.id.homepage_recharge_tv).setOnClickListener(this);
        view.findViewById(R.id.homepage_withdraw_tv).setOnClickListener(this);
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

    private void initMasterLayout(View view) {
        mMasterLayout = (RelativeLayout) view.findViewById(R.id.homepage_master_layout);
        mMasterAvatarIm = (ImageView) view.findViewById(R.id.homepage_master_avatar_im);
        mMasterNameTv = (TextView) view.findViewById(R.id.homepage_master_name_tv);
        mMastarProfitTv = (TextView) view.findViewById(R.id.homepage_master_profit_tv);
        view.findViewById(R.id.homepage_master_more_tv).setOnClickListener(this);
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
        getContactList();
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


//    private LinkedHashMap<String, ContractEntity> mContractMap = new LinkedHashMap<>();
//    private Map<String, ContractInfoEntity> mContractInfoMap = new HashMap<>();

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
                    ContractUtil.addContract(info);
//                    String dataType = info.getQueryParam();
//                    if (mContractMap.containsKey(dataType)) {
//                        ContractEntity contract = mContractMap.get(dataType);
//                        contract.add(info);
//                    } else {
//                        ContractEntity contract = new ContractEntity(dataType);
//                        contract.add(info);
//                        mContractMap.put(dataType, contract);
//                    }
//                    mContractInfoMap.put(info.getCode(), info);
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
                FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
                mViewPager.setAdapter(adapter);
                mViewPager.setOffscreenPageLimit(mContractList.size() - 1);
//                startDataTimer();
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
        if (dataType.equals(HttpConstant.PriceCode.YDHF)) {
            intent = new Intent(getActivity(), HFBuildPositionActivity.class);
        } else {
            intent = new Intent(getActivity(), BuildPositionActivity.class);
        }
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
                openActivityForResult(LoginActivity.class, IntentItem.REQUEST_LOGIN);
                break;
            case R.id.homepage_recharge_tv://充值
                recharge();
                break;
            case R.id.homepage_withdraw_tv://提现
                withdraw();
                break;
            case R.id.homepage_expect_raise_layout://看涨
                expect(true);
                break;
            case R.id.homepage_expect_fall_layout://看跌
                expect(false);
                break;
            case R.id.homepage_master_more_tv://更多
                openActivity(MasterListActivity.class);
                break;
            case R.id.btn_timeline:
            case R.id.btn_kline5:
            case R.id.btn_kline15:
            case R.id.btn_kline30:
            case R.id.btn_kline60:
                dataLineBtnOnClicked(v);
                break;
            default:
                break;
        }
    }

    private void recharge() {
        if (isLogin()) {
            String account = LoginConfig.getInstance().getAccount();
            if (UserInfoHelper.getInstance().hasTelNumber(account)) {
                openRechargeActivity();
            } else {

            }

        } else {
            openActivityForResult(LoginActivity.class, IntentItem.REQUEST_RECHARGE);
        }
    }

    private void openRechargeActivity() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        intent.putExtra(IntentItem.USER_INFO, mUserEntity);
        startActivity(intent);
    }

    private void withdraw() {
        if (isLogin()) {
            String account = LoginConfig.getInstance().getAccount();
            if (UserInfoHelper.getInstance().hasTelNumber(account)) {
                openWithdrawActivity();
            } else {

            }

        } else {
            openActivityForResult(LoginActivity.class, IntentItem.REQUEST_WITHDRAW);
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
            openActivityForResult(LoginActivity.class, IntentItem.REQUEST_LOGIN);
        }
    }

    private void dataLineBtnOnClicked(View v) {
        for (int i = 0; i < LINE_ID.length; ++i) {
            if (LINE_ID[i] == v.getId()) {
                if (!btnLineType[i].isSelected()) {
                    btnLineType[i].setSelected(true);
                    btnLineBg[i].setVisibility(View.VISIBLE);
                    if (mCurDataLineFragment != null) {
                        mCurDataLineFragment.setLineType(i + 1);
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


    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity vo = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            vo.init();
            AppApplication.getInstance().setMarketEntity(vo);
            if (mCurDataLineFragment != null) {
                String dataType = mCurDataLineFragment.getDataType();
                String data = vo.getData(dataType);
                PriceEntity price = new PriceEntity(data);
                mLatestPrice = price.getLatestPrice();
                mCurDataLineFragment.setPriceTvs(getActivity(), price);

            }
            if (mTitleProfitCtr.isProfitViewShow()) {
                List<PositionInfoEntity> list = getPopProfitList(vo, mPositionInfoList);
                if (mProfitListPop != null) {
                    mProfitListPop.addData(list, mPositionInfoList.size());
                }
            }
        }
    }

    private Timer positionTimer = null;
    private TimerTask positionTimerTask = null;

    private void startPositionTimer() {
        if (null != positionTimer || null != positionTimerTask) {
            stopPositionTimer();
        }
        positionTimer = new Timer();
        positionTimerTask = new TimerTask() {
            @Override
            public void run() {
                getPositionList();
            }
        };
        positionTimer.schedule(positionTimerTask, 0, HttpConstant.REFRESH_POSITION_LIST);
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
                LogZ.i("profitView");
                PositionListEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    mPositionInfoList = entity.getObject().getContent();
                    if (mPositionInfoList != null && !mPositionInfoList.isEmpty()) {
                        LogZ.i("显示profitView");
                        mTitleProfitCtr.show();
                        if (mProfitListPop == null) {
                            mProfitListPop = new ProfitListPop(getActivity());
                        }
                    } else {
                        LogZ.i("隐藏profitView");
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

    private void getUserInfo() {
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUserEntity = user;
                if (user != null && user.getObject() != null) {
                    mAccountInfoLayout.setVisibility(View.VISIBLE);
                    mBalanceAmountTv.setText(String.valueOf(user.getObject().getFunds()));
                    String couponNum = String.valueOf(user.getObject().getCouponAmount());
                    mCashCouponTv.setText("现金券：" + couponNum);
                }
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
            startUserTimer();
            startPositionTimer();
            setLoginView();
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

}

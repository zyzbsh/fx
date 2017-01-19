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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import fxtrader.com.app.constant.IntentItem;
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
import fxtrader.com.app.entity.SystemBulletinEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
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
import fxtrader.com.app.view.SystemBulletinDialog;
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

    private RelativeLayout mAccountInfoLayout;

    private TextView mBalanceAmountTv;

    private TextView mCashCouponTv;

    private TextView mExpectRaiseDesTv;

    private TextView mExpectFallDesTv;

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

    private RecyclerView mRececlerView;

    private DataAdapter mMasterAdapter;

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
            mBalanceLayout.setVisibility(View.VISIBLE);
            startPositionListService();
            registerPositionListResevier();
            startPositionListService();
            startUserTimer();
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mBalanceLayout.setVisibility(View.GONE);
            mCashCouponTv.setText(getActivity().getString(R.string.cash_coupon_num, "--"));
        }
        registerNetworkChangeReceiver();
        registerLoginReceiver();
        requestParticipant();
        requestSystemBulletin();
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
        if (mPositionListReceiver != null) {
            getActivity().unregisterReceiver(mPositionListReceiver);
        }
        if (mNetworkChangeListener != null) {
            getActivity().unregisterReceiver(mNetworkChangeListener);
        }
        getActivity().stopService(new Intent(getActivity(), PriceService.class));
        getActivity().stopService(new Intent(getActivity(), PositionService.class));
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
            startUserTimer();
            startPositionListService();
            openBuildPositionActivity(mExpect);
        } else if (requestCode == IntentItem.REQUEST_BUILD_POSITION) {
            LogZ.i("REQUEST_BUILD_POSITION_onActivityResult");
            startPositionListService();
        } else if (requestCode == IntentItem.REQUEST_RECHARGE) {
            LogZ.i("REQUEST_RECHARGE_onActivityResult");
            startUserTimer();
            startPositionListService();
            openRechargeActivity();
        } else if (requestCode == IntentItem.REQUEST_WITHDRAW) {
            LogZ.i("REQUEST_WITHDRAW_onActivityResult");
            startUserTimer();
            startPositionListService();
            openWithdrawActivity();
        }
        setLoginView();
    }

    public void logOut(){
        getActivity().stopService(new Intent(getActivity(), PositionService.class));
        mIsLogOut = true;
        stopUserTimer();
        mLoginTv.setVisibility(View.VISIBLE);
        mBalanceLayout.setVisibility(View.GONE);
        mCashCouponTv.setText(getActivity().getString(R.string.cash_coupon_num, "--"));
    }

    private void setLoginView() {
        LogZ.i("setLoginView");
        if (isLogin()) {
            mLoginTv.setVisibility(View.GONE);
            mBalanceLayout.setVisibility(View.VISIBLE);
        } else {
            mLoginTv.setVisibility(View.VISIBLE);
            mBalanceLayout.setVisibility(View.GONE);
            mCashCouponTv.setText(getActivity().getString(R.string.cash_coupon_num, "--"));
        }
    }

    private void initViews(View view) {
        initTitleProfitLayout(view);
        initSystemBulletinLayout(view);
        initAccountInfoLayout(view);
        initExpectLayout(view);
        initViewPager(view);
        initLineBtn(view);
        initMasterList(view);
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

    private TextView mMasterListEmptyTv;
    private void initMasterList(View view){
        view.findViewById(R.id.homepage_master_more_tv).setOnClickListener(this);
        mMasterListEmptyTv = (TextView) view.findViewById(R.id.homepage_master_list_empty_tv);
        mRececlerView = (RecyclerView) view.findViewById(R.id.homepage_master_list_recycler_view);
        mRececlerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRececlerView.setNestedScrollingEnabled(false);
        mMasterAdapter = new DataAdapter(getActivity());
        mRececlerView.setAdapter(mMasterAdapter);
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
                openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_LOGIN);
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
                if (mMasterMoreListener != null) {
                    mMasterMoreListener.more();
                }
                break;
            case R.id.btn_timeline:
            case R.id.btn_kline5:
            case R.id.btn_day:
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
            openActivityForResult(LoginNewActivity.class, IntentItem.REQUEST_LOGIN);
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

    private boolean masterRequested = false;
    private MarketEntity mMarketentity;
    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity vo = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            if (vo != null) {
                vo.init();
                mMarketentity = vo;
                AppApplication.getInstance().setMarketEntity(vo);
                if (!masterRequested) {
                    masterRequested = true;
                    requestMaster();
                }
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

    private void registerPositionListResevier(){
        mPositionListReceiver = new PositionListReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_POSITION_LIST);
        getActivity().registerReceiver(mPositionListReceiver, filter);
    }

    class PositionListReceiver extends BroadcastReceiver{
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
            LogZ.i("login receiver onReceive");
            registerPositionListResevier();
            getActivity().startService(new Intent(getActivity(), PositionService.class));
            requestMaster();
            startUserTimer();
            startPositionListService();
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

    private void requestSystemBulletin(){
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<SystemBulletinEntity> request = communityApi.systembulletin(getSystemBulletinParams());
        request.enqueue(new Callback<SystemBulletinEntity>() {
            @Override
            public void onResponse(Call<SystemBulletinEntity> call, Response<SystemBulletinEntity> response) {
                SystemBulletinEntity entity = response.body();
                if (entity != null && entity.isSuccess()) {
                    SystemBulletinDialog dialog = new SystemBulletinDialog(getContext(), entity);
                    dialog.show();
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

    private void requestMaster(){
        MasterResponseListener listener = new MasterResponseListener();
        if (LoginConfig.getInstance().isLogin()) {
            String organId = LoginConfig.getInstance().getOrganId() + "";
            String id = LoginConfig.getInstance().getId();
            MasterListManager.getInstance().getMastersWithLogined(organId, id, listener);
        } else {
            MasterListManager.getInstance().getMasters(listener);
        }
    }

    class MasterResponseListener implements ResponseListener<MasterListEntity> {
        @Override
        public void success(MasterListEntity object) {
            if (object != null && object.isSuccess()) {
                if (object.getObject().isEmpty()) {
                    mRececlerView.setVisibility(View.GONE);
                    mMasterListEmptyTv.setVisibility(View.VISIBLE);
                } else {
                    mRececlerView.setVisibility(View.VISIBLE);
                    mMasterListEmptyTv.setVisibility(View.GONE);
                    int size = object.getObject().size();
                    if (size <= 4) {
                        mMasterAdapter.setDataList(object.getObject());
                    } else {
                        size = 4;
                        List<MasterEntity> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            list.add(object.getObject().get(i));
                        }
                    }
                    ViewGroup.LayoutParams mParams = mRececlerView.getLayoutParams();
                    mParams.height = UIUtil.dip2px(getActivity(), 48) * size;
                    mRececlerView.setLayoutParams(mParams);
                }
            }
        }

        @Override
        public void error(String error) {
            LogZ.e(error);
        }
    }

    class DataAdapter extends ListBaseAdapter<MasterEntity> {

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_homepage_master, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MasterEntity item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            setTagImWithPosition(viewHolder.rankTv, viewHolder.rankTv2, position);
            viewHolder.nameTv.setText(item.getNickname());
            viewHolder.profitTv.setText(ContractUtil.getDouble(item.getProfit(), 1) +"");
            if (getContext() != null) {
                Glide.with(getContext())
                        .load(item.getHeadImg())
                        .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(viewHolder.avatarIm);
            }
        }

        private void setTagImWithPosition(TextView rankTv, TextView rankTv2, int position) {
            rankTv.setText((position + 1) + "");
            if (position == 0) {
                rankTv2.setBackgroundResource(R.mipmap.icon_rank_first);
            } else if (position == 1) {
                rankTv2.setBackgroundResource(R.mipmap.icon_rank_second);
            } else if (position == 2) {
                rankTv2.setBackgroundResource(R.mipmap.icon_rank_third);
            } else if(position == 3) {
                rankTv2.setText("4");
                rankTv2.setBackgroundResource(R.mipmap.bg_circle_yellow);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView rankTv;
            private TextView rankTv2;
            private ImageView avatarIm;
            private TextView profitTv;
            private TextView nameTv;

            public ViewHolder(View view) {
                super(view);
                rankTv = (TextView) view.findViewById(R.id.homepage_master_rank_tv);
                rankTv2 = (TextView) view.findViewById(R.id.homepage_master_rank_tv_2);
                avatarIm = (CircleImageView) view.findViewById(R.id.homepage_master_avatar_im);
                profitTv = (TextView) view.findViewById(R.id.homepage_master_profit_tv);
                nameTv = (TextView) view.findViewById(R.id.homepage_master_name_tv);
            }


        }
    }

    private MasterMoreListener mMasterMoreListener;

    public void setMasterMoreListener(MasterMoreListener listener) {
        mMasterMoreListener = listener;
    }

    public interface MasterMoreListener {
        public void more();
    }

    private BroadcastReceiver mNetworkChangeListener;

    private void registerNetworkChangeReceiver() {
        mNetworkChangeListener = new NetworkChangeReceiver();
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

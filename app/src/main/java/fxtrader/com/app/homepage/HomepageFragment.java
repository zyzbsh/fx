package fxtrader.com.app.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.Config;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.login.LoginActivity;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.NetTools;
import fxtrader.com.app.tools.StringUtil;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.BuildPositionDialog;
import fxtrader.com.app.view.BuyDialog;
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

    public static final int REQUEST_LOGIN = 10;

    private MainTitleProfitCtr mTitleProfitCtr;

    private TextView mSystemBulletinContentTv;

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

    private final static int[] LINE_ID = { R.id.btn_timeline, R.id.btn_kline5,
            R.id.btn_kline15, R.id.btn_kline30, R.id.btn_kline60 };

    private Button[] btnLineType = new Button[LINE_ID.length];

    private DataLineFragment mCurDataLineFragment;

    private boolean mExpect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_LOGIN) {
            showBuyDialog(mExpect);
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
        View v = view.findViewById(R.id.homepage_title_profit_layout);
        mTitleProfitCtr = new MainTitleProfitCtr(v);
    }

    private void initAccountInfoLayout(View view) {
        mBalanceAmountTv = (TextView) view.findViewById(R.id.homepage_balance_amount_tv);
        mCashCouponTv = (TextView) view.findViewById(R.id.homepage_cash_coupon_tv);
        view.findViewById(R.id.homepage_recharge_tv).setOnClickListener(this);
        view.findViewById(R.id.homepage_withdraw_tv).setOnClickListener(this);
    }

    private void initSystemBulletinLayout(View view) {
        mSystemBulletinContentTv = (TextView) view.findViewById(R.id.homepage_sys_announcement);
        mSystemBulletinContentTv.setText("系统公告：今日已有30374人参与交易");
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

    private void initViewPager(View view){
        mViewPager = (ViewPager) view.findViewById(R.id.homepage_view_pager);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        int width = UIUtil.getScreenWidth(getActivity()) - UIUtil.dip2px(getContext(), 26) * 2;
        int height = width * 4 / 5 + UIUtil.dip2px(getActivity(), 55);
        params.width = width;
        params.height = height;
        mViewPager.setLayoutParams(params);
        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new DataLineFragment());
//        fragments.add(new DataLineFragment());
        FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        getContactList();
    }

    private void initLineBtn(View view) {
        for (int i = 0; i < LINE_ID.length; ++i) {
            btnLineType[i] = (Button) view.findViewById(LINE_ID[i]);
            btnLineType[i].setOnClickListener(this);
        }
        btnLineType[0].performClick();
    }



    /**
     * 获取合约列表
     */
    private void getContactList(){
        ContractApi contractApi = RetrofitUtils.createApi(ContractApi.class);
        Call<ContractListEntity> repos = contractApi.list(getContractListParams());
        repos.enqueue(new Callback<ContractListEntity>() {
            @Override
            public void onResponse(Call<ContractListEntity> call, Response<ContractListEntity> response) {
                ContractListEntity entity = response.body();
                List<ContractInfoEntity> list = entity.getObject();
                if (list == null || list.isEmpty()) {
                    LogZ.i("合约列表没有数据。");
                    return;
                }
                List<Fragment> fragments = new ArrayList<>();
                for (ContractInfoEntity info : list){
                    DataLineFragment fragment = new DataLineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(IntentItem.CONTARCT_INFO, info);
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
                mViewPager.setAdapter(adapter);
                mViewPager.setOffscreenPageLimit(list.size() - 1);
//                mContractTv.setText(list.get(0).getName());
                startDataTimer();
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

    public final static long REFRESH_TIME = 2500;
    private Timer dataTimer = null;
    private TimerTask dataTimerTask = null;

    private void startDataTimer() {
        Log.i("zyu", "startDataTimer");
        if (null != dataTimer || null != dataTimerTask) {
            stopDataTimer();
        }
        dataTimer = new Timer();
        dataTimerTask = new TimerTask() {
            @Override
            public void run() {
                getMarketPrice();
            }
        };
        dataTimer.schedule(dataTimerTask, 0, REFRESH_TIME);
    }

    private void getMarketPrice(){
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        Call<MarketEntity> response = dataApi.rates(getMarketParams());
        response.enqueue(new Callback<MarketEntity>() {
            @Override
            public void onResponse(Call<MarketEntity> call, Response<MarketEntity> response) {
                MarketEntity vo = response.body();
                vo.init();
                if (mCurDataLineFragment != null) {
                    String dataType = mCurDataLineFragment.getDataType();
                    String data = vo.getData(dataType);
                    mCurDataLineFragment.setPriceTvs(data);
                }
            }

            @Override
            public void onFailure(Call<MarketEntity> call, Throwable t) {
            }
        });
    }

    private Map<String, String> getMarketParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.market.rates");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void stopDataTimer() {
        if (null != dataTimer) {
            dataTimer.cancel();
            dataTimer = null;
        }
        if (null != dataTimerTask) {
            dataTimerTask.cancel();
            dataTimerTask = null;
        }
    }



    private void showBuildPositionDialog() {
        BuildPositionDialog dialog = new BuildPositionDialog(getActivity());
        dialog.show();
        dialog.setOkListener(new BuildPositionDialog.OkListener() {
            @Override
            public void ok() {
                mTitleProfitCtr.setProfitListListener(new MainTitleProfitCtr.ProfitListListener() {
                    @Override
                    public void showList() {
                        //TODO
                        ProfitListPop pop = new ProfitListPop(getActivity());
                        pop.show(mTitleProfitCtr.getProfitView());
                    }
                });
                mTitleProfitCtr.show();
            }
        });
    }

    private void showBuyDialog(boolean up) {
        BuyDialog dialog = new BuyDialog(getActivity(), up);
        dialog.show();
        dialog.setBuildPositionListener(new BuyDialog.BuildPositionListener() {
            @Override
            public void buildPosition() {
                showBuildPositionDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    private void recharge(){
        if (isLogin()) {

        } else {

        }
    }

    private void withdraw() {
        if (isLogin()) {

        } else {

        }
    }

    private void expect(boolean raise) {
        mExpect = raise;
        if (isLogin()) {
            showBuyDialog(raise);
        }else {
            openActivityForResult(LoginActivity.class, REQUEST_LOGIN);
        }
    }

    private void dataLineBtnOnClicked(View v) {
        for (int i = 0; i < LINE_ID.length; ++i) {
            if (LINE_ID[i] == v.getId()) {
                if (!btnLineType[i].isSelected()) {
                    btnLineType[i].setSelected(true);
                    btnLineType[i].setBackgroundColor(getResources().getColor(R.color.red_text));
                    if (mCurDataLineFragment != null) {
                        mCurDataLineFragment.setLineType(i + 1);
                    }
                }
            } else {
                btnLineType[i].setSelected(false);
                btnLineType[i].setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    private boolean isLogin(){
        return LoginConfig.getInstance().isLogin();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mCurDataLineFragment != null) {
            if(hidden) {
            }
        }

    }

    class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments=fragments;
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

}

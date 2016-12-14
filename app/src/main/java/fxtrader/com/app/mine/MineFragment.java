package fxtrader.com.app.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.homepage.RedEnvelopListActivity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.PersonalInfoView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/12/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{

    private PersonalInfoView mPersonalInfoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getUser();
        getCouponNum();
    }

    private void initViews(View view) {
        setupLayoutHeight(view);

        view.findViewById(R.id.mine_fund_list_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_trade_history_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_agent_platform_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_my_buy_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_use_rules_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_announcement_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_share_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_my_gift_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_info_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_red_envelope_layout).setOnClickListener(this);

        mPersonalInfoView = (PersonalInfoView) view.findViewById(R.id.personal_info_view);
        mPersonalInfoView.setListener(new PersonalInfoView.OnPersonalInfoListener() {
            @Override
            public void onPersonalInfoLayoutClicked() {
                openActivity(PersonalInfoActivity.class);
            }

            @Override
            public void onWithdrawTvClicked() {
                openActivity(WithdrawActivity.class);
            }

            @Override
            public void onRechargeTvClicked() {
                openActivity(RechargeActivity.class);
            }

            @Override
            public void onCouponClicked() {
                openActivity(ExperienceVoucherActivity.class);
            }
        });
    }

    private void setupLayoutHeight(View view) {
        int screenWidth = UIUtil.getScreenWidth(getActivity());
        int height = screenWidth / 3;
        setupLayoutHeight(view, R.id.mine_layout_1, height);
        setupLayoutHeight(view, R.id.mine_layout_2, height);
        setupLayoutHeight(view, R.id.mine_layout_3, height);
    }

    private void setupLayoutHeight(View view, int layoutId, int height) {
        LinearLayout layout = (LinearLayout) view.findViewById(layoutId);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
        params.height = height - UIUtil.dip2px(getActivity(), 10);
        layout.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_fund_list_layout://资金流水
                openActivity(ProfitAndLossDetailActivity.class);
                break;
            case R.id.mine_trade_history_layout://交易历史
                openActivity(TradeHistoryActivity.class);
                break;
            case R.id.mine_agent_platform_layout://经纪人平台
                break;
            case R.id.mine_my_buy_tv://我的合买
                break;
            case R.id.mine_use_rules_layout://冠东使用规则
                openActivity(TradeRulesActivity.class);
                break;
            case R.id.mine_announcement_layout://公告
                openActivity(AnnouncementActivity.class);
                break;
            case R.id.mine_share_tv://分享好友
                break;
            case R.id.mine_my_gift_layout://我的礼券
                openActivity(ExperienceVoucherActivity.class);
                break;
            case R.id.mine_info_layout: //个人资料
                openActivity(PersonalInfoActivity.class);
                break;
            case R.id.mine_red_envelope_layout://我的红包
                openActivity(RedEnvelopListActivity.class);
                break;
            default:
                break;
        }
    }

    private void getUser() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<UserEntity> respo = userApi.info(token, getUserParams());
        respo.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                UserEntity entity = response.body();
                LogZ.i(entity.toString());
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getUserParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.get");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getCouponNum(){
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> respo = userApi.coupons(token, getCouponsParams());
        respo.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getCouponsParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.coupon.get");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}

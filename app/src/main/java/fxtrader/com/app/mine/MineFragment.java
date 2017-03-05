package fxtrader.com.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.PersonalInfoView;

/**
 * Created by zhangyuzhu on 2016/12/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{

    private PersonalInfoView mPersonalInfoView;

    private UserEntity mUserEntity;

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
        getUserInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentItem.REQUEST_PERSONAL_INFO && data != null) {
            boolean update = data.getBooleanExtra(IntentItem.PERSONAL_INFO_UPDATE, false);
            if (update) {
                String nickname = data.getStringExtra(IntentItem.NICKNAME);
                mPersonalInfoView.setNickname(nickname);
                String avatarUrl = data.getStringExtra(IntentItem.AVATAR_URL);
                LogZ.i("nickname = " + nickname + ", avatarUrl = " + avatarUrl);
                mPersonalInfoView.loadAvatar(avatarUrl);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            getUserInfo();
        }
    }

    private void initViews(View view) {
        setupLayoutHeight(view);

        view.findViewById(R.id.mine_fund_list_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_trade_history_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_use_rules_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_announcement_layout).setOnClickListener(this);
        view.findViewById(R.id.mine_follow_layout).setOnClickListener(this);
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
//        setupLayoutHeight(view, R.id.mine_layout_3, height);
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
            case R.id.mine_use_rules_layout://冠东使用规则
                openActivity(TradeRulesActivity.class);
                break;
            case R.id.mine_announcement_layout://公告
                openActivity(AnnouncementActivity.class);
                break;
            case R.id.mine_follow_layout://我的关注
                openActivity(MyFollowActivity.class);
                break;
            case R.id.mine_my_gift_layout://我的礼券
                openActivity(ExperienceVoucherActivity.class);
                break;
            case R.id.mine_info_layout: //个人资料
                openPersonalInfoActivity();
                break;
            case R.id.mine_red_envelope_layout://我的红包
                openActivity(RedEnvelopeActivity.class);
                break;
            default:
                break;
        }
    }

    private void getUserInfo() {
        String account = LoginConfig.getInstance().getAccount();
        mUserEntity = UserInfoHelper.getInstance().getEntity(account);
        if (mUserEntity == null) {
            requestUserInfo();
        }
    }

    private void requestUserInfo(){
        showProgressDialog();
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUserEntity = user;
                setUserView();
                dismissProgressDialog();
            }

            @Override
            public void onHttpFailure() {
                dismissProgressDialog();
            }
        });
    }

    private void setUserView(){
        mPersonalInfoView.set(mUserEntity);
    }

    public void openPersonalInfoActivity(){
        Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
        startActivityForResult(intent, IntentItem.REQUEST_PERSONAL_INFO);
    }
}

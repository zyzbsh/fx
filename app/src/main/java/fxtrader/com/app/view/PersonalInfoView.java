package fxtrader.com.app.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fxtrader.com.app.R;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.mine.BindPhoneActivity;

/**
 * Created by pc on 2016/11/19.
 */
public class PersonalInfoView extends RelativeLayout implements View.OnClickListener {

    private CircleImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mBalanceTv;

    private TextView mCouponTv;

    private OnPersonalInfoListener mListener;

    private InfoRemindDialog mDialog;

    private boolean mIsWithdraw;

    public interface OnPersonalInfoListener{
        public void onPersonalInfoLayoutClicked();
        public void onWithdrawTvClicked();
        public void onRechargeTvClicked();
        public void onCouponClicked();
    }


    public PersonalInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_personal_info, this);
        initViews();
    }

    public void setListener(OnPersonalInfoListener listener) {
        mListener = listener;
    }


    private void initViews() {
        findViewById(R.id.person_info_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_withdraw_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_recharge_layout).setOnClickListener(this);
        findViewById(R.id.cash_coupon_count_tv).setOnClickListener(this);
        findViewById(R.id.cash_coupon_icon_im).setOnClickListener(this);
        findViewById(R.id.cash_coupon_tv).setOnClickListener(this);
        mNameTv = (TextView) findViewById(R.id.person_name_tv);
        mBalanceTv = (TextView) findViewById(R.id.person_account_balance_tv);
        mAvatarIm = (CircleImageView) findViewById(R.id.person_avatar_im);
        mCouponTv = (TextView) findViewById(R.id.cash_coupon_count_tv);
        testData();
    }

    private void testData() {
        mNameTv.setText("Atmosphere");
        mBalanceTv.setText("帐号余额：5680");
    }



    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.person_info_layout://个人中心
                mListener.onPersonalInfoLayoutClicked();
                break;
            case R.id.personal_info_withdraw_layout://提现
                mIsWithdraw = true;
                mListener.onWithdrawTvClicked();
                break;
            case R.id.personal_info_recharge_layout://充值
                mIsWithdraw = false;
                mListener.onRechargeTvClicked();
                break;
            case R.id.cash_coupon_tv: //现金券
            case R.id.cash_coupon_icon_im:
            case R.id.cash_coupon_count_tv:
                mListener.onCouponClicked();
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        if (mDialog == null) {
            mDialog = new InfoRemindDialog(getContext());
            mDialog.setOnDialogListener(new InfoRemindDialog.DialogInfoRemindListener() {
                @Override
                public void close() {

                }

                @Override
                public void bind() {
                    if (mIsWithdraw) {

                    } else {

                    }
                    Intent intent = new Intent(getContext(), BindPhoneActivity.class);
                    getContext().startActivity(intent);
                }
            });
        }
        mDialog.show();
    }

    public void set(UserEntity user) {
        Glide.with(getContext()).load(user.getObject().getHeadimgurl()).into(mAvatarIm);
        mNameTv.setText(user.getObject().getNickname());
        mBalanceTv.setText("帐号余额：" + String.valueOf(user.getObject().getFunds()));
        mCouponTv.setText(String.valueOf(user.getObject().getCouponAmount()));
    }


}

package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fxtrader.com.app.MainActivity;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.manager.UserInfoManager;

/**
 * Created by zhangyuzhu on 2016/11/26.
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    private UserEntity mUser;

    private ImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mAccountTv;

    private TextView mSexTv;

    private TextView mBindRemindTv1;

    private TextView mBindRemindTv2;

    private TextView mPhoneTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_info);
        initViews();
        setUser();
    }

    private void initViews() {
        mAvatarIm = (ImageView) findViewById(R.id.person_info_avatar_im);
        mNameTv = (TextView) findViewById(R.id.personal_info_name_tv);
        mAccountTv = (TextView) findViewById(R.id.personal_info_account_tv);

        mSexTv = (TextView) findViewById(R.id.personal_info_sex_tv);

        mBindRemindTv1 = (TextView) findViewById(R.id.personal_info_remind_bind_1_tv);
        mBindRemindTv2 = (TextView) findViewById(R.id.personal_info_remind_bind_2_tv);
        mPhoneTv = (TextView) findViewById(R.id.personal_info_bind_reward_tv);

        findViewById(R.id.personal_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_bind_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_change_pwd_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_login_out_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_info_sex_layout://性别
                break;
            case R.id.personal_info_bind_layout://绑定手机
                if (!hasTelNumber()) {
                    openActivity(BindPhoneActivity.class);
                }
                break;
            case R.id.personal_info_change_pwd_layout://修改密码
                openActivity(ChangePwdActivity.class);
                break;
            case R.id.personal_info_login_out_layout:
                loginOut();
                break;
            default:
                break;
        }
    }

    private void setUser() {
        mUser = UserInfoHelper.getInstance().getEntity(LoginConfig.getInstance().getAccount());
        if (mUser  == null ) {
            requestUserInfo();
        } else {
            setUserView();
        }
    }

    private void requestUserInfo(){
        showProgressDialog();
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUser = user;
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
        Glide.with(this).load(mUser.getObject().getHeadimgurl()).into(mAvatarIm);
        mNameTv.setText(mUser.getObject().getNickname());
        mAccountTv.setText("帐号余额：" + String.valueOf(mUser.getObject().getFunds()));
        int sex = mUser.getObject().getSex();
        if (sex == 1) {
            mSexTv.setText("男");
        } else {
            mSexTv.setText("女");
        }
        if (hasTelNumber()) {
            mBindRemindTv1.setVisibility(View.GONE);
            mBindRemindTv2.setVisibility(View.GONE);
            mPhoneTv.setVisibility(View.VISIBLE);
            mPhoneTv.setText(mUser.getObject().getTelNumber());
        } else {
            mBindRemindTv1.setVisibility(View.VISIBLE);
            mBindRemindTv2.setVisibility(View.VISIBLE);
            mPhoneTv.setVisibility(View.GONE);
        }
    }

    private boolean hasTelNumber(){
        if (mUser == null) {
            return false;
        }

        String telNumber = mUser.getObject().getTelNumber();
        return !TextUtils.isEmpty(telNumber);
    }

    private void loginOut(){
        openActivity(MainActivity.class);
    }

}

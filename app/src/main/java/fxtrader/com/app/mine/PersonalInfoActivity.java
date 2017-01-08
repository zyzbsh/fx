package fxtrader.com.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import fxtrader.com.app.MainActivity;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.http.manager.UserInfoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/11/26.
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    private UserEntity mUser;

    private TextView mEditTv;

    private ImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mAccountTv;

    private TextView mSexTv;

    private TextView mBindRemindTv1;

    private TextView mBindRemindTv2;

    private TextView mPhoneTv;

    private RelativeLayout mChangeAvatarLayout;

    private LinearLayout mInfoLayout;

    private LinearLayout mNicknameLayout;

    private EditText mNicknameEdt;

    private Spinner mSexSpinner;

    private String mSexChecked = "";

    private boolean mIsEditMode = false;

    private boolean mIsUpdate = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_info);
        initViews();
        setUser();
    }

    private void initViews() {
        mEditTv = (TextView) findViewById(R.id.title_edt_tv);

        mAvatarIm = (ImageView) findViewById(R.id.person_info_avatar_im);
        mChangeAvatarLayout = (RelativeLayout) findViewById(R.id.person_info_change_avatar_layout);

        mInfoLayout = (LinearLayout) findViewById(R.id.personal_info_layout);
        mNameTv = (TextView) findViewById(R.id.personal_info_name_tv);
        mAccountTv = (TextView) findViewById(R.id.personal_info_account_tv);

        mNicknameLayout = (LinearLayout) findViewById(R.id.personal_info_nickname_layout);
        mNicknameEdt = (EditText) findViewById(R.id.personal_info_nickname_edt);

        mSexTv = (TextView) findViewById(R.id.personal_info_sex_tv);

        mSexSpinner = (Spinner) findViewById(R.id.personal_info_sex_spinner);
        String[] mItems = getResources().getStringArray(R.array.sex);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexSpinner .setAdapter(adapter);
        mSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] sexs = getResources().getStringArray(R.array.sex);
                mSexChecked = sexs[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mBindRemindTv1 = (TextView) findViewById(R.id.personal_info_remind_bind_1_tv);
        mBindRemindTv2 = (TextView) findViewById(R.id.personal_info_remind_bind_2_tv);
        mPhoneTv = (TextView) findViewById(R.id.personal_info_bind_reward_tv);

        findViewById(R.id.title_edt_tv).setOnClickListener(this);
        findViewById(R.id.personal_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_bind_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_change_pwd_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_login_out_layout).setOnClickListener(this);

        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if (mIsEditMode) {
            restoreUI();
        } else {
            Intent intent = getIntent();
            intent.putExtra(IntentItem.PERSONAL_INFO_UPDATE, mIsUpdate);
            intent.putExtra(IntentItem.NICKNAME, mNameTv.getText().toString().trim());
            intent.putExtra(IntentItem.AVATAR_URL, "");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_edt_tv:
                if (mIsEditMode) {
                    update();
                } else {
                    changeToEditMode();
                }
                break;
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

    private void changeToEditMode(){
        mIsEditMode = true;

        mEditTv.setText("保存");

        mChangeAvatarLayout.setVisibility(View.VISIBLE);

        mNicknameLayout.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);

        mSexTv.setVisibility(View.GONE);
        mSexSpinner.setVisibility(View.VISIBLE);
    }

    private void restoreUI(){
        mIsEditMode = false;

        mEditTv.setText("编辑");

        mChangeAvatarLayout.setVisibility(View.GONE);

        mNicknameLayout.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);

        mSexTv.setVisibility(View.VISIBLE);
        mSexSpinner.setVisibility(View.GONE);
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
        setSpinnerItemSelectedByValue(mSexSpinner, mSexTv.getText().toString().trim());
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

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    private void loginOut(){
        LoginConfig.getInstance().clear();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(IntentItem.LOG_OUT, true);
        openActivity(MainActivity.class);
    }

    private void update(){
        String nickname = mNicknameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showToastShort("请输入昵称");
            return;
        }
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> request = userApi.updateInfo(token, getParams());
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                dismissProgressDialog();
                CommonResponse entity = response.body();
                if (entity != null) {
                    showToastShort(entity.getMessage());
                    if (entity.isSuccess()) {
                        mIsUpdate = true;
                        restoreUI();
                        setUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.updateUserInfo");
        params.put("headimgurl", mUser.getObject().getHeadimgurl());
        String nickname = mNicknameEdt.getText().toString().trim();
        params.put("nickname", mNicknameEdt.getText().toString().trim());
        int sex;
        if ("男".equals(mSexChecked)) {
            sex = HttpConstant.SexType.MALE;
        } else {
            sex = HttpConstant.SexType.FEMALE;
        }
        params.put("sex", sex + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setUI(){
        mNameTv.setText(mNicknameEdt.getText().toString().trim());
        mSexTv.setText(mSexChecked);
    }

}

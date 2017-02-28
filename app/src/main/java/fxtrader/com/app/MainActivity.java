package fxtrader.com.app;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.AppUpdateResponse;
import fxtrader.com.app.find.FindFragment;
import fxtrader.com.app.homepage.HomepageFragment;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.http.manager.GeTuiClientIdManager;
import fxtrader.com.app.login.LoginNewActivity;
import fxtrader.com.app.mine.MineFragment;
import fxtrader.com.app.permission.IPermissionCheck;
import fxtrader.com.app.permission.IPermissionRequest;
import fxtrader.com.app.permission.PermissionChecker;
import fxtrader.com.app.permission.PermissionRequester;
import fxtrader.com.app.protection.ProtectionFragment;
import fxtrader.com.app.tools.GeTuiUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.update.UpdateHelper;
import fxtrader.com.app.update.bean.Update;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String[] PERMISSIONS_REQUEST = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private View[] mBottomTabBgViews = new View[4];

    private RadioGroup mRadioGroup;

    private HomepageFragment mHomepageFragment;

    private BaseFragment mProtectionFragment;

    private FindFragment mFindFragment;

    private BaseFragment mMineFragment;

    private ArrayList<Integer> mCheckedIdRecords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppApplication.getInstance().setActivity(this);
        setContentView(R.layout.activity_main);
        initBottomLayout();
        checkPermission();
        GeTuiUtil.init();

        checkApp();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogZ.i("onNewIntent");
        super.onNewIntent(intent);
        if (intent.hasExtra(IntentItem.LOG_OUT)) {
            LogZ.i("log out");
            if (mHomepageFragment != null) {
                mHomepageFragment.logOut();
            }
            mRadioGroup.check(R.id.main_tab_homepage_btn);
            String msg = intent.getStringExtra(IntentItem.MSG);
            if (!TextUtils.isEmpty(msg)) {
                showToastShort(msg);
            }
        }
    }

    private void checkPermission() {
        PermissionChecker.with(this).check(PERMISSIONS_REQUEST)
                .subscribe(new IPermissionCheck() {

                    @Override
                    public void result(List<String> deniedPermissions) {
                        if (!deniedPermissions.isEmpty()) {
                            requestPermission(deniedPermissions);
                        }
                    }
                });
    }

    private void requestPermission(List<String> deniedPermissions) {
        String[] denieds = new String[deniedPermissions.size()];
        deniedPermissions.toArray(denieds);
        PermissionRequester.with(this).request(denieds)
                .subscribe(new IPermissionRequest() {

                    @Override
                    public void onDenied(String[] permissions) {
                        ArrayList<String> list = new ArrayList<String>(Arrays
                                .asList(permissions));
                    }

                    @Override
                    public void onAllGranted() {
                    }
                });
    }

    private void initBottomLayout() {

        mBottomTabBgViews[0] = findViewById(R.id.main_tab_bg_homepage);
        mBottomTabBgViews[1] = findViewById(R.id.main_tab_bg_protection);
        mBottomTabBgViews[2] = findViewById(R.id.main_tab_bg_find);
        mBottomTabBgViews[3] = findViewById(R.id.main_tab_bg_mine);

        mRadioGroup = (RadioGroup) findViewById(R.id.main_radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);

        RadioButton btn = (RadioButton) findViewById(R.id.main_tab_homepage_btn);
        btn.setChecked(true);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (R.id.main_tab_min_btn == checkedId) {
            if (isLogin()) {
                hideAllFragment(transaction);
                checkTabBg(3);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    transaction.add(R.id.main_content_layout, mMineFragment);
                } else {
                    transaction.show(mMineFragment);
                }
                transaction.commit();
            } else {
                Intent intent = new Intent(this, LoginNewActivity.class);
                intent.putExtra(IntentItem.MINE, true);
                startActivity(intent);
                int size = mCheckedIdRecords.size();
                mRadioGroup.check(mCheckedIdRecords.get(size - 1));
            }
        } else {
            mCheckedIdRecords.clear();
            hideAllFragment(transaction);
            if (R.id.main_tab_homepage_btn == checkedId) {
                checkTabBg(0);
                if (mHomepageFragment == null) {
                    mHomepageFragment = new HomepageFragment();
                    mHomepageFragment.setMasterMoreListener(new HomepageFragment.MasterMoreListener() {
                        @Override
                        public void more() {
                            if (mFindFragment != null) {
                                mFindFragment.setMasterListState();
                            }
                            mRadioGroup.check(R.id.main_tab_find_btn);
                        }
                    });
                    transaction.add(R.id.main_content_layout, mHomepageFragment);
                } else {
                    transaction.show(mHomepageFragment);
                }
            } else if (R.id.main_tab_protection_btn == checkedId) {
                checkTabBg(1);
                if (mProtectionFragment == null) {
                    mProtectionFragment = new ProtectionFragment();
                    transaction.add(R.id.main_content_layout, mProtectionFragment);
                } else {
                    transaction.show(mProtectionFragment);
                }
            } else if (R.id.main_tab_find_btn == checkedId) {
                checkTabBg(2);
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                    transaction.add(R.id.main_content_layout, mFindFragment);
                } else {
                    transaction.show(mFindFragment);
                }
            }
            transaction.commit();
            mCheckedIdRecords.add(checkedId);
        }

    }

    private void checkTabBg(int index) {
        for (int i = 0; i < mBottomTabBgViews.length; i++) {
            if (index == i) {
                mBottomTabBgViews[i].setVisibility(View.VISIBLE);
            } else {
                mBottomTabBgViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mHomepageFragment != null) fragmentTransaction.hide(mHomepageFragment);
        if (mProtectionFragment != null) fragmentTransaction.hide(mProtectionFragment);
        if (mFindFragment != null) fragmentTransaction.hide(mFindFragment);
        if (mMineFragment != null) fragmentTransaction.hide(mMineFragment);
    }

    private void checkApp() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<AppUpdateResponse> request = userApi.checkApp(getCheckAppParams());
        request.enqueue(new Callback<AppUpdateResponse>() {
            @Override
            public void onResponse(Call<AppUpdateResponse> call, Response<AppUpdateResponse> response) {
                AppUpdateResponse appUpdateResponse = response.body();
                if (appUpdateResponse.getObject() != null) {
                    Update update = getUpdate(appUpdateResponse);
                    UpdateHelper.getInstance()
                            .check(update, MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AppUpdateResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getCheckAppParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("mobileDevices", "android");
        params.put("appVersions", getVersionCode(this) + "");
        return params;
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Update getUpdate(AppUpdateResponse appUpdateResponse){
        Update update = new Update();
        update.setUpdateUrl(appUpdateResponse.getObject().getDownloadUrl());
        update.setVersionCode(Integer.parseInt(appUpdateResponse.getObject().getAppVersions()));
        update.setApkSize(10);
        update.setVersionName("1.0.0");
        update.setUpdateContent(appUpdateResponse.getObject().getUpdateContent());
        update.setForce(appUpdateResponse.getObject().isForceUpdate());
        return update;
    }

}

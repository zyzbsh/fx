package fxtrader.com.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.find.FindFragment;
import fxtrader.com.app.homepage.HomepageFragment;
import fxtrader.com.app.login.LoginNewActivity;
import fxtrader.com.app.mine.MineFragment;
import fxtrader.com.app.protection.ProtectionFragment;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

}

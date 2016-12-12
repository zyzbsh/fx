package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.homepage.DataLineFragment;

/**
 * Created by zhangyuzhu on 2016/12/10.
 */
public class RedEnvelopeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_red_envelope);

        mViewPager = (ViewPager) findViewById(R.id.red_envelope_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mRadioGroup.check(R.id.red_envelope_receive_btn);
                } else {
                    mRadioGroup.check(R.id.red_envelope_send_btn);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RedEnvelopeReceiveFragment());
        fragments.add(new RedEnvelopeSendFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mRadioGroup = (RadioGroup) findViewById(R.id.red_envelope_radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.red_envelope_receive_btn);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.red_envelope_receive_btn:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.red_envelope_send_btn:
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;
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
        }
    }
}

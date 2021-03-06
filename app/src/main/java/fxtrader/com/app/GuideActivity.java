package fxtrader.com.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.adapter.GuidePageAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.UserRecordConfig;

/**

 * 实现首次启动的引导页面

 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private ViewPager vp;
    private int []imageIdArray;
    private List<View> viewList;
    private LinearLayout vg;
    private ImageView iv_point;
    private ImageView []ivPointArray;
    private TextView mStartTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserRecordConfig.getInstance().isGuided()) {
            openActivity(MainActivity.class);
            finish();
            return;
        }
        setContentView(R.layout.activity_guide);

        mStartTv = (TextView) findViewById(R.id.guide_start_tv);
        mStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRecordConfig.getInstance().guided();
                openActivity(MainActivity.class);
                finish();
            }

        });
        initViewPager();
        initPoint();
    }

    /**
     * 加载底部圆点
     */

    private void initPoint() {
        vg = (LinearLayout) findViewById(R.id.guide_ll_point);
        ivPointArray = new ImageView[viewList.size()];
        int size = viewList.size();
        for (int i = 0;i<size;i++){
            iv_point = new ImageView(this);
            ivPointArray[i] = iv_point;
            if (i == 0){
                iv_point.setBackgroundResource(R.drawable.shape_full_holo);
            }else{
                iv_point.setBackgroundResource(R.drawable.shape_empty_holo);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 5, 5, 5);
            vg.addView(ivPointArray[i], params);
        }
    }
    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = new int[]{R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0;i<len;i++){
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);
            //将ImageView加入到集合中
            viewList.add(imageView);
        }
        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * 滑动后的监听
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = imageIdArray.length;
        for (int i = 0;i<length;i++){
            ivPointArray[position].setBackgroundResource(R.drawable.shape_full_holo);
            if (position != i){
                ivPointArray[i].setBackgroundResource(R.drawable.shape_empty_holo);
            }
        }
        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1){
            mStartTv.setVisibility(View.VISIBLE);
        }else {
            mStartTv.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

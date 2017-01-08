package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.MainActivity;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.AnnouncementConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.AnnouncementDetailEntity;
import fxtrader.com.app.tools.DateTools;

/**
 * Created by zhangyuzhu on 2016/11/22.
 * 公告
 */
public class AnnouncementDetailActivity extends BaseActivity{

    private TextView mTitleTv;

    private TextView mTimeTv;

    private TextView mContentTv;

    private TextView mReturnTv;

    private AnnouncementDetailEntity mEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_announcement_detail);
        mEntity = (AnnouncementDetailEntity) getIntent().getSerializableExtra(IntentItem.ANNOUNCEMENT_DETAIL);
        AnnouncementConfig.getInstance().read(mEntity.getId());
        initViews();
        setViews();
    }

    private void initViews(){
        mTitleTv = (TextView) findViewById(R.id.announcement_detail_title_tv);
        mTimeTv = (TextView) findViewById(R.id.announcement_detail_time_tv);
        mContentTv = (TextView) findViewById(R.id.announcement_detail_content_tv);
        mReturnTv = (TextView) findViewById(R.id.announcement_detail_return_homepage_tv);
        mReturnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(MainActivity.class);
            }
        });
    }

    private void setViews() {
        mTitleTv.setText(mEntity.getTitle());
        String date = DateTools.changeToDate2(mEntity.getDate());
        mTimeTv.setText(getString(R.string.time_num, date));
        mContentTv.setText(mEntity.getContent());
    }
}

package fxtrader.com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * 设置止盈(止损)布局
 * Created by pc on 2016/12/17.
 */
public class ProfitAndLossView extends LinearLayout implements View.OnClickListener{

    private TextView mTitleTv;

    private ImageButton mMinuxBtn;

    private ImageButton mPlusBtn;

    private TextView mPercentTv;

    private int mCount = 0;

    public ProfitAndLossView(Context context) {
        super(context);
        initView();
    }

    public ProfitAndLossView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProfitAndLossView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_profit_and_loss_set, this, true);
        mTitleTv = (TextView) findViewById(R.id.view_profit_and_loss_set_title_tv);
        mPercentTv = (TextView) findViewById(R.id.dialog_build_set_percent_tv);
        mMinuxBtn = (ImageButton) findViewById(R.id.view_profit_and_loss_set_minus_btn);
        mPlusBtn = (ImageButton) findViewById(R.id.view_profit_and_loss_set_plus_btn);
        mMinuxBtn.setOnClickListener(this);
        mPlusBtn.setOnClickListener(this);
        mPercentTv.setText("不设");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_profit_and_loss_set_minus_btn:
                if (mCount > 0) {
                    mCount--;
                    if (mCount == 0) {
                        mPercentTv.setText("不设");
                    } else {
                        mPercentTv.setText(mCount * 10 + "%");
                    }
                }
                break;
            case R.id.view_profit_and_loss_set_plus_btn:
                if (mCount < 9) {
                    mCount++;
                    mPercentTv.setText(mCount * 10 + "%");
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(int resId) {
        mTitleTv.setText(resId);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public int getPercent() {
        return mCount;
    }

    public void setPercent(int percent){
        if (percent == 0 && percent == 10){
            mCount = 0;
            mPercentTv.setText("不设");
        } else {
            mCount = percent;
            mPercentTv.setText(mCount * 10 + "%");
        }

    }
}

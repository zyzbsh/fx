package fxtrader.com.app.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/19.
 */
public abstract class MainItemView extends LinearLayout {

    private LinearLayout mLayout;

    private TextView mMoreTv;

    private TextView mTitleTv;

    public MainItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_main_item, this);
        mLayout = (LinearLayout) findViewById(R.id.main_item_view);
        mMoreTv = (TextView) findViewById(R.id.main_item_more_tv);
        mTitleTv = (TextView) findViewById(R.id.main_lists_title_tv);
        mTitleTv.setText(getTitleRes());
    }

    public void setOnMoreClicked(OnClickListener listener) {
        mMoreTv.setOnClickListener(listener);
    }


    protected View getDividerView() {
        View v = new View(getContext());
        v.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        v.setBackgroundColor(Color.parseColor("#000000"));
        return v;
    }

    protected void addItemView(View view) {
        mLayout.addView(view);
    }

    abstract protected int getTitleRes();



}

package fxtrader.com.app.view.ctr;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/20.
 */
public class MainTitleProfitCtr {

    private View mProfitView;

    private TextView mProfitTv;

    private ImageButton mProfitListBtn;

    public interface ProfitListListener {
        public void showList();
    }


    public MainTitleProfitCtr(View profitView) {
        mProfitView = profitView;
        mProfitTv = (TextView) profitView.findViewById(R.id.homepage_title_profit_tv);
        mProfitListBtn = (ImageButton) profitView.findViewById(R.id.homepage_title_profit_list_btn);
    }

    public View getProfitView(){
        return mProfitView;
    }

    public boolean isProfitViewShow() {
        return mProfitView.getVisibility() == View.VISIBLE;
    }

    public void show() {
        mProfitView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mProfitView.setVisibility(View.GONE);
    }

    public void setProfit(String profit) {
        mProfitTv.setText(profit);
    }

    public void setProfitListListener(final ProfitListListener listener) {
        mProfitListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.showList();
                }
            }
        });
    }


}

package fxtrader.com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/19.
 */
public class MainMasterItemView extends MainItemView {

    public MainMasterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        testData();
    }

    @Override
    protected int getTitleRes() {
        return R.string.master_list;
    }

    public void testData() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.main_item_master, this, false);
            ImageView avatarIm = (CircleImageView) view.findViewById(R.id.main_item_master_avatar_im);
            TextView avatarNameTv = (TextView) view.findViewById(R.id.main_item_master_avatar_name_tv);
            TextView amountTv = (TextView) view.findViewById(R.id.main_item_master_amount_tv);
            TextView buyPriceTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_tv);
            TextView buyPriceNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_num_tv);
            TextView buyNameTv = (TextView) view.findViewById(R.id.main_item_master_buy_name_tv);
            TextView buySelectTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_tv);
            TextView buySelectNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_num_tv);
            TextView followTv = (TextView) view.findViewById(R.id.main_item_master_follow_tv);

            avatarNameTv.setText("NICHOLAS");
            amountTv.setText("2306.00");
            buyPriceTv.setText("买入价");
            buyPriceNumTv.setText("1930.2");
            buyNameTv.setText("0.2t粤东有");
            buySelectTv.setText("买跌");
            buySelectNumTv.setText("5手");
            followTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                }
            });

            addItemView(view);
            if (i != 3 - 1) {
                addItemView(getDividerView());
            }
        }
    }

}

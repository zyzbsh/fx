package fxtrader.com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/19.
 */
public class MainRedEnvelopItemView extends MainItemView {
    public MainRedEnvelopItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        testData();
    }

    @Override
    protected int getTitleRes() {
        return R.string.red_envelope_list;
    }

    private void testData() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.main_item_red_envelop, this, false);

            CircleImageView avatarIm = (CircleImageView) view.findViewById(R.id.main_item_red_avatar_im);
            TextView nameTv = (TextView) view.findViewById(R.id.main_item_red_name_tv);
            TextView timeTv = (TextView) view.findViewById(R.id.main_item_red_time_tv);
            TextView contentTv = (TextView) view.findViewById(R.id.main_item_red_content_tv);
            TextView followTv = (TextView) view.findViewById(R.id.main_item_red_follow_tv);
            followTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            nameTv.setText("EDITD");
            timeTv.setText("20:00");
            contentTv.setText("收到100元红包");

            addItemView(view);
            if (i != 3 - 1) {
                addItemView(getDividerView());
            }
        }
    }

}

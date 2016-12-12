package fxtrader.com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/19.
 */
public class MainWinStreamItemView extends MainItemView {
    public MainWinStreamItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        testData();
    }

    @Override
    protected int getTitleRes() {
        return R.string.win_stream_list;
    }

    private void testData() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.main_item_win_steam, this, false);
            ImageView tagIm = (ImageView) view.findViewById(R.id.main_item_win_tag_im);
            CircleImageView avatarIm = (CircleImageView) view.findViewById(R.id.main_item_win_avatar_im);
            TextView nameTv = (TextView) view.findViewById(R.id.main_item_win_name_tv);
            TextView followTv = (TextView) view.findViewById(R.id.main_item_win_follow_tv);
            followTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                }
            });
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.main_item_win_icon_layout);
            for (int j = 0; j < 5; j++) {
                ImageView im = new ImageView(getContext());
                layout.addView(im);
            }

            nameTv.setText("STACY MARTIN");

            addItemView(view);
            if ( i != 3 - 1) {
                addItemView(getDividerView());
            }
        }
    }
}

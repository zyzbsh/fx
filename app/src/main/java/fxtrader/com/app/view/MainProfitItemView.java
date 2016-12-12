//package fxtrader.com.app.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import fxtrader.com.app.R;
//
///**
// * Created by pc on 2016/11/19.
// */
//public class MainProfitItemView extends MainItemView {
//    public MainProfitItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        testData();
//    }
//
//    @Override
//    protected int getTitleRes() {
//        return R.string.profit_list;
//    }
//
//    private void testData() {
//        for (int i = 0; i < 3; i++) {
//            View view = LayoutInflater.from(getContext()).inflate(R.layout.find_item_profit, this, false);
//
//            ImageView tagIm = (ImageView) view.findViewById(R.id.main_item_profit_tag_im);
//            CircleImageView avatarIm = (CircleImageView) view.findViewById(R.id.main_item_profit_avatar_im);
//            TextView nameTv = (TextView) view.findViewById(R.id.main_item_profit_name_tv);
//            TextView maxPercentTv = (TextView) view.findViewById(R.id.main_item_max_percent_tv);
//            TextView maxNameTv = (TextView) view.findViewById(R.id.main_item_max_name_tv);
//            TextView followTv = (TextView) view.findViewById(R.id.main_item_profit_follow_tv);
//            followTv.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //TODO
//                }
//            });
//
//            nameTv.setText("ABCD");
//            maxPercentTv.setText("98.88%");
//            String content = getContext().getString(R.string.profit_max_name, "粤东有");
//            maxNameTv.setText(content);
//
//            addItemView(view);
//            if (i != 3 - 1) {
//                addItemView(getDividerView());
//            }
//        }
//    }
//}

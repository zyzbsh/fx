package fxtrader.com.app.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by zhangyuzhu on 2016/11/30.
 */
public final class UIUtil {

    private static int sScreenWidth = 0;
    private static int sScreenHeight = 0;

    public static int getScreenWidth(Activity activity) {
        if (sScreenWidth == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            sScreenWidth = displayMetrics.widthPixels;
        }
        return sScreenWidth;
    }

    public static int getScreenHeight(Activity activity) {
        if (sScreenHeight == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            sScreenHeight = displayMetrics.heightPixels;
        }
        return sScreenHeight;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void openActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void setForegroundColor(TextView tv, String str, int color, int start, int end){
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(color),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }

}

package fxtrader.com.app.tools;

import android.content.Context;
import android.util.DisplayMetrics;

import fxtrader.com.app.AppApplication;

public class PixelTools {
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static float getRatio() {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		AppApplication.getInstance().getActivity().getWindowManager()
				.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		float ratioWidth = (float) screenWidth / 600;
		float ratioHeight = (float) screenHeight / 800;
		return Math.min(ratioWidth, ratioHeight);
	}
}

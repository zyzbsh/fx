package fxtrader.com.app.tools;

import java.text.DecimalFormat;

public class DecimalTools {

	public static double formatTWO(double d) {
		return Double.parseDouble(new DecimalFormat("######0.00").format(d));
	}

	public static float formatFloatThree(float d) {

		double mDouble = Double.parseDouble(d + "");

		String str = new DecimalFormat("#####0.000").format(mDouble);

		return Float.parseFloat(str);
	}

	public static long formatZERO(double d) {
		return (long) d;
	}
}

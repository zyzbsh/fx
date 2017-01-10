package fxtrader.com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.tools.UIUtil;

public class NewLineView extends LinearLayout implements OnClickListener {

	private final static int[] LINE_ID = { R.id.btn_timeline,
			R.id.btn_kline5, R.id.btn_kline30, R.id.btn_kline60, R.id.btn_day};

	private final static int[] LINE_TYPE = {HttpConstant.KType.MIN_1, HttpConstant.KType.MIN_5, HttpConstant.KType.MIN_30, HttpConstant.KType.HOUR_1, HttpConstant.KType.DAY};

	private Button[] btnLineType = new Button[LINE_ID.length];

	private LayoutInflater inflater;

	private NewMAChartView mChartView;

	public NewLineView(Context context) {
		super(context);
		initView();
	}

	public NewLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public NewLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		Log.i("zyu", "initView newLine");
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_main_line, this, true);
		mChartView = (NewMAChartView) findViewById(R.id.view_new_machart);
		for (int i = 0; i < LINE_ID.length; ++i) {
			btnLineType[i] = (Button) findViewById(LINE_ID[i]);
			btnLineType[i].setOnClickListener(this);
		}
		btnLineType[0].performClick();
		LayoutParams lp = (LayoutParams) mChartView.getLayoutParams();
		int screenWidth = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity());
		int pad = UIUtil.dip2px(getContext(), 35);
		int width = screenWidth - 2 * pad;
		lp.width = width;
		lp.height = width * 3 / 5;
		mChartView.setLayoutParams(lp);

	}

	@Override
	public void onClick(View v) {
		if (R.id.btn_timeline == v.getId() || R.id.btn_kline5 == v.getId()
				|| R.id.btn_day == v.getId()
				|| R.id.btn_kline30 == v.getId()
				|| R.id.btn_kline60 == v.getId()) {
			for (int i = 0; i < LINE_ID.length; ++i) {
				if (LINE_ID[i] == v.getId()) {
					if (!btnLineType[i].isSelected()) {
						btnLineType[i].setSelected(true);
						mChartView.setLineType(LINE_TYPE[i]);
						btnLineType[i].setBackgroundColor(getResources().getColor(R.color.red_text));
					}
				} else {
					btnLineType[i].setSelected(false);
					btnLineType[i].setBackgroundColor(getResources().getColor(R.color.transparent));
				}
			}
		}
	}

	public void setCode(String code) {
		mChartView.setCode(code);
	}

	public void setCurentPrice(float price) {
		mChartView.setCurentPrice(price);
	}

}

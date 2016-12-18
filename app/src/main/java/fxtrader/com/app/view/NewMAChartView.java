package fxtrader.com.app.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import fxtrader.com.app.entity.OHLCEntity;

import fxtrader.com.app.config.Config;
//import fxtrader.com.app.request.DataRequest;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.DataLineApi;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.DecimalTools;
import fxtrader.com.app.tools.NetTools;
import fxtrader.com.app.tools.PixelTools;
import fxtrader.com.app.tools.StringUtil;
import fxtrader.com.app.entity.DataVo;
import fxtrader.com.app.entity.MACandleChartVO;
import fxtrader.com.app.entity.MAChartVO;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewMAChartView extends SurfaceView implements Callback {

	public final static String tips = "非WIFI状态下该项不显示";

	private final static String DATASERVICE = "DATASERVICE";

	public final static float RATIO = PixelTools.getRatio();
	public final static long REFRESH_DRAW_TIME = 80;

//	private final static int Color_Blue = Color.rgb(0, 144, 255);
	private final static int Color_WHITE = Color.rgb(255, 255, 255);
	private final static int Color_Red = Color.rgb(231, 96, 73);
	private final static int Color_Green = Color.rgb(16, 170, 154);
//	private final static int Color_YELLOW = Color.rgb(255, 244, 92);
	private final static int Color_Blue2 = Color.rgb(106, 191, 255);

	private Paint textPaint, gridPaint, linePaint, mPaintPositive,
			mPaintNegative, horizontalLinePaint;

	private MAChartVO mLineChart = null;
	private MACandleChartVO mCandleChart = null;

	private float count = 2.5f * RATIO;

	private int lineType = 1;
	private String code = "AU";

	private boolean isUp = true;

//	private DataRequest request;

	private Timer drawTimer = null;
	private TimerTask drawTimerTask = null;
	private Timer dataTimer = null;
	private TimerTask dataTimerTask = null;

	private float curentPrice = 0.0f;

	private boolean flag = true;

	private Retrofit mRetrofit;

	private DataLineApi mDataLineApi;

	private String mContractType = "";

	public NewMAChartView(Context context) {
		super(context);
		initView();
	}

	public NewMAChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public NewMAChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public void setOnTop(boolean top) {
		setZOrderOnTop(top);
	}

	public void setContractType(String type) {
		mContractType = type;
	}

	private void initView() {
		//解决拖动时，上下边界线出现黑色闪烁
//		setBackgroundColor(Color.WHITE);
//		setZOrderOnTop(true);
//		getHolder().setFormat(PixelFormat.TRANSPARENT);
		Log.i("zyu", "NewMachartView");
		textPaint = new Paint();
		textPaint.setColor(Color_Red);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(15 * RATIO);
		textPaint.setStrokeWidth(1.5f * RATIO);
		gridPaint = new Paint();
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
				RATIO);
		gridPaint.setPathEffect(effects);
		gridPaint.setAntiAlias(true);
		gridPaint.setStrokeWidth(RATIO / 2);
		linePaint = new Paint();
		linePaint.setStrokeWidth(RATIO * 2.5f);
		linePaint.setColor(Color_Red);
		linePaint.setAntiAlias(true);

		horizontalLinePaint = new Paint();
		horizontalLinePaint.setColor(Color_Red);
		horizontalLinePaint.setStrokeWidth(1.5f * RATIO);

		mPaintPositive = new Paint();
		mPaintPositive.setColor(Color_Red);
		mPaintPositive.setStrokeWidth(1.5f * RATIO);
		mPaintNegative = new Paint();
		mPaintNegative.setColor(Color_Green);
		mPaintNegative.setStrokeWidth(1.5f * RATIO);

//		Retrofit retrofit = new Retrofit.Builder()
//				.baseUrl("http://quota.ht.ifxeasy.com/")
//				.addConverterFactory(GsonConverterFactory.create())
//				.build();
//		mDataLineApi = retrofit.create(DataLineApi.class);
		mDataLineApi = RetrofitUtils.createApi(DataLineApi.class);
		getHolder().addCallback(this);
	}

	private void draw() {
		Canvas canvas = getHolder().lockCanvas();
		try {
			if (null != canvas) {
				canvas.drawColor(Color_WHITE);
				if (NetTools.isWiFiState(getContext())) {
					drawView(canvas);
				} else {
					drawTips(canvas);
				}

			}
		} catch (Exception e) {
		} finally {
			try {
				if (null != getHolder() && null != canvas) {
					getHolder().unlockCanvasAndPost(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void drawView(Canvas canvas) {
		if (1 == lineType) {
			if (null != getmLineChart()) {
				drawX(canvas, getmLineChart().getXtitle());
				drawY(canvas, getmLineChart().getYtitle());
				drawLine(canvas, getmLineChart());
			}
		} else {
			if (null != getmCandleChart()) {
				drawX(canvas, getmCandleChart().getXtitle());
				drawY(canvas, getmCandleChart().getYtitle());
				drawCandle(canvas, getmCandleChart());
			}
		}
	}

	private void drawTips(Canvas canvas) {
		Paint tipsPaint = new Paint();
		tipsPaint.setTextSize(18 * RATIO);
		tipsPaint.setColor(Color.BLACK);
		Rect rect = new Rect();
		tipsPaint.getTextBounds(tips, 0, tips.length(), rect);
		int txtWidth = rect.width();
		canvas.drawText("非WIFI状态下该项不显示", (canvas.getWidth() - txtWidth) / 2,
				canvas.getHeight() / 2, tipsPaint);
	}

	private void drawLine(Canvas canvas, MAChartVO mChartVO) {
		if (null == mChartVO) {
			return;
		}
		List<Float> lineData = mChartVO.getValues();
		float lineLength = ((float) canvas.getWidth() / lineData.size());
		PointF ptFirst = null;
		float valueX = 0.0f - (canvas.getWidth() / mChartVO.getXtitle().size());
		float valueY = (float) ((1f - (lineData.get(0).floatValue() - mChartVO
				.getMinPrice())
				/ (mChartVO.getMaxPrice() - mChartVO.getMinPrice())) * (canvas
				.getHeight()));
		if (null != lineData) {
			for (int j = 0; j < lineData.size(); j++) {
				ptFirst = new PointF(valueX, valueY);
				valueX = valueX + lineLength;
				if (j < lineData.size() - 1) {
					valueY = (float) ((1f - (lineData.get(j + 1).floatValue() - mChartVO
							.getMinPrice())
							/ (mChartVO.getMaxPrice() - mChartVO.getMinPrice())) * (canvas
							.getHeight()));
					canvas.drawLine(ptFirst.x, ptFirst.y, valueX, valueY,
							linePaint);
				} else {
					if (isUp) {
						count += (0.3 * RATIO);
					} else {
						count -= (0.3 * RATIO);
					}
					float curPrice = lineData.get(lineData.size() - 1)
							.floatValue();
					valueY = (float) ((1f - (curPrice - mChartVO.getMinPrice())
							/ (mChartVO.getMaxPrice() - mChartVO.getMinPrice())) * (canvas
							.getHeight()));
					// canvas.drawLine(ptFirst.x, ptFirst.y, valueX, valueY,
					// linePaint);
					canvas.drawLine(0, valueY, valueX - lineLength, valueY,
							horizontalLinePaint);
					textPaint.setColor(Color.RED);
					canvas.drawCircle(valueX - lineLength, valueY, count,
							textPaint);
					if (count > (5 * RATIO)) {
						isUp = false;
					} else if (count <= (3 * RATIO)) {
						isUp = true;
					}
					textPaint.setColor(Color_Blue2);
					canvas.drawRect(canvas.getWidth() - (65 * RATIO), valueY
							+ (12 * RATIO), canvas.getWidth() - (5 * RATIO),
							valueY - (8 * RATIO), textPaint);
					textPaint.setColor(Color.RED);
					canvas.drawText(curPrice + "", canvas.getWidth()
							- (60 * RATIO), valueY + (8 * RATIO), textPaint);
					textPaint.setColor(Color.BLACK);
				}
			}
		}
	}

	private void drawCandle(Canvas canvas, MACandleChartVO mCandleChartVO) {
		if (null == mCandleChartVO) {
			return;
		}
		if (null == mCandleChartVO.getXtitle()
				|| 0 == mCandleChartVO.getXtitle().size()) {
			return;
		}
		float stickWidth = ((float) canvas.getWidth() / 36) - 1f;
		float stickX = 1f - (canvas.getWidth() / mCandleChartVO.getXtitle()
				.size());

		List<OHLCEntity> ohlcList = mCandleChartVO.getValues();
		float maxPrice = mCandleChartVO.getMaxPrice();
		float minPrice = mCandleChartVO.getMinPrice();
		if (null != ohlcList && 0 < ohlcList.size()) {
			for (int i = 0; i < ohlcList.size(); ++i) {
				OHLCEntity ohlc = ohlcList.get(i);
				float openY = (float) ((1f - (ohlc.getOpen() - minPrice)
						/ (maxPrice - minPrice)) * (canvas.getHeight()));
				float highY = (float) ((1f - (ohlc.getHigh() - minPrice)
						/ (maxPrice - minPrice)) * (canvas.getHeight()));
				float lowY = (float) ((1f - (ohlc.getLow() - minPrice)
						/ (maxPrice - minPrice)) * (canvas.getHeight()));
				float closeY = (float) ((1f - (ohlc.getClose() - minPrice)
						/ (maxPrice - minPrice)) * (canvas.getHeight()));
				if (ohlc.getOpen() < ohlc.getClose()) {
					if (stickWidth >= 2f) {
						canvas.drawRect(stickX, closeY, stickX + stickWidth,
								openY, mPaintPositive);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintPositive);
				} else if (ohlc.getOpen() > ohlc.getClose()) {
					if (stickWidth >= 2f) {
						canvas.drawRect(stickX, openY, stickX + stickWidth,
								closeY, mPaintNegative);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintNegative);
				} else {
					if (stickWidth >= 2f) {
						canvas.drawLine(stickX, closeY, stickX + stickWidth,
								openY, mPaintPositive);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintPositive);
				}
				stickX = stickX + 1 + stickWidth;
			}
		}
	}

	private void drawX(Canvas canvas, List<String> xtitle) {
		if (null != xtitle && 1 < xtitle.size()) {
			float coordinateX = canvas.getWidth() / xtitle.size();
			float coordinateY = canvas.getHeight();
			float offset = 45 * RATIO;
			float postOffset = 10 * RATIO;
			for (int i = 0; i < xtitle.size(); i++) {
				if (0 == i) {
					continue;
				}
				// 画X坐标
				canvas.drawText(xtitle.get(i), coordinateX * i - offset,
						coordinateY - postOffset, textPaint);
				// 画竖线网格
				canvas.drawLine(coordinateX * i - (offset / 2), postOffset,
						coordinateX * i - (offset / 2), coordinateY
								- (2 * postOffset), gridPaint);
			}
		}
	}

	private void drawY(Canvas canvas, List<String> ytitle) {
		if (null != ytitle && 1 < ytitle.size()) {
			float width = canvas.getWidth();
			float height = canvas.getHeight();
			float postOffset = height / ytitle.size();
			float offset = width - 60 * RATIO;
			for (int i = 0; i < ytitle.size(); i++) {
				// 画横线
				if (0 != i) {
					canvas.drawLine(0, height - i * postOffset, width, height
							- i * postOffset, gridPaint);
				}
				// 画Y轴坐标
				if (i < ytitle.size() && 0 < i) {
					canvas.drawText(ytitle.get(i), offset, height - i
							* postOffset, textPaint);
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		startDataTimer();
		startDrawTimer();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		stopDrawTimer();
		stopDataTimer();
	}

	private void handleData(DataVo response) {
		if (null != response) {
			if (1 == lineType) {
				handleLineData(response);
			} else {
				handleCandleData(response);
			}
		}
	}

	private void handleLineData(DataVo response) {
		DataVo dataVo = response;
//		DataVo dataVo = null;
//		try {
//			dataVo = new Gson().fromJson(response, DataVo.class);
//		} catch (Exception e) {
//
//		}
		if (null == dataVo) {
			return;
		}
		if (null == dataVo.getObject() || dataVo.getObject().isEmpty()) {
			return;
		}
		String[] objects = dataVo.getObject().split("\\|");
		if (45 > objects.length) {
			return;
		}

		MAChartVO mChartVO = new MAChartVO();
		List<String> timeList = new ArrayList<String>();
		List<Float> values = new ArrayList<Float>();
		for (int i = objects.length - 44; i < objects.length; ++i) {
			String[] object = objects[i].split(",");
			if (object.length > 3) {
				return;
			}
			timeList.add(object[0]);
			values.add(Float.valueOf(object[1]));
		}
		Float curPrice  = 0f;
		if (!values.isEmpty()) {
			curPrice = values.get(0);
		}
		values.add(curPrice);
//		values.add(curentPrice);
		List<String> xtitle = new ArrayList<String>();
		for (int i = 6; i >= 0; --i) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(timeList.get(i * 7).substring(8, 12));
			buffer.insert(2, " : ");
			xtitle.add(buffer.toString());
		}
		if (null == xtitle || 0 >= xtitle.size()) {
			return;
		}

		float maxPrice = 0.0f;
		for (int i = 7; i < values.size(); ++i) {
			maxPrice = Math.max(maxPrice, values.get(i));
		}
		float minPrice = maxPrice;
		for (int i = 7; i < values.size(); ++i) {
			minPrice = Math.min(minPrice, values.get(i));
		}
		float ave = (maxPrice - minPrice) / 4;
		List<String> ytitle = new ArrayList<String>();
		ytitle.add("");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 1)) + "");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 2)) + "");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 3)) + "");
		maxPrice += ave;
		minPrice -= ave;
		mChartVO.setXtitle(xtitle);
		mChartVO.setAvePrice(ave);
		mChartVO.setMaxPrice(maxPrice);
		mChartVO.setMinPrice(minPrice);
		mChartVO.setYtitle(ytitle);
		mChartVO.setValues(values);
		setmLineChart(mChartVO);
	}

	private void handleCandleData(DataVo response) {
		DataVo data = response;
//		DataVo data = null;
//		try {
//			data = new Gson().fromJson(response, DataVo.class);
//		} catch (Exception e) {
//
//		}
		if (null == data) {
			return;
		}
		if (null == data.getObject() || data.getObject().isEmpty()) {
			return;
		}
		List<OHLCEntity> ohlcList = new ArrayList<OHLCEntity>();
		String[] objects = data.getObject().split("\\|");
		if (36 > objects.length) {
			for (int i = 0; i < objects.length; ++i) {
				String[] arrayData = objects[i].split(",");
				if (arrayData.length < 5) {
					return;
				}
				double open = Double.parseDouble(arrayData[3]);
				double high = Double.parseDouble(arrayData[5]);
				double low = Double.parseDouble(arrayData[6]);
				double close = Double.parseDouble(arrayData[4]);
				String date = arrayData[1].substring(8, 12);
				ohlcList.add(new OHLCEntity(open, high, low, close, date));
			}
		} else {
			for (int i = 0; i < 36; ++i) {
				String[] arrayData = objects[i].split(",");
				if (arrayData.length < 5) {
					return;
				}
				double open = Double.parseDouble(arrayData[3]);
				double high = Double.parseDouble(arrayData[5]);
				double low = Double.parseDouble(arrayData[6]);
				double close = Double.parseDouble(arrayData[4]);
				String date = arrayData[1].substring(8, 12);

				ohlcList.add(new OHLCEntity(open, high, low, close, date));
			}
		}
		Collections.reverse(ohlcList);

		List<String> xtitle = new ArrayList<String>();
		if (0 < ohlcList.size()) {
			for (int i = 0; i < 7; ++i) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(ohlcList.get(i * (ohlcList.size() / 7)).getDate());
				buffer.insert(2, " : ");
				xtitle.add(buffer.toString());
			}
		}
		if (null == xtitle || 0 >= xtitle.size()) {
			return;
		}

		float maxPrice = 0f;
		if (null != ohlcList) {
			for (OHLCEntity e : ohlcList) {
				maxPrice = Math.max(maxPrice, (float) e.getHigh());
			}
		}
		float minPrice = maxPrice;
		if (null != ohlcList) {
			for (OHLCEntity e : ohlcList) {
				minPrice = Math.min(minPrice, (float) e.getLow());
			}
		}
		float ave = (maxPrice - minPrice) / 4;
		List<String> ytitle = new ArrayList<String>();
		ytitle.add("");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 1)) + "");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 2)) + "");
		ytitle.add(DecimalTools.formatFloatThree((minPrice + ave * 3)) + "");
		maxPrice += ave;
		minPrice -= ave;
		MACandleChartVO mCandleChart = new MACandleChartVO();
		mCandleChart.setAvePrice(ave);
		mCandleChart.setMaxPrice(maxPrice);
		mCandleChart.setMinPrice(minPrice);
		mCandleChart.setXtitle(xtitle);
		mCandleChart.setYtitle(ytitle);
		mCandleChart.setValues(ohlcList);
		setmCandleChart(mCandleChart);
	}

	private void startDrawTimer() {
		if (null != drawTimer || null != drawTimerTask) {
			stopDrawTimer();
		}
		drawTimer = new Timer();
		drawTimerTask = new TimerTask() {
			@Override
			public void run() {
				draw();
			}
		};
		drawTimer.schedule(drawTimerTask, 0, REFRESH_DRAW_TIME);
	}

	private void startDataTimer() {
		Log.i("zyu", "startDataTimer");
		if (null != dataTimer || null != dataTimerTask) {
			stopDataTimer();
		}
		dataTimer = new Timer();
		dataTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (NetTools.isWiFiState(getContext())) {
					if (StringUtil.StrIsNull(code)) {
						return;
					}
					String url = null;
					if (1 == lineType) {
						String time = null;
						if (8 > DateTools.getCurrentHour()) {
							time = DateTools.getLastDay();
						} else {
							time = DateTools.getCurrentData();
						}
						url = Config.GetTimeLine + "?contract=" + code
								+ "&quotedate=" + time;
						if (flag) {
							flag = false;
							requestTimeLine();
						}
					} else {
						url = Config.GetKLine + "?contract=" + code + "&type="
								+ lineType;
						if (flag) {
							flag = false;
							requestCandleLine(lineType + "");
						}
					}
				}
			}
		};
		dataTimer.schedule(dataTimerTask, 0, HttpConstant.REFRESH_TIME);
	}

	private void requestTimeLine() {
		if (TextUtils.isEmpty(mContractType)) {
			return;
		}
//		Call<DataVo> repos = mDataLineApi.listTimeLine("AG", "60");
//		repos.enqueue(new retrofit2.Callback<DataVo>() {
//			@Override
//			public void onResponse(Call<DataVo> call, Response<DataVo> response) {
//				DataVo data = response.body();
//				handleData(data);
//				Log.i("zyu", data.toString());
//				flag = true;
//			}
//			@Override
//			public void onFailure(Call<DataVo> call, Throwable t) {
//				flag = true;
//				t.printStackTrace();
//			}
//		});
		Call<DataVo> respon = mDataLineApi.listTLine(getTLineParams());
		respon.enqueue(new retrofit2.Callback<DataVo>() {
			@Override
			public void onResponse(Call<DataVo> call, Response<DataVo> response) {
				DataVo data = response.body();
				handleData(data);
//				Log.i("zyu", data.toString());
				flag = true;
			}
			@Override
			public void onFailure(Call<DataVo> call, Throwable t) {
				flag = true;
				t.printStackTrace();
			}
		});
	}

	private Map<String, String> getTLineParams() {
		final Map<String, String> params = ParamsUtil.getCommonParams();
		params.put("method", "gdiex.market.timeLine");
		params.put("contract", mContractType);
		params.put("number", "180");
		params.put("quotedate", String.valueOf(System.currentTimeMillis()));
		params.put("sign", ParamsUtil.sign(params));
		return params;
	}


	private void requestCandleLine(String type) {
		if (TextUtils.isEmpty(mContractType)) {
			return;
		}
//		Call<DataVo> repos = mDataLineApi.listCandleLine("AG", type, "60");
//		repos.enqueue(new retrofit2.Callback<DataVo>() {
//			@Override
//			public void onResponse(Call<DataVo> call, Response<DataVo> response) {
//				DataVo data = response.body();
//				handleData(data);
//				Log.i("zyu", data.toString());
//				flag = true;
//			}
//			@Override
//			public void onFailure(Call<DataVo> call, Throwable t) {
//				flag = true;
//				t.printStackTrace();
//			}
//		});
		Call<DataVo> respon = mDataLineApi.listKLine(getKLineParams(type));
		respon.enqueue(new retrofit2.Callback<DataVo>() {
			@Override
			public void onResponse(Call<DataVo> call, Response<DataVo> response) {
				DataVo data = response.body();
				handleData(data);
				Log.i("zyu", data.toString());
				flag = true;
			}
			@Override
			public void onFailure(Call<DataVo> call, Throwable t) {
				flag = true;
				t.printStackTrace();
			}
		});
	}

	private Map<String, String> getKLineParams(String type) {
		final Map<String, String> params = ParamsUtil.getCommonParams();
		params.put("method", "gdiex.market.kLine");
		params.put("contract", mContractType);
		params.put("number", "180");
		params.put("type", type);
		params.put("sign", ParamsUtil.sign(params));
		return params;
	}



	private void stopDrawTimer() {
		if (null != drawTimer) {
			drawTimer.cancel();
			drawTimer = null;
		}
		if (null != drawTimerTask) {
			drawTimerTask.cancel();
			drawTimerTask = null;
		}
	}

	private void stopDataTimer() {
		if (null != dataTimer) {
			dataTimer.cancel();
			dataTimer = null;
		}
		if (null != dataTimerTask) {
			dataTimerTask.cancel();
			dataTimerTask = null;
		}
	}

	public MAChartVO getmLineChart() {
		return mLineChart;
	}

	public void setmLineChart(MAChartVO mLineChart) {
		this.mLineChart = mLineChart;
	}

	public MACandleChartVO getmCandleChart() {
		return mCandleChart;
	}

	public void setmCandleChart(MACandleChartVO mCandleChart) {
		this.mCandleChart = mCandleChart;
	}

	public void setLineType(int lineType) {
		this.lineType = lineType;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCurentPrice(float price) {
		this.curentPrice = price;
	}

}

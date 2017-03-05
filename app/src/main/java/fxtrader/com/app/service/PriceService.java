package fxtrader.com.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.LogZ;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/18.
 */
public class PriceService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        startDataTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDataTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer dataTimer = null;
    private TimerTask dataTimerTask = null;

    private void startDataTimer() {
        Log.i("zyu", "startDataTimer");
        if (null != dataTimer || null != dataTimerTask) {
            stopDataTimer();
        }
        dataTimer = new Timer();
        dataTimerTask = new TimerTask() {
            @Override
            public void run() {
                getMarketPrice();
            }
        };
        dataTimer.schedule(dataTimerTask, 0, HttpConstant.REFRESH_TIME);
    }

    private void getMarketPrice() {
        ContractApi dataApi = RetrofitUtils.createJsonApi(ContractApi.class);
        Call<ResponseBody> response = dataApi.rates(getMarketParams());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = null;
                MarketEntity vo = new MarketEntity();
                try {
                    json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    vo.setSuccess(jsonObject.optBoolean("success"));
                    vo.setMessage(jsonObject.optString("message"));
                    vo.setCode(jsonObject.optInt("code"));

                    JSONObject object = jsonObject.getJSONObject("object");
                    vo.setPriceJson(object.toString());
                    vo.init();
                    sendContentBroadcast(vo);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogZ.i("price = " + json);
//                MarketEntity vo = new MarketEntity();
//                if (vo != null) {
//                    vo.init();
//                    sendContentBroadcast(vo);
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private Map<String, String> getMarketParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.market.rates");
        params.put("sign", ParamsUtil.sign(params));
        return params;
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

    /**
     * 发送广播
     * @param price
     */
    protected void sendContentBroadcast(MarketEntity price) {
        Intent intent=new Intent();
        intent.setAction(IntentItem.ACTION_PRICE);
        intent.putExtra(IntentItem.PRICE, price);
        sendBroadcast(intent);
    }
}

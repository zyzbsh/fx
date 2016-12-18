package fxtrader.com.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
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
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        Call<MarketEntity> response = dataApi.rates(getMarketParams());
        response.enqueue(new Callback<MarketEntity>() {
            @Override
            public void onResponse(Call<MarketEntity> call, Response<MarketEntity> response) {
                MarketEntity vo = response.body();
                vo.init();
                sendContentBroadcast(vo);
            }

            @Override
            public void onFailure(Call<MarketEntity> call, Throwable t) {
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
        intent.setAction("fxtrader.com.app.price");
        intent.putExtra("price", price);
        sendBroadcast(intent);
    }
}

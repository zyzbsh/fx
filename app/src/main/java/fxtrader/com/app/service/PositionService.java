package fxtrader.com.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.R;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 建仓列表
 * Created by pc on 2016/12/18.
 */
public class PositionService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LogZ.i("start position service");
        startPositionTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopPositionTimer();
        startPositionTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPositionTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer positionTimer = null;
    private TimerTask positionTimerTask = null;

    private void startPositionTimer() {
        if (null != positionTimer || null != positionTimerTask) {
            stopPositionTimer();
        }
        positionTimer = new Timer();
        positionTimerTask = new TimerTask() {
            @Override
            public void run() {
                getPositionList();
            }
        };
        positionTimer.schedule(positionTimerTask, 0, HttpConstant.REFRESH_POSITION_LIST);
    }

    private void stopPositionTimer() {
        if (null != positionTimer) {
            positionTimer.cancel();
            positionTimer = null;
        }
        if (null != positionTimerTask) {
            positionTimerTask.cancel();
            positionTimerTask = null;
        }
    }

    private void getPositionList() {
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        String token = ParamsUtil.getToken();
        Call<PositionListEntity> respon = dataApi.positionList(token, getPositionListParams());
        respon.enqueue(new Callback<PositionListEntity>() {
            @Override
            public void onResponse(Call<PositionListEntity> call, Response<PositionListEntity> response) {
                PositionListEntity entity = response.body();
                List<PositionInfoEntity> list = entity.getObject().getContent();
                sendContentBroadcast(list);
            }

            @Override
            public void onFailure(Call<PositionListEntity> call, Throwable t) {
            }
        });
    }

    private Map<String, String> getPositionListParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.list");
        params.put("sale", "false");
        params.put("page", "0");
        params.put("size", String.valueOf(Integer.MAX_VALUE));
        params.put("sort", "buyingDate,DESC");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    /**
     * 发送广播
     * @param
     */
    protected void sendContentBroadcast(List<PositionInfoEntity> list) {
        Intent intent=new Intent();
        intent.setAction(IntentItem.ACTION_POSITION_LIST);
        intent.putExtra(IntentItem.POSITION_LIST, (Serializable) list);
        sendBroadcast(intent);
    }
}

package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.SubscribeListEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/12/28.
 */
public class MyFollowActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_follow_list);
        requestData();
    }

    private void requestData(){
        showProgressDialog();
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<SubscribeListEntity> request = communityApi.subscribes(token, getParams());
        request.enqueue(new Callback<SubscribeListEntity>() {
            @Override
            public void onResponse(Call<SubscribeListEntity> call, Response<SubscribeListEntity> response) {
                dismissProgressDialog();
                SubscribeListEntity entity = response.body();
                if (entity.isSuccess()) {

                } else {
                    showToastShort(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubscribeListEntity> call, Throwable t) {
                dismissProgressDialog();
                LogZ.e(t.toString());
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getSubscripts");
        params.put("customerId", LoginConfig.getInstance().getId());
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}

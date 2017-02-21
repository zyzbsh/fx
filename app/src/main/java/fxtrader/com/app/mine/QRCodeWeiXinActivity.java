package fxtrader.com.app.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.CheckOrgnIdResponse;
import fxtrader.com.app.entity.CouponDetailEntity;
import fxtrader.com.app.entity.RechargeUrlResponse;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2017/2/19.
 * http://blog.csdn.net/gao36951/article/details/41149049
 * http://blog.csdn.net/zhangphil/article/details/44917901
 * http://blog.csdn.net/coder_yao/article/details/52230289
 * http://blog.csdn.net/u012702547/article/details/51501350
 */
public class QRCodeWeiXinActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_qr_code_wx);
        checkOrganId();
    }

    private void checkOrganId(){
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<CheckOrgnIdResponse> request = userApi.spdbScanPaySwitch(ParamsUtil.getToken(), getCheckParams());
        request.enqueue(new Callback<CheckOrgnIdResponse>() {
            @Override
            public void onResponse(Call<CheckOrgnIdResponse> call, Response<CheckOrgnIdResponse> response) {
                CheckOrgnIdResponse result = response.body();
                if (result != null && result.isObject()) {
                    getRechargeUrl();
                } else {
                    dismissProgressDialog();
                    showToastShort("该挂会没有开启支付功能");
                }
            }

            @Override
            public void onFailure(Call<CheckOrgnIdResponse> call, Throwable t) {
                dismissProgressDialog();
                if (t != null){
                    LogZ.e(t.getMessage());
                }
            }
        });
    }

    private Map<String, String> getCheckParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.currency.spdbScanPaySwitch");
        params.put("organ_id", AppApplication.getInstance().getUser().getObject().getOrganId() + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getRechargeUrl(){
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<RechargeUrlResponse> request = userApi.rechargeSpdbScanPay(ParamsUtil.getToken(), getCheckParams());
        request.enqueue(new Callback<RechargeUrlResponse>() {
            @Override
            public void onResponse(Call<RechargeUrlResponse> call, Response<RechargeUrlResponse> response) {
                RechargeUrlResponse result = response.body();
                if (result != null) {
                    generateBitmap(result.getObject(), 200, 200);
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<RechargeUrlResponse> call, Throwable t) {
                dismissProgressDialog();
                if (t != null){
                    LogZ.e(t.getMessage());
                }
            }
        });
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}

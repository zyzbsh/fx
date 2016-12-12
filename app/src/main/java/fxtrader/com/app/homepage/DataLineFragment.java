package fxtrader.com.app.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.NewLineView;
import fxtrader.com.app.view.NewMAChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/12/9.
 */
public class DataLineFragment extends BaseFragment{

    private NewLineView mNiewLineView;

    private TextView mContractTv;

    private TextView mContractPercentTv;

    private TextView mContractProfitTv;

    private TextView mHighestTv;

    private TextView mLowestTv;

    private TextView mYesterdayCloseTv;

    private TextView mTodayOpenTv;

    private NewMAChartView mChartView;

    private ContractInfoEntity mContact;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_data_line, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContractLayout(view);
        initChartView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mContact = (ContractInfoEntity) bundle.getSerializable(IntentItem.CONTARCT_INFO);
        if (mContact != null) {

        }
    }

    private void initContractLayout(View view) {
        mContractTv = (TextView) view.findViewById(R.id.homepage_contract_tv);
        mContractPercentTv = (TextView) view.findViewById(R.id.homepage_contract_percent_tv);
        mContractProfitTv = (TextView) view.findViewById(R.id.homepage_contract_profit_tv);
        mHighestTv = (TextView) view.findViewById(R.id.homepage_highest_tv);
        mLowestTv = (TextView) view.findViewById(R.id.homepage_lowest_tv);
        mYesterdayCloseTv = (TextView) view.findViewById(R.id.homepage_yesterday_open_tv);
        mTodayOpenTv = (TextView) view.findViewById(R.id.homepage_today_open_tv);
    }

    private void initChartView(View view) {
        mChartView = (NewMAChartView) view.findViewById(R.id.view_new_machart);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mChartView.getLayoutParams();
        int screenWidth = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity());
        int pad = UIUtil.dip2px(getContext(), 35);
        int width = screenWidth - 2 * pad;
        lp.width = width;
        lp.height = width * 4 / 5;
        mChartView.setLayoutParams(lp);
    }

    public String getDataType(){
        if (mContact == null) {
            return "";
        }
        return mContact.getDataType();
    }

    public void setLineType(int lineType) {
        mChartView.setLineType(lineType);
    }


    public void setPriceTvs(String data) {
        try {
            String[] a = data.split(",");
            String latestPrice = a[0];
            String openPrice = a[1];
            String lastClosePrice = a[2];
            String highestPrice = a[3];
            String lowestPrice = a[4];
            boolean isMarketOpen = Boolean.parseBoolean(a[5]);
            mContractProfitTv.setText(latestPrice);
            mTodayOpenTv.setText(getString(R.string.today_open_num, openPrice));
            mYesterdayCloseTv.setText(getString(R.string.yesterday_close_num, lastClosePrice));
            mHighestTv.setText(getString(R.string.highest_num, highestPrice));
            mLowestTv.setText(getString(R.string.lowest_num, lowestPrice));
        } catch(Exception e) {
            LogZ.e(e);
        }
    }

}

package fxtrader.com.app.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.TicketsHelper;
import fxtrader.com.app.db.helper.UserCouponsHelper;
import fxtrader.com.app.entity.BuildPositionResponseEntity;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.CouponDetailEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.entity.TicketEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.ProfitAndLossView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 建仓
 * Created by pc on 2016/12/18.
 */
public class HFBuildPositionActivity extends BaseActivity implements View.OnClickListener{

    private static final int DEFAULT_DEAL_COUNT = 1;

    private BroadcastReceiver mPriceReceiver;

    private boolean mIsUp;

    private String mDataType = "";

    private ContractEntity mContract;

    private ContractInfoEntity mCurContractInfo;

    private int mTicketId = 1;

    private TextView mContractNameTv;

    private TextView mSpecificationTv;

    private TextView mHandChargeTv;

    private TextView mProfitAndLossTv;

    private TextView mLatestPriceTv;

    private TextView mTargetPriceTv;

    private TextView mStopLossPriceTv;

    private TextView mOkTv;

    private SeekBar mSeekBar;

    private TextView mTradeCountTv;

    private int mDealCount = DEFAULT_DEAL_COUNT;

    private int mTicketCount = 0;

    private CheckBox mCouponCb;

    private TextView mCouponNumTv;

    private List<CouponDetailEntity> mCoupons;

    private List<TicketEntity> mTickets;

    private List<CouponDetailEntity> mValidCoupons;

    private TextView mMarginTv;

    private TextView mFeeTv;

    private String mLatestPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_hf_build_position);
        mLatestPrice = getIntent().getStringExtra(IntentItem.PRICE);
        mIsUp = getIntent().getBooleanExtra(IntentItem.EXCEPTION, false);
        mDataType = getIntent().getStringExtra(IntentItem.DATA_TYPE);
        LogZ.i("latestPrice = " + mLatestPrice + ", mIsUp = " + mIsUp + ", mDataType = " + mDataType);
        mContract = (ContractEntity) getIntent().getSerializableExtra(IntentItem.CONTARCT_INFO);
        if (mContract.hasData()) {
            mCurContractInfo = mContract.getData().get(0);
        } else {
            return;
        }
        mCoupons = UserCouponsHelper.getInstance().getData();
        mTickets = TicketsHelper.getInstance().getData();
        setValidCoupons(mCurContractInfo);
        initParams();
        initTitle();
        initContractInfo();
        initInfoRec();
        initSeekBar();
        initCouponLayout();
        initMarginLayout();
        initOkTv();

        setContractInfoLayout(mCurContractInfo);
        setPriceLayout(mLatestPrice);
        setMarginLayout();


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPriceReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPriceReceiver!=null) {
            unregisterReceiver(mPriceReceiver);
        }
    }

    private void initParams() {
        NestedScrollView layout = (NestedScrollView) findViewById(R.id.dialog_build_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(this, 18);
        int h = UIUtil.dip2px(this, 50);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        params.height = UIUtil.getScreenHeight(AppApplication.getInstance().getActivity()) - h;
        layout.setLayoutParams(params);
    }

    private void setValidCoupons(ContractInfoEntity contractInfoEntity){
        if (mValidCoupons == null) {
            mValidCoupons = new ArrayList<>();
        } else {
            mValidCoupons.clear();
        }
        String dataType = contractInfoEntity.getDataType();
        int margin = contractInfoEntity.getMargin();
        LogZ.i("tickets: " + mTickets.toString());
        LogZ.i("coupons: " + mCoupons.toString());
        for(TicketEntity ticket : mTickets) {
            int value = ticket.getValue();
            if (value == margin && ticket.containDataType(dataType)) {
                int id = ticket.getId();
                for (CouponDetailEntity coupon : mCoupons) {
                    if (coupon.getTicketId() == id) {
                        if (coupon.getLastUseTime() > System.currentTimeMillis()) {
                            mValidCoupons.add(coupon);
                            LogZ.i("valid :  " + coupon.toString());
                        }
                    }
                }
            }
        }
    }

    private void initTitle() {
        TextView buyTv = (TextView) findViewById(R.id.dialog_build_tv);
        if (mIsUp) {
            buyTv.setText(R.string.buy_up);
            buyTv.setBackgroundColor(getCompactColor(R.color.red_text));
        } else {
            buyTv.setText(R.string.buy_down);
            buyTv.setBackgroundColor(getCompactColor(R.color.green));
        }
        mContractNameTv = (TextView) findViewById(R.id.dialog_build_contract_name_tv);
        mContractNameTv.setText(getString(R.string.build_position_contract_name, mContract.getName()));
    }

    private void initContractInfo() {
        mSpecificationTv = (TextView) findViewById(R.id.dialog_build_specification_tv);
        mHandChargeTv = (TextView) findViewById(R.id.dialog_build_unit_tv);
        mProfitAndLossTv = (TextView) findViewById(R.id.dialog_build_profit_and_loss_tv);
        mLatestPriceTv = (TextView) findViewById(R.id.dialog_build_price_latest_tv);
        mTargetPriceTv = (TextView) findViewById(R.id.dialog_build_price_target_tv);
        mStopLossPriceTv = (TextView) findViewById(R.id.dialog_build_price_stop_loss_tv);
    }

    private void initInfoRec() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_buy_rec);
        ViewGroup.LayoutParams mParams = recyclerView.getLayoutParams();
        final int space = UIUtil.dip2px(this, 2);
        int raw = getData().size() / 2 + getData().size() % 2;
        mParams.height = UIUtil.dip2px(this, 70) * raw + space * 2;
        recyclerView.setLayoutParams(mParams);
        final CustomAdapter adapter = new CustomAdapter(this, getData());
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ContractInfoEntity data, int position) {
                setContractInfoLayout(data);
//                mDealCount = DEFAULT_DEAL_COUNT;
//                mTradeCountTv.setText(String.valueOf(mDealCount));
//                mSeekBar.setMax(data.getDealLimit() * 10);
//                mSeekBar.setProgress(0);
                setMarginLayout();
                setCouponLayout(data);
            }
        });

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left= space;
                outRect.right= space;
                //注释这两行是为了上下间距相同
//        if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
//        }
            }
        });
    }

    private void initCouponLayout(){
        mCouponCb = (CheckBox) findViewById(R.id.build_position_coupon_cb);
        mCouponNumTv = (TextView) findViewById(R.id.build_position_coupon_num_tv);
        if (mValidCoupons != null && !mValidCoupons.isEmpty()) {
            mCouponCb.setClickable(true);
        } else {
            mCouponCb.setClickable(false);
        }
        mCouponNumTv.setText(String.valueOf(mCoupons.size()));

    }

    private void setCouponCb(boolean enabled){
        mCouponCb.setClickable(enabled);
    }

    private void setCouponNumTv() {
        mCouponNumTv.setText(String.valueOf(mCoupons.size()));
    }

    private void initMarginLayout() {
        mMarginTv = (TextView) findViewById(R.id.dialog_buy_deposit_tv);
        mFeeTv = (TextView) findViewById(R.id.dialog_buy_fee_tv);
    }

    private void setContractInfoLayout(ContractInfoEntity info) {
        mCurContractInfo = info;
        mSpecificationTv.setText(info.getBaseNum() + info.getBaseUnit());
        mHandChargeTv.setText(getString(R.string.margin_num, info.getMargin()));
        mProfitAndLossTv.setText(String.valueOf(info.getPlRate()));
    }

    private void setPriceLayout(String price) {
        if (mCurContractInfo == null || TextUtils.isEmpty(price)) {
            return;
        }
        mLatestPriceTv.setText(price);
        setTargetAndStopLossTv(price);
    }

    private void setTargetAndStopLossTv(String price) {
        double latestPrice = Double.parseDouble(price);
        double profitAndLoss = mCurContractInfo.getPlRate() * mCurContractInfo.getPlUnit();

        double stopPercent = mCurContractInfo.getProfitAndLoss();
        if (stopPercent < 10E-6) {
            mTargetPriceTv.setText("");
        } else {
            double profit = mCurContractInfo.getMargin() * stopPercent;
            double targetPrice;
            if (mIsUp) {
                targetPrice = latestPrice  + profit / profitAndLoss;
            } else {
                targetPrice = latestPrice  - profit / profitAndLoss;
            }
            BigDecimal b = new BigDecimal(targetPrice);
            double f = b.setScale(1, BigDecimal.ROUND_CEILING).doubleValue();
            mTargetPriceTv.setText(String.valueOf(f));
        }

        if (stopPercent < 10E-6 || stopPercent >=1) {
            mStopLossPriceTv.setText("");
        }else {
            double loss = mCurContractInfo.getMargin() * stopPercent;
            double stopLossPrice;
            if (mIsUp) {
                stopLossPrice = latestPrice  - loss / profitAndLoss;
            } else {
                stopLossPrice = latestPrice  + loss / profitAndLoss;
            }
            BigDecimal b = new BigDecimal(stopLossPrice);
            double f = b.setScale(1, BigDecimal.ROUND_CEILING).doubleValue();
            mStopLossPriceTv.setText(String.valueOf(f));
        }
    }

    private void setCouponLayout(ContractInfoEntity contractInfoEntity) {
        setValidCoupons(contractInfoEntity);
        if (mValidCoupons != null && !mValidCoupons.isEmpty()) {
            mCouponCb.setClickable(true);
        } else {

        }
    }


    private void initSeekBar() {
        mTradeCountTv = (TextView) findViewById(R.id.dialog_build_trade_num_tv);
        mTradeCountTv.setText(String.valueOf(mDealCount));
        OnSeekbarClickedListener listener = new OnSeekbarClickedListener();
        findViewById(R.id.dialog_buy_minus_btn).setOnClickListener(listener);
        findViewById(R.id.dialog_buy_plus_btn).setOnClickListener(listener);
        mSeekBar = (SeekBar) findViewById(R.id.dialog_buy_seek_bar);
        mSeekBar.setMax((mCurContractInfo.getDealLimit() - DEFAULT_DEAL_COUNT)* 10);
        final int stoped;
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int num = progress / 10;
                int temp = progress % 10;
                if (temp == 0) {
                    mDealCount = num + DEFAULT_DEAL_COUNT;
                } else if (temp < 5) {
                    seekBar.setProgress(num * 10);
                    mDealCount = num + DEFAULT_DEAL_COUNT;
                } else if (temp > 5) {
                    seekBar.setProgress((num + 1) * 10);
                    mDealCount = num + 1 + DEFAULT_DEAL_COUNT;
                }
                mTradeCountTv.setText(String.valueOf(mDealCount));
                setMarginLayout();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int temp = progress % 10;
                if (temp == 0) {

                } else if (temp < 5) {

                } else if (temp > 5) {

                }

            }
        });
    }

    private class OnSeekbarClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_buy_minus_btn:
                    if (mDealCount != DEFAULT_DEAL_COUNT) {
                        mDealCount--;
                        mSeekBar.setProgress((mDealCount - DEFAULT_DEAL_COUNT)* 10);
                        mTradeCountTv.setText(String.valueOf(mDealCount));
                        setMarginLayout();
                    }
                    break;
                case R.id.dialog_buy_plus_btn:
                    if (mDealCount != mCurContractInfo.getDealLimit()) {
                        mDealCount++;
                        mSeekBar.setProgress((mDealCount - DEFAULT_DEAL_COUNT) * 10);
                        mTradeCountTv.setText(String.valueOf(mDealCount));
                        setMarginLayout();
                    }
                default:
                    break;
            }
        }
    }

    private void initOkTv() {
        mOkTv = (TextView) findViewById(R.id.dialog_buy_build_position_tv);
        mOkTv.setOnClickListener(this);
        findViewById(R.id.dialog_buy_cancel_tv).setOnClickListener(this);
    }

    private void registerPriceReceiver() {
        mPriceReceiver = new PriceReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_PRICE);
        registerReceiver(mPriceReceiver, filter);
    }


    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity entity = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            entity.init();
            String data = entity.getData(mDataType);
            PriceEntity price = new PriceEntity(data);
            setPriceLayout(price.getLatestPrice());
        }
    }

    private void setMarginLayout() {
        String margin = String.valueOf(mCurContractInfo.getMargin() * mDealCount);
        double handCharge = mCurContractInfo.getHandingCharge() * mDealCount;
        BigDecimal b = new BigDecimal(handCharge);
        double f = b.setScale(1, BigDecimal.ROUND_CEILING).doubleValue();
        String fee = String.valueOf(f);
        mMarginTv.setText(getString(R.string.deposit_num, margin));
        mFeeTv.setText(getString(R.string.fee_num, fee));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_buy_build_position_tv:
                buildPosition();
                break;
            case R.id.dialog_buy_cancel_tv:
                finish();
                break;
            default:
                break;
        }
    }

    private void buildPosition(){
//        if (mDealCount > 0 && mTicketCount > 0) {
//            return;
//        }
        showProgressDialog();
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        Call<BuildPositionResponseEntity> respon = api.buildPosition(ParamsUtil.getToken(), getBuildPositionParams());
        respon.enqueue(new Callback<BuildPositionResponseEntity>() {
            @Override
            public void onResponse(Call<BuildPositionResponseEntity> call, Response<BuildPositionResponseEntity> response) {
                BuildPositionResponseEntity entity = response.body();
                if (entity.isSuccess()) {
                    PositionEntity positionEntity = entity.getObject();
                    dismissProgressDialog();
                } else {
                    dismissProgressDialog();
                    showToastLong(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BuildPositionResponseEntity> call, Throwable t) {
                LogZ.i(t.toString());
                showToastLong("建仓失败，请重试。");
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getBuildPositionParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.open");
        String direction = "";
        if (mIsUp) {
            direction = HttpConstant.DealDirection.UP;
        } else {
            direction = HttpConstant.DealDirection.DROP;
        }
        params.put("dealDirection", direction);
        int ticketId = 1;
        if (mValidCoupons != null && !mValidCoupons.isEmpty()) {
            for (CouponDetailEntity coupon : mValidCoupons) {
                if (coupon.getLastUseTime() < System.currentTimeMillis()) {
                    ticketId = coupon.getTicketId();
                }
            }
        }

        int dealCount = mDealCount;
        int ticketCount = 0;
        if (ticketId != 1) {
            dealCount = 0;
            ticketCount = 1;
        }
        params.put("dealCount", String.valueOf(dealCount));
        params.put("ticketCount", String.valueOf(ticketCount));
        params.put("code", mCurContractInfo.getCode());
        params.put("ticketId", String.valueOf(ticketId));
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
        private Context context;
        private List<ContractInfoEntity> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private MyViewHolder holder;
        private int layoutPosition;

        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, ContractInfoEntity data, int position);
        }

        public CustomAdapter(Context context, List<ContractInfoEntity> data) {
            this.context = context;
            this.data = data;
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_build_position_hf, parent, false);
            holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            ContractInfoEntity info = data.get(position);
            String percent = String.valueOf(ContractUtil.getDouble(info.getProfitAndLoss() * 100, 0)) + "%";
            holder.profitPercentTv.setText(percent);
            String fee = String.valueOf(info.getHandingCharge());
            String feeText = context.getString(R.string.fee_num, fee);
            holder.feeTv.setText(feeText);
            holder.itemView.setTag(info);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取当前点击的位置
                    layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, (ContractInfoEntity) holder.itemView.getTag(), layoutPosition);
                }
            });


            //更改状态
            if (position == layoutPosition) {
                holder.desTv.setTextColor(Color.WHITE);
                holder.profitPercentTv.setTextColor(Color.WHITE);
                holder.itemView.setBackgroundResource(R.mipmap.bg_build_position_item_clicked);
                holder.itemLayout.setBackgroundColor(Color.parseColor("#00000000"));
            } else {
                holder.desTv.setTextColor(Color.parseColor("#f6812e"));
                holder.profitPercentTv.setTextColor(Color.parseColor("#e83743"));
                holder.itemView.setBackgroundColor(Color.parseColor("#00000000"));
                holder.itemLayout.setBackgroundResource(R.mipmap.bg_build_position_item_default);
            }

            if (position == 0) {
                holder.discountTv.setVisibility(View.VISIBLE);
                holder.discountTv.setText("4折");
            } else if (position ==1) {
                holder.discountTv.setVisibility(View.VISIBLE);
                holder.discountTv.setText("6折");
            } else if (position == 2) {
                holder.discountTv.setVisibility(View.VISIBLE);
                holder.discountTv.setText("8折");
            } else {
                holder.discountTv.setVisibility(View.GONE);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView feeTv;
            private TextView desTv;
            private TextView profitPercentTv;
            private RelativeLayout itemLayout;
            private TextView discountTv;


            public MyViewHolder(View itemView) {
                super(itemView);
                feeTv = (TextView) itemView.findViewById(R.id.item_dialog_build_fee_tv);
                desTv = (TextView) itemView.findViewById(R.id.build_position_hf_profit_percent_des_tv);
                profitPercentTv = (TextView) itemView.findViewById(R.id.build_position_hf_profit_percent_tv);
                itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_dialog_build_layout);
                discountTv = (TextView) itemView.findViewById(R.id.item_build_position_hf_discount_tv);
            }
        }
    }

    private List<ContractInfoEntity> getData() {
        return mContract.getData();
    }
}

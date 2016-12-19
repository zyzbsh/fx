package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.FullyGridLayoutManager;
import fxtrader.com.app.entity.BuildPositionResponseEntity;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.CouponListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 建仓
 * Created by pc on 2016/11/19.
 */
public class BuildDialog extends Dialog implements View.OnClickListener{

    private boolean mIsUp;

    private ContractEntity mContract;

    private ContractInfoEntity mCurContractInfo;

    private int mTicketId = 1;

    private BuildPositionListener mBuildPositionListener;

    private TextView mSpecificationTv;

    private TextView mHandChargeTv;

    private TextView mProfitAndLossTv;

    private TextView mLatestPriceTv;

    private TextView mTargetPriceTv;

    private TextView mStopLossPriceTv;

    private TextView mOkTv;

    private SeekBar mSeekBar;

    private TextView mTradeCountTv;

    private int mDealCount = 0;

    private ProfitAndLossView mStopProfitView;

    private ProfitAndLossView mStopLossView;

    private int mTicketCount = 0;

    private String mLatestPrice;

    private DefaultProgressDialog mLoadingProgress;



    public interface BuildPositionListener {
        public void buildPosition();
    }

    public BuildDialog(Context context, String price, ContractEntity entity, boolean up) {
        super(context, R.style.BuyDialogTheme);
        this.setCanceledOnTouchOutside(false);
        mLatestPrice = price;
        mIsUp = up;
        mContract = entity;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_build);
        if (mContract.hasData()) {
            mCurContractInfo = mContract.getData().get(0);
        } else {
            return;
        }
        initParams();
        initTitle();
        initContractInfo();
        initInfoRec();
        initSeekBar();
        initProfitAndLoss();
        initOkTv();

        if (mContract.hasData()) {
            setContractInfoLayout(mCurContractInfo);
        }
    }

    private void initParams() {
        NestedScrollView layout = (NestedScrollView) findViewById(R.id.dialog_build_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 18);
        int h = UIUtil.dip2px(getContext(), 50);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        params.height = UIUtil.getScreenHeight(AppApplication.getInstance().getActivity()) - h;
        layout.setLayoutParams(params);
    }



    public void setBuildPositionListener(final BuildPositionListener listener) {
//        mOkTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//                listener.buildPosition();
//            }
//        });
    }


    private void initTitle() {
        TextView buyTv = (TextView) findViewById(R.id.dialog_build_tv);
        if (mIsUp) {
            buyTv.setText(R.string.buy_up);
            buyTv.setBackgroundColor(getColor(R.color.red_text));
        } else {
            buyTv.setText(R.string.buy_down);
            buyTv.setBackgroundColor(getColor(R.color.green));
        }
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
        final int space = UIUtil.dip2px(getContext(), 2);
        int raw = getData().size() / 2 + getData().size() % 2;
        mParams.height = UIUtil.dip2px(getContext(), 70) * raw + space * 2;
        recyclerView.setLayoutParams(mParams);
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData());
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(), 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ContractInfoEntity data, int position) {
                setContractInfoLayout(data);
                mDealCount = 0;
                mTradeCountTv.setText(String.valueOf(mDealCount));
                mSeekBar.setMax(data.getDealLimit() * 10);
                mSeekBar.setProgress(0);
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

    private void setContractInfoLayout(ContractInfoEntity info) {
        mCurContractInfo = info;
        mSpecificationTv.setText(info.getBaseNum() + info.getBaseUnit());
        mHandChargeTv.setText(getContext().getString(R.string.fee_num, info.getMargin()));
        mProfitAndLossTv.setText(String.valueOf(info.getPlRate()));
    }

    private void setPriceLayout(String price) {
        if (mCurContractInfo == null) {
            return;
        }
        mLatestPriceTv.setText(price);
        setTargetAndStopLossTv(price);
    }

    private void setTargetAndStopLossTv(String price) {
        double latestPrice = Double.parseDouble(price);
        double profitAndLoss = mCurContractInfo.getPlRate() * mCurContractInfo.getPlUnit();
        double profitAndLossMoney = profitAndLoss * mDealCount;
        double profit = mCurContractInfo.getMargin() * mDealCount * mStopProfitView.getPercent() / 10.0;
        double loss = mCurContractInfo.getMargin() * mDealCount * mStopLossView.getPercent() / 10.0;

        double targetPrice;
        double stopLossPrice;
        if (mIsUp) {
            targetPrice = latestPrice * (1 + profit / profitAndLossMoney);
            stopLossPrice = latestPrice * ( 1 - loss / profitAndLossMoney);
        } else {
            targetPrice = latestPrice * (1 - profit / profitAndLossMoney);
            stopLossPrice = latestPrice * ( 1 + loss / profitAndLossMoney);
        }
        mTargetPriceTv.setText(String.valueOf(targetPrice));
        mStopLossPriceTv.setText(String.valueOf(stopLossPrice));
    }


    private void initSeekBar() {
        mTradeCountTv = (TextView) findViewById(R.id.dialog_build_trade_num_tv);
        mTradeCountTv.setText(String.valueOf(mDealCount));
        OnSeekbarClickedListener listener = new OnSeekbarClickedListener();
        findViewById(R.id.dialog_buy_minus_btn).setOnClickListener(listener);
        findViewById(R.id.dialog_buy_plus_btn).setOnClickListener(listener);
        mSeekBar = (SeekBar) findViewById(R.id.dialog_buy_seek_bar);
        mSeekBar.setMax(mCurContractInfo.getDealLimit()* 10);
        final int stoped;
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int num = progress / 10;
                int temp = progress % 10;
                if (temp == 0) {
                    mDealCount = num;
                } else if (temp < 5) {
                    seekBar.setProgress(num * 10);
                    mDealCount = num;
                } else if (temp > 5) {
                    seekBar.setProgress((num + 1) * 10);
                    mDealCount = num + 1;
                }
                mTradeCountTv.setText(String.valueOf(mDealCount));
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
                    if (mDealCount != 0) {
                        mDealCount--;
                        mSeekBar.setProgress(mDealCount* 10);
                        mTradeCountTv.setText(String.valueOf(mDealCount));
                    }
                    break;
                case R.id.dialog_buy_plus_btn:
                    if (mDealCount != mCurContractInfo.getDealLimit()) {
                        mDealCount++;
                        mSeekBar.setProgress(mDealCount * 10);
                        mTradeCountTv.setText(String.valueOf(mDealCount));
                    }
                default:
                    break;
            }
        }
    }

    private void initProfitAndLoss() {
        mStopProfitView = (ProfitAndLossView) findViewById(R.id.dialog_build_stop_profit_layout);
        mStopLossView = (ProfitAndLossView) findViewById(R.id.dialog_build_stop_loss_layout);
        mStopProfitView.setTitle(R.string.stop_profit);
        mStopLossView.setTitle(R.string.stop_loss);
    }

    private void initOkTv() {
        mOkTv = (TextView) findViewById(R.id.dialog_buy_build_position_tv);
        mOkTv.setOnClickListener(this);
        findViewById(R.id.dialog_buy_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_buy_build_position_tv:
                buildPosition();
                break;
            default:
                break;
        }
    }

    private void buildPosition(){
        if (mDealCount > 0 && mTicketCount > 0) {
            return;
        }
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        Call<BuildPositionResponseEntity> respon = api.buildPosition(ParamsUtil.getToken(), getBuildPositionParams());
        respon.enqueue(new Callback<BuildPositionResponseEntity>() {
            @Override
            public void onResponse(Call<BuildPositionResponseEntity> call, Response<BuildPositionResponseEntity> response) {
                setProfitAndLoss();
            }

            @Override
            public void onFailure(Call<BuildPositionResponseEntity> call, Throwable t) {

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
        params.put("dealCount", "");
        params.put("ticketCount", "");
        params.put("code", "");
        params.put("ticketId", String.valueOf(mTicketId));
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setProfitAndLoss(){
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        Call<CommonResponse> respon = api.setProfitAndLoss(ParamsUtil.getToken(), getSetPositionParams());
        respon.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getSetPositionParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.open");
        params.put("storageId", "");
        params.put("profit", "");
        params.put("loss", "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    protected void showLoadingProgress(){
        LogZ.i("show loading progress");
        if (mLoadingProgress == null) {
            mLoadingProgress = new DefaultProgressDialog(getContext());
        }
        mLoadingProgress.show();
    }

    protected void dismissLoadingProgress(){
        if (mLoadingProgress != null && mLoadingProgress.isShowing()){
            mLoadingProgress.dismiss();
        }
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_dialog_build, parent, false);
            holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            ContractInfoEntity info = data.get(position);
            String fee = String.valueOf(info.getHandingCharge());
            String feeText = context.getString(R.string.fee_num, fee);
            holder.feeTv.setText(feeText);
            holder.specTv.setText(info.getBaseNum() + info.getBaseUnit());
            holder.marginTv.setText(String.valueOf(info.getMargin()));
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
                holder.specTv.setTextColor(Color.WHITE);
                holder.marginTv.setTextColor(Color.WHITE);
                holder.buildUnit.setTextColor(Color.WHITE);
                holder.itemView.setBackgroundResource(R.mipmap.bg_build_position_item_clicked);
                holder.itemLayout.setBackgroundColor(Color.parseColor("#00000000"));
            } else {
                holder.specTv.setTextColor(Color.parseColor("#f6812e"));
                int color = Color.parseColor("#e83743");
                holder.marginTv.setTextColor(color);
                holder.buildUnit.setTextColor(color);
                holder.itemView.setBackgroundColor(Color.parseColor("#00000000"));
                holder.itemLayout.setBackgroundResource(R.mipmap.bg_build_position_item_default);
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
            private TextView specTv;
            private TextView marginTv;
            private TextView buildUnit;
            private RelativeLayout itemLayout;


            public MyViewHolder(View itemView) {
                super(itemView);
                feeTv = (TextView) itemView.findViewById(R.id.item_dialog_build_fee_tv);
                specTv = (TextView) itemView.findViewById(R.id.item_dialog_build_spec_tv);
                marginTv = (TextView) itemView.findViewById(R.id.item_dialog_build_margin_tv);
                buildUnit = (TextView) itemView.findViewById(R.id.item_dialog_build_unit_tv);
                itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_dialog_build_layout);
            }
        }
    }

    private List<ContractInfoEntity> getData() {
        return mContract.getData();
    }

}

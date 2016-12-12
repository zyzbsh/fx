package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 买涨(跌)
 * Created by pc on 2016/11/19.
 */
public class BuyDialog extends Dialog implements View.OnClickListener{

    private boolean mIsUp;

    private BuildPositionListener mBuildPositionListener;

    private TextView mOkTv;

    private SeekBar mSeekBar;

    private int mNum = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_buy_minus_btn:
                if (mNum != 0) {
                    mNum--;
                    mSeekBar.setProgress(mNum * 10);
                }
                break;
            case R.id.dialog_buy_plus_btn:
                if (mNum != 10) {
                    mNum++;
                    mSeekBar.setProgress(mNum * 10);
                }
                break;
            case R.id.dialog_buy_build_position_tv:
                buildPosition();
                break;
            default:
                break;
        }
    }

    private void buildPosition(){
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        Call<CommonResponse> respon = api.buildPosition(ParamsUtil.getToken(), getBuildPositionParams());
        respon.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                setPosition();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getBuildPositionParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.open");
        params.put("dealCount", "");
        params.put("dealDirection", "");
        params.put("code", "");
        params.put("ticketId", "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setPosition(){
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        Call<CommonResponse> respon = api.setPosition(ParamsUtil.getToken(), getSetPositionParams());
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

    public interface BuildPositionListener {
        public void buildPosition();
    }

    public BuyDialog(Context context, ContractInfoEntity entity, boolean up) {
        super(context, R.style.BuyDialogTheme);
        mIsUp = up;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buy);
        setBuyTitle();
        initOkTv();
        setCancelTv();
        setInfoRec();
        setSeekBar();
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


    private void setBuyTitle() {
        TextView buyTv = (TextView) findViewById(R.id.dialog_buy_tv);
        if (mIsUp) {
            buyTv.setText(R.string.buy_up);
            buyTv.setBackgroundColor(getColor(R.color.red_text));
        } else {
            buyTv.setText(R.string.buy_down);
            buyTv.setBackgroundColor(getColor(R.color.green));
        }
    }

    private void initOkTv() {
        mOkTv = (TextView) findViewById(R.id.dialog_buy_build_position_tv);
        mOkTv.setOnClickListener(this);
    }


    private void setCancelTv() {
        findViewById(R.id.dialog_buy_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setInfoRec() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_buy_rec);
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TestInfo data, int position) {
//data为内容，position为点击的位置              ToastUtil.getShortToastByString(MainActivity.this,"data: "+data+",position: "+position);
            }
        });
    }

    private void setSeekBar() {
        findViewById(R.id.dialog_buy_minus_btn).setOnClickListener(this);
        findViewById(R.id.dialog_buy_plus_btn).setOnClickListener(this);
        mSeekBar = (SeekBar) findViewById(R.id.dialog_buy_seek_bar);
        mSeekBar.setMax(100);
        final int stoped;
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int num = progress / 10;
                int temp = progress % 10;
                if (temp == 0) {
                    mNum = num;
                } else if (temp < 5) {
                    seekBar.setProgress(num * 10);
                    mNum = num;
                } else if (temp > 5) {
                    seekBar.setProgress((num + 1) * 10);
                    mNum = num + 1;
                }

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


    private int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
        private Context context;
        private List<TestInfo> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private MyViewHolder holder;
        private int layoutPosition;

        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, TestInfo data, int position);
        }

        public CustomAdapter(Context context, List<TestInfo> data) {
            this.context = context;
            this.data = data;
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_dialog_buy, parent, false);
            holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            TestInfo info = data.get(position);
            holder.unitTv.setText(info.getUnit());
            holder.priceTv.setText(info.getPrice());
            String profit = this.context.getString(R.string.dialog_buy_profit_num, info.getProfit());
            holder.profitTv.setText(profit);
            String fee = this.context.getString(R.string.dialog_buy_fee_num, info.getFee());
            holder.feeTv.setText(fee);
            holder.itemView.setTag(info);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取当前点击的位置
                    layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, (TestInfo) holder.itemView.getTag(), layoutPosition);
                }
            });

            //更改状态
            if (position == layoutPosition) {
                holder.itemLayout.setBackgroundResource(R.drawable.shape_dialog_buy_item_select);
                int selectColor = Color.parseColor("#ffffff");
                holder.unitTv.setTextColor(selectColor);
                holder.priceTv.setTextColor(selectColor);
                holder.profitTv.setTextColor(selectColor);
                holder.feeTv.setTextColor(selectColor);
                holder.feeUnitTv.setTextColor(selectColor);
            } else {
                holder.itemLayout.setBackgroundResource(R.drawable.shape_dialog_buy_item);
                int defaultColor = Color.parseColor("#282828");
                holder.unitTv.setTextColor(defaultColor);
                holder.priceTv.setTextColor(defaultColor);
                holder.profitTv.setTextColor(defaultColor);
                holder.feeTv.setTextColor(defaultColor);
                holder.feeUnitTv.setTextColor(defaultColor);
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
            private LinearLayout itemLayout;
            private TextView unitTv;
            private TextView priceTv;
            private TextView profitTv;
            private TextView feeTv;
            private TextView feeUnitTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_dialog_layout);
                unitTv = (TextView) itemView.findViewById(R.id.item_dialog_unit_tv);
                priceTv = (TextView) itemView.findViewById(R.id.item_dialog_price_tv);
                profitTv = (TextView) itemView.findViewById(R.id.item_dialog_profit_tv);
                feeTv = (TextView) itemView.findViewById(R.id.item_dialog_fee_tv);
                feeUnitTv = (TextView) itemView.findViewById(R.id.item_dialog_fee_unit_tv);
            }
        }
    }

    private List<TestInfo> getData() {
        List<TestInfo> data = new ArrayList<>();
        TestInfo info1 = new TestInfo("0.1t", "10", "0.02", "1");
        TestInfo info2 = new TestInfo("5t", "200", "0.5", "20");
        TestInfo info3 = new TestInfo("10t", "500", "2", "75");
        TestInfo info4 = new TestInfo("40t", "2000", "2", "75");

        data.add(info1);
        data.add(info2);
        data.add(info3);
        data.add(info4);

        return data;
    }


    private class TestInfo {
        private String unit = "";
        private String price = "";
        private String profit = "";
        private String fee = "";

        public TestInfo(String unit, String price, String profit, String fee) {
            this.unit = unit;
            this.price = price;
            this.profit = profit;
            this.fee = fee;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }

}

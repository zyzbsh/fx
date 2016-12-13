package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.FullyGridLayoutManager;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
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

    private BuildPositionListener mBuildPositionListener;

    private TextView mOkTv;

    private SeekBar mSeekBar;

    private ContractEntity mContract;

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

    public BuildDialog(Context context, ContractEntity entity, boolean up) {
        super(context, R.style.BuyDialogTheme);
        mIsUp = up;
        this.setCanceledOnTouchOutside(false);
        mContract = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_build);
        NestedScrollView layout = (NestedScrollView) findViewById(R.id.dialog_build_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 18);
        int h = UIUtil.dip2px(getContext(), 50);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        params.height = UIUtil.getScreenHeight(AppApplication.getInstance().getActivity()) - h;
        layout.setLayoutParams(params);
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
        ViewGroup.LayoutParams mParams = recyclerView.getLayoutParams();
        int raw = getData().size() / 2 + getData().size() % 2;
        mParams.height = UIUtil.dip2px(getContext(), 70) * raw;
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
            holder.feeTv.setText(String.valueOf(info.getHandingCharge()));
            holder.specTv.setText(info.getBaseNum() + info.getBaseUnit());
            holder.numTv.setText(String.valueOf(info.getHandingCharge() * 10));
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
                int selectColor = Color.parseColor("#ffffff");
                holder.feeTv.setTextColor(selectColor);
            } else {
                int defaultColor = Color.parseColor("#282828");
                holder.feeTv.setTextColor(defaultColor);
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
            private TextView numTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                feeTv = (TextView) itemView.findViewById(R.id.item_dialog_build_fee_tv);
                specTv = (TextView) itemView.findViewById(R.id.item_dialog_build_spec_tv);
                numTv = (TextView) itemView.findViewById(R.id.item_dialog_build_num_tv);
            }
        }
    }

    private List<ContractInfoEntity> getData() {
        return mContract.getData();
    }

}

package fxtrader.com.app.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;

/**
 * 体验券
 * Created by zhangyuzhu on 2016/11/28.
 */
public class ExperienceVoucherActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_experience_voucher);
        initViews();
    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.experience_voucher_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this);
        List<ExperenceEntity> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new ExperenceEntity());
        }
        adapter.setDataList(data);
        recyclerView.setAdapter(adapter);
    }

    class MyAdapter extends ListBaseAdapter<ExperenceEntity> {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_experience_voucher, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ExperenceEntity item = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView experienceVoucherTv;
            private TextView moneyTv;
            private TextView validTv;

            public ViewHolder(View view) {
                super(view);
                experienceVoucherTv = (TextView) view.findViewById(R.id.experience_voucher_tv);
                moneyTv = (TextView) view.findViewById(R.id.experience_voucher_num_tv);
                validTv = (TextView) view.findViewById(R.id.experience_voucher_valid_tv);
            }
        }
    }

    class ExperenceEntity{

    }

}

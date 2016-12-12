package fxtrader.com.app.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.homepage.ProfitListActivity;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;

/**
 * 发现盈利榜
 * Created by zhangyuzhu on 2016/12/4.
 */
public class ProfitFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_find, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.find_more_tv).setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.find_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> data = getTestData();
        DataAdapter adapter = new DataAdapter(getActivity());
        adapter.setDataList(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));

    }

    private List<String> getTestData() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_more_tv://更多
                openActivity(ProfitListActivity.class);
                break;
            default:
                break;
        }
    }

    class DataAdapter extends ListBaseAdapter<String> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.find_item_profit, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText("NICHOLS");
            viewHolder.maxPercentTv.setText("+32.97%");
            setTagImWithPosition(viewHolder.tagIm, position);
        }

        private void setTagImWithPosition(ImageView im, int position) {
            if (position == 0) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_first);
            } else if (position == 1) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_second);
            } else if (position == 2) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_third);
            } else if(position == 3) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_forth);
            } else {
                im.setVisibility(View.INVISIBLE);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView tagIm;
            CircleImageView avatarIm;
            TextView nameTv;
            TextView maxPercentTv;
            TextView followTv;

            public ViewHolder(View view) {
                super(view);
                tagIm = (ImageView) view.findViewById(R.id.find_item_profit_tag_im);
                avatarIm = (CircleImageView) view.findViewById(R.id.find_item_profit_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.find_item_profit_name_tv);
                maxPercentTv = (TextView) view.findViewById(R.id.find_item_max_percent_tv);
                followTv = (TextView) view.findViewById(R.id.find_item_profit_follow_tv);
            }
        }
    }

}

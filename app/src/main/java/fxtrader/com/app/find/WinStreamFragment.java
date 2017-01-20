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
import fxtrader.com.app.entity.RedEnvelopeEntity;
import fxtrader.com.app.homepage.WinStreamListActivity;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;

/**
 * 发现连胜榜
 * Created by zhangyuzhu on 2016/12/4.
 */
public class WinStreamFragment extends BaseFragment implements View.OnClickListener{

    private LRecyclerView mRecyclerView;

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
        mRecyclerView = (LRecyclerView) view.findViewById(R.id.find_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPullRefreshEnabled(false);
        DataAdapter adapter = new DataAdapter(getActivity());
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(getContext(), adapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_more_tv://更多
                openActivity(WinStreamListActivity.class);
                break;
            default:
                break;
        }
    }

    class DataAdapter extends ListBaseAdapter<RedEnvelopeEntity> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.find_item_win_stream, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText("NICHOLS");
            setTagImWithPosition(viewHolder.tagIm, position);
            for (int i = 0; i < 5; i++) {
                viewHolder.flagViews[i].setBackgroundResource(R.drawable.shape_oval_red);
                viewHolder.flagViews[i].setText(R.string.win);
            }
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
            TextView followTv;
            TextView[] flagViews = new TextView[5];

            public ViewHolder(View view) {
                super(view);
                tagIm = (ImageView) view.findViewById(R.id.find_item_win_stream_tag_im);
                avatarIm = (CircleImageView) view.findViewById(R.id.find_item_win_stream_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.find_item_win_stream_name_tv);
                followTv = (TextView) view.findViewById(R.id.find_item_win_stream_follow_tv);
                flagViews[0] = (TextView) view.findViewById(R.id.find_item_win_stream_1_tv);
                flagViews[1] = (TextView) view.findViewById(R.id.find_item_win_stream_2_tv);
                flagViews[2] = (TextView) view.findViewById(R.id.find_item_win_stream_3_tv);
                flagViews[3] = (TextView) view.findViewById(R.id.find_item_win_stream_4_tv);
                flagViews[4] = (TextView) view.findViewById(R.id.find_item_win_stream_5_tv);
            }
        }
    }
}

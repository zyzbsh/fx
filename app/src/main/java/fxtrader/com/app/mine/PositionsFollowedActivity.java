package fxtrader.com.app.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.UserSubscribeEntity;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.view.CircleImageView;

/**
 * Created by pc on 2016/12/28.
 */
public class PositionsFollowedActivity extends BaseActivity{

    private LRecyclerView mRecyclerView;

    private TextView mPersonNumTv;

    private DataAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_followed_position_list);
    }

    private void initViews() {

    }

    class DataAdapter extends ListBaseAdapter<UserSubscribeEntity> {
        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_follow, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UserSubscribeEntity entity = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(mContext)
                    .load(entity.getHeadImgUrl())
                    .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(viewHolder.avatarIm);
            viewHolder.nameTv.setText(entity.getNickname());
            boolean isFollowed = entity.isSubscribe();
            if (isFollowed) {
                viewHolder.followTv.setText(getString(R.string.follow));
            } else {
                viewHolder.followTv.setText(getString(R.string.cancel_follow));
            }
            viewHolder.followTv.setTag(isFollowed);
            viewHolder.followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView avatarIm;
            TextView nameTv;
            TextView followTv;
            TextView dealDirectionTv;
            TextView buildTimeTv;
            TextView buildPriceTv;
            TextView currentPriceTv;
            TextView handChargeTv;
            TextView profitAndLossTv;
            TextView stopProfitTv;
            TextView stopLossTv;
            TextView myButTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.followed_position_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.followed_position_name_tv);
                dealDirectionTv = (TextView) view.findViewById(R.id.followed_position_deal_direction_tv);
                followTv = (TextView) view.findViewById(R.id.followed_position_follow_tv);
                buildTimeTv = (TextView) view.findViewById(R.id.followed_position_build_time_tv);
                buildPriceTv = (TextView) view.findViewById(R.id.followed_position_build_price_tv);
                currentPriceTv = (TextView) view.findViewById(R.id.followed_position_current_price_tv);
                handChargeTv = (TextView) view.findViewById(R.id.followed_position_hand_charge_tv);
                profitAndLossTv = (TextView) view.findViewById(R.id.followed_position_profit_and_loss_tv);
                stopProfitTv = (TextView) view.findViewById(R.id.followed_position_stop_profit_tv);
                stopLossTv = (TextView) view.findViewById(R.id.followed_position_stop_loss_tv);
                myButTv = (TextView) view.findViewById(R.id.followed_position_my_buy_tv);
            }
        }
    }
}

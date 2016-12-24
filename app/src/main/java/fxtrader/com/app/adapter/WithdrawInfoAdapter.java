package fxtrader.com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/12/23.
 */
public class WithdrawInfoAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mData;

    public WithdrawInfoAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_info, viewGroup, false);
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.item_withdraw_info_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String info = mData.get(i);
        holder.tv.setText(info);

        return view;
    }

    static class ViewHolder {
        TextView tv;
    }

}

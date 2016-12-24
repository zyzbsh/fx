package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.WithdrawInfoAdapter;
import fxtrader.com.app.tools.UIUtil;

/**
 * Created by pc on 2016/12/23.
 */
public class WithdrawBankDialog extends Dialog{

    private String mTitle;
    private List<String> mData;
    private WithdrawInfoAdapter mAdapter;
    private TextView mTitleView;
    private OnItemListener mOnItemListener;

    public WithdrawBankDialog(Context context, String title, List<String> data, OnItemListener listener) {
        super(context);
        mTitle = title;
        mData = data;
        mOnItemListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw_inifo);
        initParams();
        initViews();
    }

    private void initParams() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_withdraw_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int w = UIUtil.dip2px(getContext(), 20);
        int h = UIUtil.dip2px(getContext(), 100);
        params.width = UIUtil.getScreenWidth(AppApplication.getInstance().getActivity()) - w;
        params.height = UIUtil.getScreenHeight(AppApplication.getInstance().getActivity()) - h;
        layout.setLayoutParams(params);
    }

    private void initViews(){
        mTitleView = (TextView) findViewById(R.id.dialog_withdraw_title_tv);
        mTitleView.setText(mTitle);
        ListView listView = (ListView) findViewById(R.id.dialog_withdraw_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                if (mOnItemListener != null) {
                    String name = mData.get(i);
                    mOnItemListener.onClicked(name);
                }
            }
        });
        mAdapter = new WithdrawInfoAdapter(getContext(), mData);
        listView.setAdapter(mAdapter);
    }

    public void setData(String title, List<String> data) {
        mTitle = title;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(data);
        refresh();
    }

    private void refresh(){
        mTitleView.setText(mTitle);
        mAdapter.notifyDataSetChanged();
    }

    public interface OnItemListener{
        public void onClicked(String name);
    }


}

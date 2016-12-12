package fxtrader.com.app.protection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.tools.UIUtil;

/**
 * 三大保障
 * Created by zhangyuzhu on 2016/12/1.
 */
public class ProtectionFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protection, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView im = (ImageView) view.findViewById(R.id.protection_title_im);
        int width = UIUtil.getScreenWidth(getActivity());
        int height = width * 249 / 640;
        ViewGroup.LayoutParams params = im.getLayoutParams();
        params.width = width;
        params.height = height;
        im.setLayoutParams(params);
    }
}

package fxtrader.com.app.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import fxtrader.com.app.tools.UIUtil;

/**
 * Created by zhangyuzhu on 2016/12/1.
 */
public abstract class BaseFragment extends Fragment {

    protected void openActivity(Class<?> clazz) {
        UIUtil.openActivity(getActivity(), clazz);
    }

    protected void openActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        getActivity().startActivityForResult(intent, requestCode);
    }

}

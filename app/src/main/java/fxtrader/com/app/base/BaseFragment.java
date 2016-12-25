package fxtrader.com.app.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.DefaultProgressDialog;

/**
 * Created by zhangyuzhu on 2016/12/1.
 */
public abstract class BaseFragment extends Fragment {

    private DefaultProgressDialog mLoadingProgress;

    protected void openActivity(Class<?> clazz) {
        UIUtil.openActivity(getActivity(), clazz);
    }

    protected void openActivityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    protected void showProgressDialog(){
        if (mLoadingProgress == null) {
            mLoadingProgress = new DefaultProgressDialog(getContext());
        }
        mLoadingProgress.show();
    }

    protected void dismissProgressDialog(){
        if (mLoadingProgress != null && mLoadingProgress.isShowing()){
            mLoadingProgress.dismiss();
        }
    }

}

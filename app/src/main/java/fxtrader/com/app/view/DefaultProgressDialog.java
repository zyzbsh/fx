package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import fxtrader.com.app.R;

/**
 * Created by zhangyuzhu on 2016/12/14.
 */
public class DefaultProgressDialog extends Dialog{

    public DefaultProgressDialog(Context context) {
        super(context);
    }

    public DefaultProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
    }
}

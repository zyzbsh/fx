package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.R;

/**
 * Created by pc on 2016/11/20.
 */
public class ClosePositionDialog extends Dialog {

    public ClosePositionDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_position);
        TextView contractTv = (TextView) findViewById(R.id.dialog_close_position_contract_tv);
        findViewById(R.id.dialog_close_position_ok_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.dialog_close_position_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

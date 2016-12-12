package fxtrader.com.app.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import fxtrader.com.app.R;

public class HorizontalProgressDialog extends AlertDialog {

	ProgressBar progressBar;
	TextView txt_status, txt_progress;

	public HorizontalProgressDialog(Context context) {
		super(context);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_horizontalprogress);
		txt_status = (TextView) findViewById(R.id.txt_uploadstatus);
	}

	@Override
	public void show() {
		super.show();
	}

	public void setStatus(String status) {
		txt_status.setText(status);
	}

}

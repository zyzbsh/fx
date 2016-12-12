package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * 交易规则
 * Created by zhangyuzhu on 2016/11/26.
 */
public class TradeRulesActivity extends BaseActivity implements View.OnClickListener{

    private TextView mProductionRulesTv;

    private TextView mAccountRulesTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_trade_rules);
        initViews();
    }

    private void initViews() {
        mProductionRulesTv = (TextView) findViewById(R.id.trade_rules_production_intro_tv);
        mAccountRulesTv = (TextView) findViewById(R.id.trade_rules_account_rules_tv);
        mProductionRulesTv.setOnClickListener(this);
        mAccountRulesTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trade_rules_production_intro_tv:
                break;
            case R.id.trade_rules_account_rules_tv:
                break;
            default:
                break;
        }
    }
}

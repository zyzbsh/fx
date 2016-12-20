package fxtrader.com.app.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2016/12/20.
 */
public class UserInfoColumn extends DatabaseColumn{

    public static final String TABLE_NAME = "user";

    public static final String ID = "id";
    public static final String ACCOUNT = "account";
    public static final String AGENT = "agent";
    public static final String AGENT_ID = "agent_id";
    public static final String AVATAR_URL = "avatar_url";
    public static final String MEMBER_CODE = "member_code";
    public static final String MEMBER_ID = "member_id";
    public static final String NICKNAME = "nickname";
    public static final String ORGAN_ID = "organ_id";
    public static final String REFERRER_ID = "referrer_id";
    public static final String REGISTER_DATE = "register_date";
    public static final String SEX = "sex";
    public static final String SHARE_ID = "share_id";
    public static final String TEL_NUMBER = "tel_number";
    public static final String WX_OPEN_ID = "wx_open_id";
    public static final String FOUNDS = "founds";
    public static final String COUPON_AMOUNT = "coupon_amount";

    private static final Map<String, String> mColumnMap = new HashMap<String, String>();
    static {
        mColumnMap.put(_ID, "integer primary key autoincrement");
        mColumnMap.put(ID, "integer");
        mColumnMap.put(ACCOUNT, "text");
        mColumnMap.put(AGENT,"integer");
        mColumnMap.put(AGENT_ID, "integer");
        mColumnMap.put(AVATAR_URL, "text");
        mColumnMap.put(MEMBER_CODE, "text");
        mColumnMap.put(MEMBER_ID, "integer");
        mColumnMap.put(NICKNAME, "text");
        mColumnMap.put(ORGAN_ID, "integer");
        mColumnMap.put(REFERRER_ID, "integer");
        mColumnMap.put(REGISTER_DATE, "integer");
        mColumnMap.put(SEX, "integer");
        mColumnMap.put(SHARE_ID, "integer");
        mColumnMap.put(TEL_NUMBER, "text");
        mColumnMap.put(WX_OPEN_ID, "text");
        mColumnMap.put(FOUNDS, "text");
        mColumnMap.put(COUPON_AMOUNT, "integer");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Map<String, String> getTableMap() {
        return mColumnMap;
    }
}

package fxtrader.com.app.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2016/12/20.
 */
public class UserCouponsColumn extends DatabaseColumn{

    public static final String TABLE_NAME = "coupons";
    public static final String ID = "id";
    public static final String TICKET_ID = "ticket_id";
    public static final String LAST_TIME = "last_time";

    private static final Map<String, String> mColumnMap = new HashMap<String, String>();
    static {
        mColumnMap.put(_ID, "integer primary key autoincrement");
        mColumnMap.put(ID, "integer");
        mColumnMap.put(TICKET_ID, "integer");
        mColumnMap.put(LAST_TIME, "integer");
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

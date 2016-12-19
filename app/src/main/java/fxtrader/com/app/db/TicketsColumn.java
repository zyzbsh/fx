package fxtrader.com.app.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2016/12/19.
 */
public class TicketsColumn extends DatabaseColumn{

    public static final String TABLE_NAME = "tickets";
    public static final String ID = "id";
    public static final String VALUE = "value";
    public static final String CONTRACT_DATA_TYPE = "type";

    private static final Map<String, String> mColumnMap = new HashMap<String, String>();
    static {
        mColumnMap.put(_ID, "integer primary key autoincrement");
        mColumnMap.put(ID, "integer");
        mColumnMap.put(VALUE, "integer");
        mColumnMap.put(CONTRACT_DATA_TYPE, "text");
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

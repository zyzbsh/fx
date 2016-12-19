package fxtrader.com.app.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.db.DBHelper;
import fxtrader.com.app.db.TicketsColumn;
import fxtrader.com.app.db.UserCouponsColumn;
import fxtrader.com.app.entity.CouponDetailEntity;
import fxtrader.com.app.entity.TicketEntity;
import fxtrader.com.app.entity.TicketListEntity;

/**
 * Created by pc on 2016/12/19.
 */
public class TicketsHelper extends ColumnHelper<TicketEntity>{

    private static Object mLock = new Object();
    private static TicketsHelper sHelper;
    private Context mContext;

    public static TicketsHelper getInstance(){
        if (sHelper == null) {
            synchronized (mLock) {
                if (sHelper == null) {
                    sHelper = new TicketsHelper();
                }
            }
        }
        return sHelper;
    }

    private TicketsHelper(){
        mContext = AppApplication.getInstance().getApplicationContext();
    }

    public void save(TicketListEntity entity) {
        List<TicketEntity> list = entity.getObject();
        if (list != null && !list.isEmpty()) {
            delete();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                TicketEntity bean = list.get(i);
                DBHelper.getInstance(mContext).insert(TicketsColumn.TABLE_NAME, getValues(bean));
            }
        }
    }

    public List<TicketEntity> getData() {
        List<TicketEntity> data = new ArrayList<>();
        Cursor c = DBHelper.getInstance(mContext).rawQuery(TicketsColumn.TABLE_NAME, null);
        if (exist(c)) {
            c.moveToFirst();
            do {
                data.add(getBean(c));
            }while (c.moveToNext());
            c.close();
        }
        return data;
    }

    public TicketEntity query(int id) {
        TicketEntity entity = null;
        Cursor c = DBHelper.getInstance(mContext).rawQuery(
                "SELECT * FROM " + TicketsColumn.TABLE_NAME + " WHERE " + TicketsColumn.ID
                        + " = ? ", new String[] { String.valueOf(id) });
        if (exist(c)) {
            c.moveToFirst();
            entity = getBean(c);
        }
        return entity;
    }

    public void delete() {
        DBHelper.getInstance(mContext).delete(TicketsColumn.TABLE_NAME, null, null);
    }

    @Override
    protected ContentValues getValues(TicketEntity bean) {
        ContentValues values = new ContentValues();
        values.put(TicketsColumn.ID, bean.getId());
        values.put(TicketsColumn.VALUE, bean.getValue());
        values.put(TicketsColumn.CONTRACT_DATA_TYPE, bean.getDataTypes());
        return values;
    }

    @Override
    protected TicketEntity getBean(Cursor c) {
        TicketEntity entity = new TicketEntity();
        entity.setId(getInt(c, TicketsColumn.ID));
        entity.setValue(getInt(c, TicketsColumn.VALUE));
        entity.setDataTypes(getString(c, TicketsColumn.CONTRACT_DATA_TYPE));
        return entity;
    }
}

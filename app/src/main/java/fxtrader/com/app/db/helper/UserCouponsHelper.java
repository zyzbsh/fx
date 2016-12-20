package fxtrader.com.app.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.db.DBHelper;
import fxtrader.com.app.db.UserCouponsColumn;
import fxtrader.com.app.entity.CouponDetailEntity;
import fxtrader.com.app.entity.CouponListEntity;

/**
 * Created by pc on 2016/12/20.
 */
public class UserCouponsHelper extends ColumnHelper<CouponDetailEntity>{

    private static Object mLock = new Object();
    private static UserCouponsHelper sHelper;
    private Context mContext;

    public static UserCouponsHelper getInstance(){
        if (sHelper == null) {
            synchronized (mLock) {
                if (sHelper == null) {
                    sHelper = new UserCouponsHelper();
                }
            }
        }
        return sHelper;
    }

    private UserCouponsHelper(){
        mContext = AppApplication.getInstance().getApplicationContext();
    }

    public List<CouponDetailEntity> getData() {
        List<CouponDetailEntity> data = new ArrayList<>();
        Cursor c = DBHelper.getInstance(mContext).rawQuery("select * from " + UserCouponsColumn.TABLE_NAME, null);
        if (exist(c)) {
            c.moveToFirst();
            do {
                data.add(getBean(c));
            }while (c.moveToNext());
            c.close();
        }
        return data;
    }

    public void save(CouponListEntity entity){
        List<CouponDetailEntity> list = entity.getObject();
        if (list != null && !list.isEmpty()) {
            delete();
            int size = list.size();
            for (int i = 0; i < size; i++ ){
                CouponDetailEntity bean = list.get(i);
                DBHelper.getInstance(mContext).insert(UserCouponsColumn.TABLE_NAME, getValues(bean));
            }
        }
    }

    public void delete() {
        DBHelper.getInstance(mContext).delete(UserCouponsColumn.TABLE_NAME, null, null);
    }

    public void delete(CouponDetailEntity entity) {
    }


    public int getCount() {
        Cursor c = DBHelper.getInstance(mContext).rawQuery(UserCouponsColumn.TABLE_NAME, null);
        if (c == null) {
            return 0;
        }
        int count = c.getCount();
        c.close();
        return count;
    }

    @Override
    protected ContentValues getValues(CouponDetailEntity bean) {
        ContentValues values = new ContentValues();
        values.put(UserCouponsColumn.ID, bean.getId());
        values.put(UserCouponsColumn.TICKET_ID, bean.getTicketId());
        values.put(UserCouponsColumn.LAST_TIME, bean.getLastUseTime());
        return values;
    }

    @Override
    protected CouponDetailEntity getBean(Cursor c) {
        CouponDetailEntity entity = new CouponDetailEntity();
        entity.setId(getInt(c, UserCouponsColumn.ID));
        entity.setTicketId(getInt(c, UserCouponsColumn.TICKET_ID));
        entity.setLastUseTime(getLong(c, UserCouponsColumn.LAST_TIME));
        return entity;
    }
}

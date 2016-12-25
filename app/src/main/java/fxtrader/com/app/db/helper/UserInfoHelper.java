package fxtrader.com.app.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.db.DBHelper;
import fxtrader.com.app.db.UserInfoColumn;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.tools.LogZ;

/**
 * Created by pc on 2016/12/20.
 */
public class UserInfoHelper extends ColumnHelper<UserEntity> {

    private static Object mLock = new Object();
    private static UserInfoHelper sHelper;
    private Context mContext;

    private UserInfoHelper() {
        mContext = AppApplication.getInstance().getApplicationContext();
    }

    public static UserInfoHelper getInstance() {
        if (sHelper == null) {
            synchronized (mLock) {
                if (sHelper == null) {
                    sHelper = new UserInfoHelper();
                }
            }
        }
        return sHelper;
    }

    public void save(UserEntity user) {

        String[] args = new String[] {user.getObject().getAccount()};
        Cursor c = DBHelper.getInstance(mContext).rawQuery(
                "SELECT * FROM " + UserInfoColumn.TABLE_NAME + " WHERE " + UserInfoColumn.ACCOUNT
                        + " = ?", args);
        if (exist(c)) {
            c.moveToFirst();
            delete(user.getObject().getAccount());
        }
        DBHelper.getInstance(mContext).insert(UserInfoColumn.TABLE_NAME, getValues(user));
        c.close();
    }

    private void delete(String account) {
        String[] args = new String[] {account};
        DBHelper.getInstance(mContext).delete(UserInfoColumn.TABLE_NAME,
                UserInfoColumn.ACCOUNT + " = ?", args);
    }

    public boolean hasTelNumber(String account) {
        UserEntity entity = getEntity(account);
        return !TextUtils.isEmpty(entity.getObject().getTelNumber());
    }

    public UserEntity getEntity(String account) {
        LogZ.i("account = " + account);
        UserEntity entity = null;
        Cursor c = DBHelper.getInstance(mContext).rawQuery("SELECT * FROM " + UserInfoColumn.TABLE_NAME + " WHERE " + UserInfoColumn.ACCOUNT + " = ? ", new String[]{account});
        if (exist(c)) {
            c.moveToFirst();
            entity = getBean(c);
        }
        return entity;
    }

    @Override
    protected ContentValues getValues(UserEntity bean) {
        ContentValues values = new ContentValues();
        values.put(UserInfoColumn.ID, bean.getObject().getId());
        values.put(UserInfoColumn.ACCOUNT, bean.getObject().getAccount());
        values.put(UserInfoColumn.AGENT, bean.getObject().isAgent() ? 1 : 0);
        values.put(UserInfoColumn.AGENT_ID, bean.getObject().getAgentId());
        values.put(UserInfoColumn.AVATAR_URL, bean.getObject().getHeadimgurl());
        values.put(UserInfoColumn.MEMBER_CODE, bean.getObject().getMemberCode());
        values.put(UserInfoColumn.MEMBER_ID, bean.getObject().getMemberId());
        values.put(UserInfoColumn.NICKNAME, bean.getObject().getNickname());
        values.put(UserInfoColumn.ORGAN_ID, bean.getObject().getOrganId());
        values.put(UserInfoColumn.REFERRER_ID, bean.getObject().getReferrerId());
        values.put(UserInfoColumn.REGISTER_DATE, bean.getObject().getRegisterDate());
        values.put(UserInfoColumn.SEX, bean.getObject().getSex());
        values.put(UserInfoColumn.SHARE_ID, bean.getObject().getShareId());
        values.put(UserInfoColumn.TEL_NUMBER, bean.getObject().getTelNumber());
        values.put(UserInfoColumn.WX_OPEN_ID, bean.getObject().getWxOpenId());
        values.put(UserInfoColumn.COUPON_AMOUNT, bean.getObject().getCouponAmount());
        values.put(UserInfoColumn.FOUNDS, bean.getObject().getFunds());
        return values;
    }

    @Override
    protected UserEntity getBean(Cursor c) {
        UserEntity entity = new UserEntity();
        UserEntity.ObjectBean objet = new UserEntity.ObjectBean();
        objet.setId(getInt(c, UserInfoColumn.ID));
        objet.setAccount(getString(c, UserInfoColumn.ACCOUNT));
        objet.setAgent((getInt(c, UserInfoColumn.AGENT) == 1) ? true : false);
        objet.setAgentId(getInt(c, UserInfoColumn.AGENT_ID));
        objet.setHeadimgurl(getString(c, UserInfoColumn.AVATAR_URL));
        objet.setMemberCode(getString(c, UserInfoColumn.MEMBER_CODE));
        objet.setMemberId(getInt(c, UserInfoColumn.MEMBER_ID));
        objet.setNickname(getString(c, UserInfoColumn.NICKNAME));
        objet.setOrganId(getInt(c, UserInfoColumn.ORGAN_ID));
        objet.setReferrerId(getInt(c, UserInfoColumn.REFERRER_ID));
        objet.setRegisterDate(getLong(c, UserInfoColumn.REGISTER_DATE));
        objet.setSex(getInt(c, UserInfoColumn.SEX));
        objet.setShareId(getInt(c, UserInfoColumn.SHARE_ID));
        objet.setTelNumber(getString(c, UserInfoColumn.TEL_NUMBER));
        objet.setWxOpenId(getString(c, UserInfoColumn.WX_OPEN_ID));
        objet.setCouponAmount(getInt(c, UserInfoColumn.COUPON_AMOUNT));
        objet.setFunds(Double.parseDouble(getString(c, UserInfoColumn.FOUNDS)));
        entity.setObject(objet);
        return entity;
    }
}

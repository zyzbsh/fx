package fxtrader.com.app.config;

import android.content.Context;
import android.content.SharedPreferences;

import fxtrader.com.app.AppApplication;

/**
 * Created by pc on 2017/1/7.
 */
public class AnnouncementConfig {
    private static final String FILE_NAME = "announcement";
    private static final String ANNOUNCEMENT_ID = "announcement_id";
    private static AnnouncementConfig sConfig;
    private SharedPreferences mSp;

    private AnnouncementConfig(){
        String fileName = LoginConfig.getInstance().getId() + FILE_NAME;
        mSp = AppApplication.getInstance().getBaseContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static AnnouncementConfig getInstance(){
        if (sConfig == null) {
            sConfig = new AnnouncementConfig();
        }
        return sConfig;
    }

    public void read(int id) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(id + "", true);
        editor.commit();
    }

    public boolean hasReaded(int id) {
        return mSp.getBoolean(id + "", false);
    }
}

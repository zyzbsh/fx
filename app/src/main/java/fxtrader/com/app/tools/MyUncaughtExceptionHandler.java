package fxtrader.com.app.tools;

import android.Manifest;
import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.permission.IPermissionCheck;
import fxtrader.com.app.permission.PermissionChecker;

/**
 * Created by zhangyuzhu on 2017/1/20.
 */
public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler a;

    public MyUncaughtExceptionHandler() {
        a = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        setExcpetionMsg(ex);
        a.uncaughtException(thread, ex);
    }

    private void setExcpetionMsg(Throwable ex) {
        saveException(ex);
    }

    public void saveException(final Throwable ex) {
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                try {
                    File dir = new File(
                            android.os.Environment.getExternalStorageDirectory()
                                    + "/redhouse/exception/");
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(dir, System.currentTimeMillis() + ".log");
                    PrintWriter writer = new PrintWriter(file);
                    ex.printStackTrace(writer);
                    writer.flush();
                    writer.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }.start();

    }

}

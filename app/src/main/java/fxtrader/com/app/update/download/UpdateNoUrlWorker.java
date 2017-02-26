package fxtrader.com.app.update.download;

import fxtrader.com.app.update.ParseData;
import fxtrader.com.app.update.UpdateHelper;
import fxtrader.com.app.update.bean.Update;
import fxtrader.com.app.update.listener.UpdateListener;
import fxtrader.com.app.update.type.UpdateType;
import fxtrader.com.app.update.util.HandlerUtil;
import fxtrader.com.app.update.util.InstallUtil;
import fxtrader.com.app.update.util.NetworkUtil;
import fxtrader.com.app.update.util.UpdateSP;

public class UpdateNoUrlWorker implements Runnable {

    protected UpdateListener checkCB;
    protected ParseData parser;
    protected String requestResultData;


    public void setRequestResultData(String requestResultData) {
        this.requestResultData = requestResultData;
    }

    public void setUpdateListener(UpdateListener checkCB) {
        this.checkCB = checkCB;
    }

    public void setParser(ParseData parser) {
        this.parser = parser;
    }

    @Override
    public void run() {
        try {
            Update parse = parser.parse(requestResultData);
            if (parse == null) {
                throw new IllegalArgumentException("parse response to update failed by " + parser.getClass().getCanonicalName());
            }
            int curVersion = InstallUtil.getApkVersion(UpdateHelper.getInstance().getContext());
            if ((parse.getVersionCode() > curVersion) &&
                    ((UpdateSP.isIgnore(parse.getVersionCode() + "")
                            && (UpdateHelper.getInstance().getUpdateType() == UpdateType.checkupdate))
                            || (!UpdateSP.isIgnore(parse.getVersionCode() + "")
                            && (UpdateHelper.getInstance().getUpdateType() != UpdateType.autowifiupdate))
                            || (!UpdateSP.isIgnore(parse.getVersionCode() + "")
                            && (UpdateHelper.getInstance().getUpdateType() == UpdateType.autowifiupdate)
                            && NetworkUtil.isConnectedByWifi()))) {
                /**有新版本*/
                UpdateSP.setForced(parse.isForce());
                sendHasUpdate(parse);
            } else {
                /**无新版本*/
                sendNoUpdate();
            }
        } catch (HttpException he) {
            sendOnErrorMsg(he.getCode(), he.getErrorMsg());
        } catch (Exception e) {
            sendOnErrorMsg(-1, e.getMessage());
        }
    }

    private void sendHasUpdate(final Update update) {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.hasUpdate(update);
            }
        });
    }

    private void sendNoUpdate() {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.noUpdate();
            }
        });
    }

    private void sendOnErrorMsg(final int code, final String errorMsg) {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.onCheckError(code, errorMsg);
            }
        });
    }
}

package com.paipeng.cplocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


import com.paipeng.cplocation.model.CPLocation;


public abstract class CPBaseLocationService extends Service implements CPLocationServiceInterface {
    protected static Context context;
    protected int gpsIntervalTime = 15;//定位时间间隔，单位s，初始化数值是默认定位时间间隔
    protected CP_LOCATION_LANGUAGE language;
    protected String apiKey;
    protected CPLocation cpLocation;
    protected CP_LOCATION_STATE cpLocationState;
    protected double latitude;
    protected double longitude;

    public CPBaseLocationService(Context context, String apiKey, int gpsIntervalTime, CP_LOCATION_LANGUAGE language) {
        this.context = context;
        this.apiKey = apiKey;
        this.gpsIntervalTime = gpsIntervalTime;
        this.language = language;
        this.cpLocationState = CP_LOCATION_STATE.CP_LOCATION_STATE_NONE;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    public enum CP_LOCATION_LANGUAGE {
        CP_LOCATION_LANGUAGE_EN,
        CP_LOCATION_LANGUAGE_ZH,
        CP_LOCATION_LANGUAGE_FR
    }

    public enum CP_LOCATION_STATE {
        CP_LOCATION_STATE_NONE,
        CP_LOCATION_STATE_STARTED,
        CP_LOCATION_STATE_STOPPED,
        CP_LOCATION_STATE_SEARCHING
    }


    private void updateLocationState(CP_LOCATION_STATE cpLocationState) {
        if (this.cpLocationState != cpLocationState) {
            this.cpLocationState = cpLocationState;
            //EventBus.getDefault().postSticky(new LocateStateChangeMessage(this.s2ILocationState));
        }
    }

    @Override
    public void searching() {
        updateLocationState(CP_LOCATION_STATE.CP_LOCATION_STATE_SEARCHING);
    }
    @Override
    public void started() {
        updateLocationState(CP_LOCATION_STATE.CP_LOCATION_STATE_STARTED);
    }

    @Override
    public void stopped() {
        updateLocationState(CP_LOCATION_STATE.CP_LOCATION_STATE_STOPPED);

    }

    @Override
    public void setLanguage(CP_LOCATION_LANGUAGE language) {
        this.language = language;
    }

    public CPLocation getCPLocation() {
        return cpLocation;
    }

    public void setCpLocation(CPLocation cpLocation) {
        this.cpLocation = cpLocation;
    }

    public static Context getContext() {
        return context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

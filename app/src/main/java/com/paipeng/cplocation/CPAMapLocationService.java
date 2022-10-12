package com.paipeng.cplocation;

import static com.paipeng.cplocation.CPBaseLocationService.CP_LOCATION_LANGUAGE.CP_LOCATION_LANGUAGE_ZH;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.paipeng.cplocation.model.CPLocation;

public class CPAMapLocationService extends CPBaseLocationService {
    private static final String TAG = CPAMapLocationService.class.getSimpleName();
    private AMapLocationClient mLocationClient = null;


    public CPAMapLocationService(Context context, String apiKey, int gpsIntervalTime, CP_LOCATION_LANGUAGE language) {
        super(context, apiKey, gpsIntervalTime, language);
    }

    @Override
    public void start() {
        super.start();
        if (mLocationClient == null) {
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            try {
                mLocationClient = new AMapLocationClient(context);
                mLocationClient.setLocationListener(mAMapLocationListener);
                mLocationOption.setLocationCacheEnable(false);
                if (language == CP_LOCATION_LANGUAGE_ZH) {
                    mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.ZH);
                } else {
                    mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.EN);
                }

                if (cpLocation == null) {
                    mLocationOption.setInterval(2 * 1000);
                } else {
                    mLocationOption.setInterval(gpsIntervalTime * 1000);
                }
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

                mLocationOption.setWifiScan(true);
                mLocationClient.setLocationOption(mLocationOption);
                mLocationClient.startLocation();
                searching();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        Log.i(TAG, "stopListener");
        if (mLocationClient != null && mLocationClient.isStarted()) {
            Log.i(TAG, "stopListener");
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
            stopped();
        }
    }

    AMapLocationListener mAMapLocationListener = aMapLocation -> {
        if (aMapLocation != null) {
            Log.i(TAG, "aMapLocation: " + aMapLocation.getErrorCode());
            if (aMapLocation.getErrorCode() == 0) {
                if (latitude != aMapLocation.getLatitude() || longitude != aMapLocation.getLongitude()) {

                }
                setCPLocation(aMapLocation);

                started();
            } else {
                //EventBus.getDefault().postSticky(new LocateErrorMessage(aMapLocation.getErrorCode()));
                Log.e(TAG, "错误码：" + aMapLocation.getErrorCode() + " 错误信息：" + aMapLocation.getErrorInfo());
                stopped();
            }
        }
    };

    private void setCPLocation(AMapLocation aMapLocation) {
        if (cpLocation == null) {
            cpLocation = new CPLocation();
        }
        Log.i(TAG, "aMapLocation: " + aMapLocation.getLatitude() + ", " + aMapLocation.getLongitude() + " " + aMapLocation.getAddress());
        cpLocation.setCountry(aMapLocation.getCountry());
        cpLocation.setProvince(aMapLocation.getProvince());
        cpLocation.setCity(aMapLocation.getCity());
        cpLocation.setDistrict(aMapLocation.getDistrict());
        cpLocation.setAddress(aMapLocation.getAddress());
        cpLocation.setLatitude(aMapLocation.getLatitude());
        cpLocation.setLongitude(aMapLocation.getLongitude());
        cpLocation.setErrorCode(aMapLocation.getErrorCode());
        cpLocation.setErrorInfo(aMapLocation.getErrorInfo());
    }

}

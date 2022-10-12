package com.paipeng.cplocation;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.paipeng.cplocation.model.CPLocation;

public class CPBaiduLocationService extends CPBaseLocationService {
    private static final String TAG = CPBaiduLocationService.class.getSimpleName();
    private LocationClient m_objLocationClient = null;
    private S2iBaiduLocationListenner s2iBaiduLocationListenner;

    public CPBaiduLocationService(Context context, String apiKey, int gpsIntervalTime, CP_LOCATION_LANGUAGE language) {
        super(context, apiKey, gpsIntervalTime, language);
        s2iBaiduLocationListenner = new S2iBaiduLocationListenner();
    }

    @Override
    public void start() {
        super.start();
        Log.i(TAG, "startLocationListener");
        if (m_objLocationClient != null) {
            return;
        }
        m_objLocationClient = new LocationClient(context);

        LocationClientOption option = new LocationClientOption();

        // 1 minute scan location
        int span=15000;

        //可选，默认false,设置是否使用gps
        //option.setOpenGps(false);

        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系 "bd09ll"
        option.setCoorType("gcj02");
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);
        m_objLocationClient.setLocOption(option);

        m_objLocationClient.registerLocationListener(s2iBaiduLocationListenner);
        m_objLocationClient.start();
        searching();
    }

    @Override
    public void stop() {
        super.stop();
        Log.i(TAG, "stopListener");
        if(m_objLocationClient!=null){
            Log.i("Application", "unRegisterLocationListener");
            m_objLocationClient.unRegisterLocationListener(s2iBaiduLocationListenner);
            Log.i("Application", "stop");
            m_objLocationClient.stop();
            m_objLocationClient = null;
            stopped();
        }
    }

    public class S2iBaiduLocationListenner extends BDAbstractLocationListener {
        public void onReceiveLocation(BDLocation location) {
            Log.i(TAG, "onReceiveLocation " + location.getAddrStr());
            if (location == null)
                return ;
            setS2iLocation(location);
            //stopListener(location);
            started();
        }
    }


    private void setS2iLocation(BDLocation bdLocation) {
        if (cpLocation == null) {
            cpLocation = new CPLocation();
        }
        Log.i(TAG, "bdLocation: " + bdLocation.getLatitude() + ", " + bdLocation.getLongitude() + " " + bdLocation.getAddrStr());
        cpLocation.setCountry(bdLocation.getCountry());
        cpLocation.setProvince(bdLocation.getProvince());
        cpLocation.setCity(bdLocation.getCity());
        cpLocation.setDistrict(bdLocation.getDistrict());
        cpLocation.setAddress(bdLocation.getAddrStr());
        cpLocation.setLatitude(bdLocation.getLatitude());
        cpLocation.setLongitude(bdLocation.getLongitude());
        cpLocation.setErrorCode(bdLocation.getLocType());
        cpLocation.setErrorInfo(bdLocation.getLocTypeDescription());
    }
}

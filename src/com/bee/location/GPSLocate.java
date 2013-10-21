package com.bee.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:54
 * To change this template use File | Settings | File Templates.
 */
public class GPSLocate implements MLocate  {
    //每500毫秒更新一次
    private static final int INTERVAL_TIME = 500;
    //立即执行
    private static final int INTERVAL_DIS = 0;

    private Context mContext;
    private LocationManager mLocationManager;
    private GPSLocationListener mGPSLocationListener;

    public GPSLocate(Context context) {
        mContext = context;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void startLocate(MLocationListener mLocationListener) {

        if (mLocationListener == null) {
            throw new NullPointerException("MLocationListener must not be null");
        }

        //判断系统是否支持GPS定位
        List<String> providers = mLocationManager.getAllProviders();
        if (providers == null || providers.size() <= 0) {
            return;
        }

        //判断系统GPS是否开启
        if (!isOpen()) return;

        mGPSLocationListener = new GPSLocationListener(mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_TIME, INTERVAL_DIS, mGPSLocationListener);
    }

    @Override
    public void stopLocate() {
        if (mLocationManager != null && mGPSLocationListener != null) {
            mLocationManager.removeUpdates(mGPSLocationListener);
        }
    }

    public class GPSLocationListener implements LocationListener {

        MLocationListener listener;

        public GPSLocationListener(MLocationListener listener) {
            this.listener = listener;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (listener != null) {
                listener.onLocationChanged(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }

    //判断系统gps是否开启
    public boolean isOpen() {

        boolean isOpen = false;

        String gps = Settings.System.getString(mContext.getContentResolver(), Settings.System.LOCATION_PROVIDERS_ALLOWED);
        if (gps == null) {
            return isOpen;
        }

        String[] providers = gps.split(",");
        if (providers == null || providers.length == 0) {
            return isOpen;
        }

        for (String provider : providers) {
            if (provider.equalsIgnoreCase("gps")) {
                isOpen = true;
                break;
            }
        }
        return isOpen;
    }
}

package com.bee.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
//import com.baidu.mapapi.BMapManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:38
 * To change this template use File | Settings | File Templates.
 */
public class GpsLocation {
    private Location location;
    private LocationManager mLocationManager;

    // 每次侦听的最大持续时间
    private static final long LISTEN_TIME = 6000000;

    private LocationListener gpsListener;

    private Timer timer;

    public GpsLocation(Context context,LocationManager mLocationManager) {
    	this.mLocationManager=mLocationManager;
        gpsListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                GpsLocation.this.location = location;
                if(location!=null)
                {
                Log.d("GpsLocation","location============"+location.toString());}
                else{
                	Log.d("GpsLocation","正在定位");
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
    }

    public void startGps() {
        stopGps();
        try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsListener);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsListener);
                timer = new Timer(false);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stopGps();
                    }
                }, LISTEN_TIME);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    public void stopGps() {
        try {
            mLocationManager.removeUpdates(gpsListener);
            if (null != timer) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    public Location getLocation() {
        return location;
    }

    public Location getLastKnownLocation() {
        return location;
    }
    
    
    private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			if(loc !=null){
				 
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}

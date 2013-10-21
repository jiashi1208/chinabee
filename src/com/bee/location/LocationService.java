package com.bee.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午9:50
 * To change this template use File | Settings | File Templates.
 */
public class LocationService {
    private LocationManager lm;
    private Context context;
    private GpsLocation gpsLocation;

    // 这是对每次获取结果的一次缓存，并不是真实的当前位置，可以当做last known location来用
    private Location lastKnownLocation;
    private long lastLocationTime = 0;

    public LocationService(Context context) {
        this.context = context;
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        gpsLocation = new GpsLocation(context,lm);
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location location) {
        lastKnownLocation = location;
    }

    /**
     * 获取上一次定位的时间
     * @return last location time
     */
    public long getLastLocationTime() {
        return lastLocationTime;
    }
    public void setLastLocationTime(long time) {
        lastLocationTime = time;
    }
    /**
     * 在某个时间点，例如程序启动时，开始定位gps。
     */
    public void startGps() {
        gpsLocation.startGps();
    }

    /**
     * 终止gps定位
     */
    public void stopGps() {
        gpsLocation.stopGps();
    }

    /**
     * Get location with listener, default timeout is 20 seconds
     * @param listener
     */
    public synchronized void getLocation(Listener listener) {
        gpsLocation.startGps();
    }

    public interface Listener {
        public void onLocationUpdate(Location location);
    }
}

package com.bee.location;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:51
 * To change this template use File | Settings | File Templates.
 */
public interface MLocate {
    //开始定位
    public void startLocate(MLocationListener mLocationListener);

    //停止定位
    public void stopLocate();
}

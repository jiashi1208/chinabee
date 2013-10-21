package com.bee.location;

import android.content.Context;
import android.location.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:57
 * To change this template use File | Settings | File Templates.
 */
public class MLocationService {
    private Context mContext;

    private List<MLocate> mLocates;//所有的定位实例（图吧定位，基站定位……）
    private MLocationListener mLocationListener;
    private boolean isLocated = false;

    public MLocationService(Context context) {
        mContext = context;
        mLocates = MLocateFactory.createAllLocateInstance(mContext);
    }

    public static MLocationService create(Context context) {
        return new MLocationService(context);
    }

    public MLocationService setMLocationListener(MLocationListener listener) {
        mLocationListener = listener;
        return this;
    }

    public MLocationService setLocateTypes(MLocateFactory.LocateType...types) {
        mLocates = MLocateFactory.createLocates(mContext, types);
        return this;
    }

    //开始定位
    public void submit() {

        isLocated = false;

        startAllLocate();
    }

    //开启所有定位
    private void startAllLocate() {

        for (int i = 0; mLocates != null && i < mLocates.size(); i++) {

            MLocate locate = mLocates.get(i);

            locate.startLocate(new MLocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    onLocated(location);
                }
            });
        }
    }

    //关闭所有定位
    private void stopAllLocate() {
        for (int i = 0; mLocates != null && i < mLocates.size(); i++) {
            mLocates.get(i).stopLocate();
        }
    }

    //已经定位调用的方法
    private void onLocated(Location location) {

        if (!isLocated) {
            if (mLocationListener != null) {
                mLocationListener.onLocationChanged(location);
            }
            isLocated = true;
            stopAllLocate();
        } else {
            stopAllLocate();
        }
    }

}

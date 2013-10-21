package com.bee.common;

import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * checking network status & user online status
 * @author apple
 *
 */
public class StatueChecker {

    /**
     * 判断手机网络连接情况
     * @param context
     * @return
     */
    public boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            //mobile
            State mobile = connectivity.getNetworkInfo(0).getState();
            //wifi
            State wifi = connectivity.getNetworkInfo(1).getState();
            if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
                System.out.println("Dear user,your mobile network is availible");
            } else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
                System.out.println("Dear user,your wifi network is availible");
            }
            // 获取网络连接管理的对象
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                // 判断当前网络是否已经连接
                if (info.getState() == State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}

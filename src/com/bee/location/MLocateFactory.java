package com.bee.location;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:58
 * To change this template use File | Settings | File Templates.
 */
public class MLocateFactory {
    public static MLocate createLocateInstance(Context context, LocateType type) {

        MLocate locate = null;

        switch (type) {
            case cell_location :
                locate = new CellLocate(context);
                break;
            case gps_location :
                locate = new GPSLocate(context);
                break;

        }

        return locate;
    }

    //创建枚举类型中的所有定位实例
    public static List<MLocate> createAllLocateInstance(Context context) {

        List<MLocate> results = new ArrayList<MLocate>();

        for (LocateType type : LocateType.values()) {
            MLocate locate = createLocateInstance(context, type);
            results.add(locate);
        }

        return results;
    }

    public static List<MLocate> createLocates(Context context, LocateType...types) {

        List<MLocate> results = new ArrayList<MLocate>();

        if (types != null && types.length > 0) {

            for (int i = 0; i < types.length; i++) {
                MLocate locate = createLocateInstance(context, types[i]);
                results.add(locate);
            }
        }

        return results;
    }

    public enum LocateType {
        //基站
        cell_location,
        //gps
        gps_location,
    }
}

package com.bee.app.ui;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Base64;

import com.bee.app.bean.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: casec1517
 * Date: 13-2-23
 * Time: 下午4:25
 * To change this template use File | Settings | File Templates.
 */
public class LocationUtil {
	    Point latLngBvo;
    public Point change(String lat,String lng){
        latLngBvo=new Point();
        latLngBvo.setLat("");
        latLngBvo.setLng("");
        if(lat!=""&&lng!=""){
            System.out.println("before lat:"+lat);
            System.out.println("before lng:"+lng);
            String _url=String.format("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=%s&y=%s",lng,lat);
            System.out.println(_url);


			// TODO Auto-generated method stub
			StringBuffer sb = new StringBuffer();
			try {
				String json;                    /*= new URL(url).getContent().toString()*/
				java.net.URL mURL = new java.net.URL(_url);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						mURL.openStream()));

				String line;

				while ((line = in.readLine()) != null) {
					sb.append(line);
				}

				json = sb.toString();
				System.out.println("json:" + sb.toString());
				if (json != "") {
					JSONObject jsonObj = new JSONObject(json);
					Integer error = jsonObj.getInt("error");
					System.out.println("error:" + error);
					if (error.intValue() == 0) {
						String x = jsonObj.getString("x");
						String y = jsonObj.getString("y");
						System.out.println("before x:" + x);
						System.out.println("before y:" + y);
						x = decode(x);
						y = decode(y);
						latLngBvo.setLat(y);
						latLngBvo.setLng(x);
						System.out.println("after x:" + x);
						System.out.println("after y:" + y);
					}
				}
			} catch (IOException ioe) {

			} catch (JSONException jse) {

			}

			return latLngBvo;
		
        }
        return latLngBvo;
    }

    public String decode(String str){
        byte b[]=android.util.Base64.decode(str,Base64.DEFAULT);
        return new String(b);
    }


}

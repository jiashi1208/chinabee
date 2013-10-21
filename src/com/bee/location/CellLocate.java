package com.bee.location;

import android.content.Context;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-23
 * Time: 上午8:59
 * To change this template use File | Settings | File Templates.
 */
public class CellLocate  implements MLocate {
    //网络制式
    private static final String[] phoneTypeArray = {"NONE", "GSM", "CDMA"};
    //联网失败，最大重试次数
    private static final int MAX_FAILURE = 6;

    private Context mContext;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;

    private List<LocationTask> mTasks;

    private MLocationListener mLocationListener;
    private int mFailure = 0;

    private boolean debug = true;
    private void debug (String msg) {
        if (debug) {
            System.out.println("[--CellLocate--]  " + msg);
        }
    }

    public CellLocate(Context context) {
        mContext = context;
        mTasks = new ArrayList<LocationTask>();
        mTelephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void startLocate(MLocationListener ls) {

        mLocationListener = ls;
        if (mLocationListener == null) {
            throw new NullPointerException("MLocationListener must not be null");
        }

        mFailure = 0;
        beginLocate();
    }

    private void beginLocate() {

        mFailure++;
        debug("cell locate begin... the mFailure = " + mFailure);

        LocationTask task = new LocationTask();
        mTasks.add(task);
        task.execute();
    }

    @Override
    public void stopLocate() {

        debug("cell locate stop");

        for (int i = 0; mTasks != null && i < mTasks.size(); i++) {
            LocationTask t = mTasks.get(i);
            if (t != null && t.getStatus() != AsyncTask.Status.FINISHED) {
                t.cancel(true);
            }
        }
        mTasks.clear();
    }

    private class LocationTask extends AsyncTask<Void, Void, Location> {

        @Override
        protected Location doInBackground(Void... voids) {

            try {
                return getCellLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Location location) {

            if (location == null && mFailure <= MAX_FAILURE) {
                beginLocate();//从新请求定位
            } else {
                if (mLocationListener != null) {
                    mLocationListener.onLocationChanged(location);
                }
            }
        }
    }

    private Cell getCell() {

        Cell cell = new Cell();

        String phoneType = "";
        int index = mTelephonyManager.getPhoneType();
        if (index < phoneTypeArray.length) {
            phoneType = phoneTypeArray[index];
        }
        debug("phoneType is : " + phoneType);

        if ("CDMA".equals(phoneType)) {//CDMA
            CdmaCellLocation gcl = (CdmaCellLocation)mTelephonyManager.getCellLocation();
            if (gcl != null) {
                cell.cid = gcl.getBaseStationId();
                cell.lac = gcl.getNetworkId();
            }
        } else {//默认为GSM
            CellLocation obj = mTelephonyManager.getCellLocation();
            GsmCellLocation gcl = (obj == null ? null : (GsmCellLocation)obj);
            if (gcl != null) {
                cell.cid = gcl.getCid();//基站编号
                cell.lac = gcl.getLac();//地域代码
            }
        }

        String networkOperator = mTelephonyManager.getNetworkOperator();
        if (networkOperator != null && networkOperator.length() >= 5) {
            cell.mcc = Integer.parseInt(networkOperator.substring(0, 3));//mobile_country_code
            cell.mnc = Integer.parseInt(networkOperator.substring(3, 5));//mobile_network_code
        }
        debug("cid = " + cell.cid + ", lac = " + cell.lac + ", mcc = " + cell.mcc + ", mnc = " + cell.mnc);
        return cell;
    }

    private List<Wifi> getWifi() {

        List<Wifi> list = new ArrayList<Wifi>();

        //已经链接的wifi
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            Wifi wifi = new Wifi();
            wifi.mac = wifiInfo.getBSSID();
            wifi.ssid = wifiInfo.getSSID();
            wifi.level = wifiInfo.getRssi();
            list.add(wifi);
        }

        //其他能扫描到的wifi
        List<ScanResult> scanList = mWifiManager.getScanResults();
        for (int i = 0; scanList != null && i < scanList.size(); i++) {
            ScanResult result = scanList.get(i);
            Wifi wifi = new Wifi();
            wifi.mac = result.BSSID;
            wifi.ssid = result.SSID;
            wifi.level = result.level;
            list.add(wifi);
        }

        return list;
    }

    //基站定位
    private Location getCellLocation() throws Exception {

        JSONObject holder = new JSONObject();
        holder.put("version", "1.1.0");
        holder.put("host", "maps.google.com");//访问google服务器
        holder.put("request_address", false);

        Cell cell = getCell();//基站信息
        if (cell != null) {
            JSONArray array = new JSONArray();
            JSONObject data = new JSONObject();
            data.put("cell_id", cell.cid);
            data.put("location_area_code", cell.lac);
            data.put("mobile_country_code", cell.mcc);
            data.put("mobile_network_code", cell.mnc);
            array.put(data);
            holder.put("cell_towers", array);
        }

        List<Wifi> results = getWifi();//wifi信息
        if (results != null) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < results.size(); i++) {
                Wifi wifi = results.get(i);
                JSONObject data = new JSONObject();
                data.put("mac_address", wifi.mac);
                data.put("ssid", wifi.ssid);
                data.put("signal_strength", wifi.level);
                array.put(data);
            }
            holder.put("wifi_towers", array);
        }

        return getRemoteData(holder.toString());
    }

    //访问google服务器，获取远程数据
    private Location getRemoteData(String postData) throws IOException, JSONException {

        if (postData == null || postData.length() == 0) return null;

        BufferedReader bReader = null;
        try {
            HttpParams params = new BasicHttpParams();
            //设置连接超时
            HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
            //设置读取超时
            HttpConnectionParams.setSoTimeout(params, 60 * 1000);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

            DefaultHttpClient client = new DefaultHttpClient(cm, params);
            StringEntity se = new StringEntity(postData);
            HttpPost post = new HttpPost("http://www.google.com/loc/json");
            post.setEntity(se);

            HttpResponse resp = client.execute(post);
            if (resp != null && resp.getStatusLine().getStatusCode() != 200) {
                debug("request network status = " + resp.getStatusLine().getStatusCode());
                return null;
            }
            HttpEntity entity = resp.getEntity();

            bReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line = sb.toString();

            while ((line = bReader.readLine()) != null) {
                sb.append(line);
            }
            return analyzeData(sb.toString());
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                    bReader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //解析返回的json数据
    private Location analyzeData(String data) throws JSONException {

        debug("cell locate result string : " + data);

        if (data == null || data.length() == 0) return null;

        JSONObject json = new JSONObject(data);
        JSONObject jsonLocation = json.optJSONObject("location");

        if (jsonLocation != null) {
            Location location = new Location("");
            location.setLatitude(jsonLocation.optDouble("latitude"));
            location.setLongitude(jsonLocation.optDouble("longitude"));
            return location;
        }

        return null;

    }

    private class Cell {
        int cid;//基站号
        int lac;//地域编号
        int mcc;//mobile_county_code
        int mnc;//mobile_network_code
    }

    private class Wifi {
        String mac;//路由网卡地址
        String ssid;
        int level;//信号强度
    }
}

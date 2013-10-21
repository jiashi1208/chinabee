package com.bee.app.ui;

import com.bee.common.HttpUtil;


import android.util.Log;


/**
 * procing handler of check in operation
 *
 * @author apple
 */
public class CheckInProcHandler {

    protected static final String TAG = "CheckInProcHandler";

	public String login(String baseUrl, String userId, String passWord) {
        try {
            String url = /*HttpUtil.changeUrl(baseUrl)*/ baseUrl+ "/login.aspx?u=" + userId + "&p=" + passWord;
            Log.e("loginurl", url);
            String result;
            Log.d("LOGIN","URL     "+url);
            result = HttpUtil.queryStringForPost(url);
            Log.d("LOGIN","result  "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}

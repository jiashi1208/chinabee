package com.bee.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.bee.app.bean.BeeSource;
import com.bee.app.db.InfoService;
import com.bee.app.ui.InfoListActivity;

public class Upload {
	
	public static void uploadData(JSONObject sms){
		
		JSONArray data = new JSONArray();
        data.put(sms);
      /*  String _url = HttpUtil.changeUrl(url) + "/Default.aspx";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//                            System.out.println("data:\n" + data.toString());
        params.add(new BasicNameValuePair("data", data.toString()));
        getHttpClient();

        String result = doPost(_url, params);

        InfoBvo infoBvo = new InfoBvo();
        infoBvo.setA1(a1.getText().toString());
        infoBvo.setA2(a2.getText().toString());
        infoBvo.setA4(a4.getText().toString());
        infoBvo.setA5(_a5);
        infoBvo.setA6(a6.getText().toString());
        infoBvo.setA7(a7.getText().toString());
        infoBvo.setA8(_a8);
        infoBvo.setA9(a9.getText().toString());
        infoBvo.setA10(a10.getText().toString());
        infoBvo.setA11(a11.getText().toString());
        infoBvo.setA12(a12.getText().toString());
        infoBvo.setA13(a13.getText().toString());
        infoBvo.setA14(a14.getText().toString());
        infoBvo.setA15(a15.getText().toString());
        infoBvo.setA16(_a16);
        InfoService infoService = new InfoService(this);

        if ( result.equals("处理完成")) {
            infoBvo.setStatus(1);
        }

        if (this.id == null) {
            infoService.add(infoBvo);
        } else {
            infoService.update(infoBvo, this.id);
        }

//        locationManager.removeUpdates(locationListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("提示信息");
        builder.setMessage(result).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if(flag){
//                    Thread.currentThread().interrupt();
//                }
                Intent intent = new Intent(AddActivity2.this, InfoListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
		*/
	}
	
	public static void uploadImag(){
		
		
	}


}

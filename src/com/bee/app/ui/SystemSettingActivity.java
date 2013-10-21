package com.bee.app.ui;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;
import com.bee.base.BaseUi;
import com.bee.common.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SystemSettingActivity extends BaseUi {
    private Button btn;
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.system_config);
        btn = (Button)findViewById(R.id.button_save);
        edt = (EditText)findViewById(R.id.edit_text_save);

        SharedPreferences sp=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
        String url=sp.getString("url", "");
    
        edt.setText(url.substring(0, url.lastIndexOf(":")));
        

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edt.getText().toString() == null
						|| edt.getText().toString().equals("")) {
					Toast.makeText(SystemSettingActivity.this,
							"服务器地址不可为空,修改失败", Toast.LENGTH_SHORT).show();
					return;
				} else if ((edt.getText().toString()
						.matches("(^http://){0,1}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))) {

					String _url = edt.getText().toString();
					if (_url.indexOf("http://") < 0) {
						_url = "http://" + _url;
					}

					_url = _url + Constants.HTTP_PORT;

					SharedPreferences sp = getSharedPreferences(
							Constants.SETTINGS_NAME, MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("url", _url);
					editor.commit();
					AppContext appContext = (AppContext) SystemSettingActivity.this
							.getApplication();
					appContext.rawURL = _url;
					Toast.makeText(SystemSettingActivity.this, "修改服务器地址成功",
							Toast.LENGTH_SHORT).show();
					Intent mIntent = new Intent(SystemSettingActivity.this,
							Setting.class);
					startActivity(mIntent);
					SystemSettingActivity.this.finish();

				} else {

					Toast.makeText(SystemSettingActivity.this,
							"服务器地址格式不对,请重新设置！", Toast.LENGTH_SHORT).show();
					return;

				}
			}
		});

    }
	

}

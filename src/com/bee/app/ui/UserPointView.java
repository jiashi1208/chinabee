package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;
import com.bee.R.menu;
import com.bee.app.bean.UserPoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserPointView extends Activity {


	private TextView pointNameView;
	private TextView latView;
	private TextView lngView;
	private TextView timeView;
	private UserPoint userPoint;
	private TextView unitView;
	private ImageButton coloseBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_user_point_view);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		pointNameView=(TextView)findViewById(R.id.pointname);
		
		latView=(TextView)findViewById(R.id.lat);
		
		lngView=(TextView)findViewById(R.id.lng);
		
		timeView=(TextView)findViewById(R.id.time);
		
		unitView=(TextView)findViewById(R.id.unit);
		
		coloseBtn=(ImageButton)findViewById(R.id.login_close_button);
		
        coloseBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_point_view, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		userPoint=(UserPoint)getIntent().getExtras().get("userPoint");
		latView.setText(userPoint.getLat());
		lngView.setText(userPoint.getLng());
		pointNameView.setText(userPoint.getName());
		timeView.setText(userPoint.getAddTime());
		
		unitView.setText("已发送给 ："+userPoint.getUnitName());
		
		
	}
	
	
	

}

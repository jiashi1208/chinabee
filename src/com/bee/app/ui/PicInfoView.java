package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;
import com.bee.R.menu;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.UserPoint;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PicInfoView extends Activity {

	private ImageView imageView;
	private TextView imageNameView;
	private TextView pointView;
	private PicBean picBean;
	private TextView unitView;
	private ImageButton coloseBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_pic_info_view);	
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		imageView=(ImageView)findViewById(R.id.image);
		
		imageNameView=(TextView)findViewById(R.id.imagename);
		
		pointView=(TextView)findViewById(R.id.point);
		
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		picBean=(PicBean)getIntent().getExtras().get("picBean");
		imageView.setImageBitmap(BitmapFactory.decodeFile(picBean.getPath()));
		imageNameView.setText(picBean.getTitle());
		pointView.setText(picBean.getAddress());
		unitView.setText("已发送给 "+picBean.getUnitName());
		
		
	}

}

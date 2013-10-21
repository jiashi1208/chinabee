package com.bee.app.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.bee.R;
import com.bee.R.layout;
import com.bee.R.menu;
import com.bee.app.AppContext;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;
import com.bee.common.CompareID;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.BitmapFactory;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class BeeSourceView extends Activity {

	private TextView a1;
	private TextView a2;
	private TextView a4;
	private TextView a6;
	private TextView a7;
	private TextView a9;
	private TextView a10;
	private TextView a11;
	private TextView a12;
	private TextView a13;
	private TextView a14;
	private TextView a15;
	private BeeSource beeSource;
	private TextView a5;
	private TextView a8;
	private TextView a16;
	private AppContext appContext;
	private ArrayList<CItem> beeList;
	private ArrayList<CItem> materialList;
	private ArrayList<CItem> guiGeList;
	private ImageButton backBtn;
	private TextView unitView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_bee_source_view);
		
		appContext=(AppContext)getApplication();
		initView();
		
		beeList=appContext.loadType("beetype");
		if(beeList.size()<1){
			
			beeList=appContext.loadTypeFromAssets("beetype");
		}		
		Collections.sort(beeList,new CompareID());
		
		materialList=appContext.loadType("material");
		if(materialList.size()<1){
			
			materialList=appContext.loadTypeFromAssets("material");
		}
		Collections.sort(materialList,new CompareID());
		
		guiGeList=appContext.loadType("guige");
		if(guiGeList.size()<1){
			
			guiGeList=appContext.loadTypeFromAssets("guige");
		}      
		Collections.sort(guiGeList,new CompareID());

	}

	private void initView() {
		// TODO Auto-generated method stub
		

		unitView=(TextView)findViewById(R.id.unit);
		
		a1 = (TextView) findViewById(R.id.a1);
		a2 = (TextView) findViewById(R.id.a2);
		a4 = (TextView) findViewById(R.id.a4);

		a6 = (TextView) findViewById(R.id.a6);
		a7 = (TextView) findViewById(R.id.a7);

		a9 = (TextView) findViewById(R.id.a9);
		a10 = (TextView) findViewById(R.id.a10);
		a11 = (TextView) findViewById(R.id.a11);
		a12 = (TextView) findViewById(R.id.a12);
		a13 = (TextView) findViewById(R.id.a13);
		a14 = (TextView) findViewById(R.id.a14);
		a15 = (TextView) findViewById(R.id.a15);
		a5 = (TextView) findViewById(R.id.a5);  //原料
		a8 = (TextView) findViewById(R.id.a8); // 蜂种 
		a16 = (TextView) findViewById(R.id.a16); //规格
		
		backBtn=(ImageButton)findViewById(R.id.login_close_button);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BeeSourceView.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bee_source_view, menu);
		return true;
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		beeSource=(BeeSource)getIntent().getExtras().get("beeSource");
        a1.setText(beeSource.getA1());
        a2.setText(beeSource.getA2());
        a4.setText(beeSource.getA4());
        a6.setText(beeSource.getA6());
        
        a7.setText(beeSource.getA7());
        a9.setText(beeSource.getA9());
        a10.setText(beeSource.getA10());
        
        
        
        
        a11.setText(beeSource.getA11());
        a12.setText(beeSource.getA12());
        a13.setText(beeSource.getA13());
        
        a14.setText(beeSource.getA14());
        a15.setText(beeSource.getA15());
      
        
        
        if (beeSource.getA5() != null && beeSource.getA5() != "") {
            try {
                for(int i=0;i<materialList.size();i++){
                    if(materialList.get(i).GetID().equals(beeSource.getA5()))  {
        	   
                        a5.setText(materialList.get(i).GetValue());

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (beeSource.getA8() != null && beeSource.getA8() != "") {
            for(int i=0;i<guiGeList.size();i++){
                if(guiGeList.get(i).GetID().equals(beeSource.getA8()))  {
                	 a8.setText(guiGeList.get(i).GetValue());
                	 break;

                }
            }
        }

        if (beeSource.getA16() != null && beeSource.getA16() != "") {
            for(int i=0;i<beeList.size();i++){
                if(beeList.get(i).GetID().equals(beeSource.getA16()))  {
                	 a16.setText(beeList.get(i).GetValue());
                	 break;
                }
            }
        }
        
        unitView.setText("已发送给 ："+beeSource.getUnitName());
        
        	
	}

}

package com.bee.app.ui;

import java.util.ArrayList;

import com.bee.R;
import com.bee.R.layout;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainPage2 extends FragmentActivity {

	private static final String TAG = "chinabee";
	private ArrayList<View> pageViews;
	private ArrayList<ImageView> imageViews;
	private LinearLayout layout2;
	private ViewPager pager;
	private AppContext appContext;
	private InfoService infoService;
	private ProgressDialog refreshDialog;
	private ViewPager mPager;
	private MyAdapter mAdapter;
	private Button first_btn;
	private Button second_btn;
	
	static final int NUM_ITEMS = 2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG,"MainPage oncreate");	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_page);	
		
		mAdapter = new MyAdapter(getSupportFragmentManager());
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch(position){
				
				case 0:
					
					first_btn.setBackgroundColor(getResources().getColor(R.color.blue));
					second_btn.setBackgroundColor(getResources().getColor(R.color.white));
					break;
					
				case 1:
					first_btn.setBackgroundColor(getResources().getColor(R.color.white));
					second_btn.setBackgroundColor(getResources().getColor(R.color.blue));
					break;
				
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mPager.setAdapter(mAdapter);
		
		first_btn = (Button)findViewById(R.id.goto_first);
		first_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        second_btn = (Button)findViewById(R.id.goto_last);
        second_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(NUM_ITEMS-1);
            }
        });
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true).setTitle("提示信息")
					.setMessage("您确定要退出应用吗？");
			DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {

						getApplicationContext().sendBroadcast(
								new Intent("finish"));
						MainPage2.this.finish();
					} else if (which == DialogInterface.BUTTON_NEGATIVE) {
						dialog.dismiss();
					}
				}
			};
			builder.setPositiveButton("退出", l);
			builder.setNegativeButton("取消", l);
			builder.show();
		}
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d(TAG,"main page  onResume");
	//	getLocalActivityManager().dispatchResume();
	}
	
	public static class MyAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList;

		public MyAdapter(FragmentManager fm) {
            super(fm);
            
            fragmentList=new ArrayList<Fragment>();
            fragmentList.add(new OneMain());
            fragmentList.add(new TwoMain());
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
          //  return ArrayListFragment.newInstance(position);
        	
        	return fragmentList.get(position);
        }
        
        
        
    }

/*	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		
		if(fragment instanceof OneMain){
			
			Toast.makeText(this, "111111",Toast.LENGTH_SHORT).show();
		}else{
			
			Toast.makeText(this, "222222",Toast.LENGTH_SHORT).show();
			
		}
		super.onAttachFragment(fragment);
	}*/
	
	
	
	
}

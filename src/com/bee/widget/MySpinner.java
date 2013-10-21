package com.bee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class MySpinner extends Spinner {
	
	 public MySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private int currSelect=-1;  


	
	 @Override  
	    public void setSelection(int position) {  
	        currSelect=position;  
	        super.setSelection(position);  
	    } 
	 
	 @Override  
	    public void setSelection(int position, boolean animate) {  
	        currSelect=position;  
	        super.setSelection(position, animate);  
	    } 
	 
	 //把默认的返回的position改为自定义的postion  
	    @Override  
	    public int getSelectedItemPosition() {  
	        return currSelect;  
	    }  
	

}

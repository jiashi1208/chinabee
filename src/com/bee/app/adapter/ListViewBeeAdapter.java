package com.bee.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.Inflater;

import com.bee.R;
import com.bee.app.bean.BeeSource;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListViewBeeAdapter extends BaseAdapter {
	
	private Context context;
	private int beeListItem;
	private ArrayList<BeeSource> listdata;
	public HashMap<Integer, Boolean> isChecked;
	public HashSet<Integer> hasID;
	
	static class ListItemView{
		 public TextView souridView;
		 public TextView dateView;
		 public TextView statusView;
		 public CheckBox checkbox;
	}
	

	public ListViewBeeAdapter(Context context,
			int beeListitem, ArrayList<BeeSource> list) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.beeListItem=beeListitem;
		this.listdata=list;
		isChecked= new HashMap<Integer,Boolean>();
		hasID=new HashSet<Integer>();
		
		for (int i = 0; i < listdata.size(); i++) {
			isChecked.put(i, false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ListItemView listItemView=new ListItemView();
		// TODO Auto-generated method stub
		if(convertView==null){
		    			
			LayoutInflater layoutInflater=LayoutInflater.from(context);
			convertView=layoutInflater.inflate(beeListItem, null);
			
 			listItemView.souridView=(TextView)convertView.findViewById(R.id.sourceid);
 			
 			listItemView.dateView=(TextView)convertView.findViewById(R.id.date);
 					
 			listItemView.statusView=(TextView)convertView.findViewById(R.id.status);
 		
 			listItemView.checkbox=(CheckBox)convertView.findViewById(R.id.checkbox);

 			convertView.setTag(listItemView);
			
			
		}else{
			
			listItemView=(ListItemView)convertView.getTag();
		}	
		    listItemView.souridView.setText(listdata.get(position).getA1());

		    listItemView.dateView.setText(listdata.get(position).getEditTime());
		    String status=listdata.get(position).getStatus();
		    
		    listItemView.checkbox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isChecked.get(position)){
						
						isChecked.put(position, false);
						
					}else{
						isChecked.put(position, false);
					}
				}
			});
		    String statusString=null;
		    if (status=="0"){	    	
		    	statusString= "未发送";
		    	
		    }if (status=="1"){
		    	statusString= "已经发送";
		    	 listItemView.statusView.setTextColor(Color.RED);
		    }
		    
		    listItemView.statusView.setText(statusString);
		if (isChecked.get(position) == null) {
			listItemView.checkbox.setChecked(false);
		} else {
			listItemView.checkbox.setChecked(isChecked.get(position));
		}
	
		return convertView;
		
		
		
	}

}

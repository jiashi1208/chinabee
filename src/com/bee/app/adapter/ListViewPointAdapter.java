package com.bee.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.bee.R;
import com.bee.app.adapter.ListViewBeeAdapter2.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListViewPointAdapter extends BaseAdapter {
	
	
	
	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater inflater;

	public ListViewPointAdapter(ArrayList<HashMap<String, String>> list, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.point_listitem, null);
			holder.pointNameView = (TextView) convertView.findViewById(R.id.pointname);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		//	holder.statusView = (TextView) convertView.findViewById(R.id.status);
			holder.dateView=(TextView)convertView.findViewById(R.id.date);
			

			//添加
		//	holder.unitView = (TextView) convertView.findViewById(R.id.unit);
			//添加
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.pointNameView.setText(list.get(position).get("pointname").toString());
		//holder.statusView.setText(list.get(position).get("status").toString());
		holder.dateView.setText(list.get(position).get("date").toString());
		holder.checkbox.setChecked(list.get(position).get("flag").equals("true"));
		
	//	holder.unitView.setText(list.get(position).get("unit"));
		return convertView;
	
	}
	
	static class ViewHolder{
		 public TextView pointNameView;
		 public TextView dateView;
		 public TextView statusView;
		 public CheckBox checkbox;
		 public TextView unitView;
	}

}

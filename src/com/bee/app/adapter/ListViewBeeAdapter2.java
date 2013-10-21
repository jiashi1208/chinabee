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

public class ListViewBeeAdapter2 extends BaseAdapter {
	

	private int beeListItem;
	private ArrayList<BeeSource> listdata;
	public HashMap<Integer, Boolean> isChecked;
	public HashSet<Integer> hasID;
	
	
	// 填充数据的list
	private ArrayList<HashMap<String, String>> list;
	// 上下文
	private Context context;
	// 用来导入布局
	private LayoutInflater inflater = null;
	

	

/*	public ListViewBeeAdapter2(Context context,
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
	}*/

	public ListViewBeeAdapter2(ArrayList<HashMap<String, String>> list, Context context) {
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
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.bee_listitem2, null);
			holder.sourceidView = (TextView) convertView.findViewById(R.id.sourceid);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
	//		holder.statusView = (TextView) convertView.findViewById(R.id.status);
			
			//添加
			//holder.unitView = (TextView) convertView.findViewById(R.id.unit);
			//添加
			
			
			holder.dateView=(TextView)convertView.findViewById(R.id.date);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.sourceidView.setText(list.get(position).get("sourceid").toString());
	//	holder.statusView.setText(list.get(position).get("status").toString());
		holder.dateView.setText(list.get(position).get("edittime").toString());
		// 根据flag来设置checkbox的选中状况
		holder.checkbox.setChecked(list.get(position).get("flag").equals("true"));
		
	//	holder.unitView.setText(list.get(position).get("unitname"));
		
		return convertView;
	}
	
	static class ViewHolder{
		 public TextView sourceidView;
		 public TextView dateView;
		 public TextView statusView;
		 
		 public TextView unitView;
		 public CheckBox checkbox;
	}

}



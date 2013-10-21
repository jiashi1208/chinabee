package com.bee.app.adapter;

import java.util.List;

import com.bee.R;
import com.bee.app.bean.Position;
import com.bee.common.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewPosAdapter extends BaseAdapter {
	
	private Context 					context;         //运行上下文
	private List<Position> 					listItems;   //数据集合
	private LayoutInflater 				listContainer;   //视图容器
	private int 						itemViewResource;//自定义项视图源 
	static class ListItemView{				             //自定义控件集合  
	        public TextView title;  
		    public TextView pos;
		    public TextView date;  
		    public ImageView flag;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewPosAdapter(Context context, List<Position> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
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

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		//Log.d("method", "getView");
		
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.title = (TextView)convertView.findViewById(R.id.pos_listitem_title);
			listItemView.date= (TextView)convertView.findViewById(R.id.pos_listitem_date);
			listItemView.flag= (ImageView)convertView.findViewById(R.id.news_listitem_flag);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//设置文字和图片
		Position pos = listItems.get(position);
		
		listItemView.title.setText(pos.getTitle());
		listItemView.title.setTag(pos);//设置隐藏参数(实体类)
		listItemView.pos.setText(pos.getPos());
		listItemView.date.setText(StringUtils.friendly_time(pos.getDate()));

		if(StringUtils.isToday(pos.getDate()))
			listItemView.flag.setVisibility(View.VISIBLE);
		else
			listItemView.flag.setVisibility(View.GONE);
		
		return convertView;
	
	}

}

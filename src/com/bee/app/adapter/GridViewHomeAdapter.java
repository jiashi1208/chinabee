package com.bee.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bee.R;
import com.bee.app.bean.ImageInfo;

public class GridViewHomeAdapter extends BaseAdapter {
	
	Context mContext;
	ArrayList<ImageInfo> list;

	public GridViewHomeAdapter(Context context, ArrayList<ImageInfo> data) {
		// TODO Auto-generated constructor stub
		mContext=context;
		list=data;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = LayoutInflater.from(mContext).inflate(
				R.layout.main_grid_item, null);
		ImageView iv = (ImageView) item.findViewById(R.id.imageView);
		RelativeLayout relativeLayout = (RelativeLayout)item.findViewById(R.id.relativeLayout);
/*		relativeLayout.setBackgroundResource(list.get(position).bgId);
		relativeLayout.getBackground().setAlpha(255);*/
		iv.setImageResource((list.get(position)).imageId);
		TextView tv = (TextView) item.findViewById(R.id.msg);
		tv.setText((list.get(position)).imageMsg);
		return item;
	}

	
	

}

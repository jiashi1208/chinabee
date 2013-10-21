package com.bee.app.adapter;

import java.util.zip.Inflater;

import com.bee.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureGridCusorAdapter extends BaseAdapter {
		
	private Context context;
	private Cursor cursor;
	private int itemViewResource;
	private LayoutInflater layoutInflater;
	
	static class GridItemView{
		
		  public ImageView imageView;
		  public TextView title;
		
	}

	public PictureGridCusorAdapter(Context context,Cursor cursor,int resource){
		
		this.context=context;
		this.cursor=cursor;
		this.itemViewResource=resource;
		layoutInflater=LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
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
		GridItemView gridItemView=null;
		if(convertView==null){
			
			gridItemView = new GridItemView();
			
			convertView=layoutInflater.inflate(itemViewResource, null);
			
			gridItemView.imageView=(ImageView)convertView.findViewById(R.id.imageView);
			gridItemView.title=(TextView)convertView.findViewById(R.id.msg);
			
			convertView.setTag(gridItemView);
					
		}else{
			gridItemView=(GridItemView)convertView.getTag();
		}
		
	//	cursor.
		
		
		return convertView;
	}

}

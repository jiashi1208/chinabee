package com.bee.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bee.R;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.Picture;
import com.bee.common.Constants;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

//自定义适配器
public class PictureGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PicBean> picBeans;

    public PictureGridAdapter(String[] titles, String[] images, Context context) {
        super();
        picBeans = new ArrayList<PicBean>();
        inflater = LayoutInflater.from(context);
/*        for (int i = 0; i < images.length; i++) {
            Picture picture = new Picture(titles[i], images[i]);
            pictures.add(picture);
        }*/
    }

    public PictureGridAdapter(List<PicBean> picBeans,
			Context context) {
		// TODO Auto-generated constructor stub
    	
    	this.inflater = LayoutInflater.from(context);
    	this.picBeans=picBeans;
	}

	@Override
    public int getCount() {
        if (null != picBeans) {
            return picBeans.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return picBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	GridViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pic_item2, null);
            viewHolder = new GridViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
           // viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.addTime=(TextView)convertView.findViewById(R.id.addTime);
            viewHolder.checkbox = (ImageView) convertView.findViewById(R.id.checkbox);
            
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            
          //  viewHolder.unitView = (TextView) convertView.findViewById(R.id.unit);
            
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GridViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(picBeans.get(position).getTitle());
        viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(picBeans.get(
                position).getPath()));
        viewHolder.toggleButton.setChecked(picBeans.get(position).getIschecked());
        if(viewHolder.toggleButton.isChecked()){
        	
        	viewHolder.checkbox.setImageResource(R.drawable.checkbox_selected);
        }else if(!viewHolder.toggleButton.isChecked()){
        	viewHolder.checkbox.setImageResource(R.drawable.checkbox_disselected);
        }
        
/*        if(picBeans.get(position).getStatus().equals(Constants.NO_UPLOAD)){
        	viewHolder.status.setText("未上传");
        	
        }else if(picBeans.get(position).getStatus().equals(Constants.UPLOAD_SUCCESS)){
        	viewHolder.status.setText("已上传给");
        }*/
        
      //  viewHolder.unitView.setText(picBeans.get(position).getUnitName());
        
        viewHolder.addTime.setText(picBeans.get(position).getAddTime());
        return convertView;
    }

}

class GridViewHolder {
    public TextView title;
    public TextView status;
    public ImageView image;
    public ImageView checkbox;
    public ToggleButton toggleButton;
    public TextView unitView;
    public TextView addTime;
    
    
}

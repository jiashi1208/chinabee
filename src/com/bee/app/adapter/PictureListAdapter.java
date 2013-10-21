package com.bee.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bee.R;
import com.bee.app.bean.PicBean;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-12-12
 * Time: 上午8:36
 * To change this template use File | Settings | File Templates.
 */
//自定义适配器
public class PictureListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PicBean> pictures;

    public PictureListAdapter(String[] titles, String[] images, Context context) {
        super();
        pictures = new ArrayList<PicBean>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < images.length; i++) {
/*            Picture picture = new Picture(titles[i], images[i]);
            pictures.add(picture);*/
        }
    }

    @Override
    public int getCount() {
        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	PicViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pic_listitem, null);
            viewHolder = new PicViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PicViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(pictures.get(position).getTitle());
        viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(pictures.get(
                position).getPath()));
        return convertView;
    }

}

class PicViewHolder {
    public TextView title;
    public ImageView image;
    public CheckBox checkbox;
}


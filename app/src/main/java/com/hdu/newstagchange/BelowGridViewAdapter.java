package com.hdu.newstagchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 下方
 * Created by 612 on 2017/10/15.
 */

public class BelowGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private LayoutInflater inflater;

    private String TAG="lxz612 gridview";

    BelowGridViewAdapter(Context context, List<String> data){
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_normal,null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(data.get(position));
        return convertView;
    }

    class ViewHolder{
        private TextView tv;
    }
}

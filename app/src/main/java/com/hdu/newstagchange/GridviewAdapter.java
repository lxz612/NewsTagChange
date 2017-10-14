package com.hdu.newstagchange;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 612 on 2017/10/14.
 */

public class GridviewAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private LayoutInflater inflater;

    private String TAG="lxz612 gridview";

    GridviewAdapter(Context context,List<String> data){
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
        View v=inflater.inflate(R.layout.item,null);
        v.setTag(position);
        TextView tv= (TextView) v.findViewById(R.id.tv);
        tv.setText(data.get(position));
        return v;
    }
}

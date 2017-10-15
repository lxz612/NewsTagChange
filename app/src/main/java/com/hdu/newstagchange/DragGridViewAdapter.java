package com.hdu.newstagchange;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import static android.R.attr.id;

/**
 * 可拖动重排GridView的Adapter
 * Created by 612 on 2017/10/14.
 */

public class DragGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private LayoutInflater inflater;
    private int hidePos=-1;//需隐藏item的位置

    private String TAG="lxz612 gridview";

    DragGridViewAdapter(Context context, List<String> data){
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
            convertView=inflater.inflate(R.layout.item_del,null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(data.get(position));
        if (position==hidePos){
            //设置为拖动背景
            convertView.findViewById(R.id.tv).setBackground(context.getResources().getDrawable(R.drawable.tv_shape_drag));
            Log.i(TAG, "colorAccent***"+position);
        }else{
            //设置为正常背景
            convertView.findViewById(R.id.tv).setBackground(context.getResources().getDrawable(R.drawable.tv_shape_normal));
        }
        return convertView;
    }


    public void swapItem(int fromPos,int toPos){
        String tempStr = data.get(fromPos);
        //重排daata数据
        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else if (fromPos > toPos) {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        data.set(toPos,tempStr);
        //设置隐藏位置
        hideItem(toPos);
        //通知gridview更新
        this.notifyDataSetChanged();
    }

    //隐藏某个位置的item
    public void hideItem(int pos){
        this.hidePos=pos;
    }

    class ViewHolder{
        private TextView tv;
    }

}



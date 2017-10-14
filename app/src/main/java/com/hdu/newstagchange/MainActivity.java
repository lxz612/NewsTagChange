package com.hdu.newstagchange;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.logo;
import static android.R.attr.width;
import static com.hdu.newstagchange.R.id.top;
import static com.hdu.newstagchange.R.id.tv;


public class MainActivity extends AppCompatActivity{

    private GridView gv_top,gv_below;
    private List<String> ls_top_str=new ArrayList<String>();
    private List<String> ls_below_str=new ArrayList<String>();
    private String TAG="lxz612";
    private LayoutInflater inflater;

    private GridviewAdapter top_adapter,below_adapter;

    private RelativeLayout activity_main;

    private TextView tv_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv_top= (GridView) findViewById(R.id.gv_top);
        gv_below= (GridView) findViewById(R.id.gv_below);
        activity_main= (RelativeLayout) findViewById(R.id.activity_main);
        tv_top= (TextView) findViewById(R.id.tv_top);
        initData();
        top_adapter=new GridviewAdapter(this,ls_top_str);
        gv_top.setAdapter(top_adapter);
        below_adapter=new GridviewAdapter(this,ls_below_str);
        gv_below.setAdapter(below_adapter);

        inflater=LayoutInflater.from(this);

        gv_top.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s=ls_top_str.get(position);
                ls_top_str.remove(position);
                ls_below_str.add(s);

                top_adapter.notifyDataSetChanged();
                below_adapter.notifyDataSetChanged();

            }
        });


        gv_below.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=ls_below_str.get(position);
                ls_below_str.remove(position);
                ls_top_str.add(s);

                top_adapter.notifyDataSetChanged();
                below_adapter.notifyDataSetChanged();
            }
        });

//        final ScaleAnimation sa=new ScaleAnimation(1,1.2F,1,1.2F,
//                Animation.RELATIVE_TO_SELF,0.5F,
//                Animation.RELATIVE_TO_SELF,0.5F);
//        sa.setDuration(2000);
//        sa.setFillAfter(true);


        //设置GridView的事件分发机制
        gv_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        final View v=inflater.inflate(R.layout.item,activity_main,false);
        final TextView tv= (TextView) v.findViewById(R.id.tv);
        //设置View
        v.setOnTouchListener(new View.OnTouchListener() {
            private int lastX;
            private int lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int offsetX;
                int offsetY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 记录触摸点坐标
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算偏移量
                        offsetX = x - lastX;
                        offsetY = y - lastY;

                        // 在当前left、top、right、bottom的基础上加上偏移量
                        v.layout(v.getLeft() + offsetX,
                                v.getTop() + offsetY,
                                v.getRight() + offsetX,
                                v.getBottom() + offsetY);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 计算偏移量
                        offsetX = x - lastX;
                        offsetY = y - lastY;
//                        if(offsetX<width/2||offsetY<height/2){
//                            activity_main.removeView(v);
//                        }
                        break;
                }
                return true;
            }
        });

        //设置长按事件
//        gv_top.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "onItemLongClick: getTop:"+view.getTop());
//                Log.i(TAG, "onItemLongClick: getLeft:"+view.getLeft());
//
//                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                //获取view的相对父布局位置参数
//                int top=view.getTop();
//                int left=view.getLeft();
//                int tv_top_height=tv_top.getHeight();
//
//                //获取view的宽高
//                int width=view.getWidth();
//                int height=view.getHeight();
//
//                tv.setText(ls_top_str.get(position));
//                tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//
//                //添加悬浮View
//                RelativeLayout.LayoutParams pa=new RelativeLayout.LayoutParams(v.getLayoutParams());
//                pa.width= width;
//                pa.height=height;
//                pa.leftMargin=left;
//                pa.topMargin=top+tv_top_height;
//                activity_main.addView(v,pa);
//
//                return true;
//            }
//        });


    }

    private void initData(){
        ls_top_str.add("头条");
        ls_top_str.add("网易");
        ls_top_str.add("财经");
        ls_top_str.add("科技");
        ls_top_str.add("时尚");
        ls_top_str.add("直播");

        ls_below_str.add("图片");
        ls_below_str.add("影视");
        ls_below_str.add("彩票");
    }


    int fromP;
    int toP;


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus){
//            setLDragListener();
//        }
//    }
//
//    ClipData.Item item=new ClipData.Item("");
//    String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
//    ClipData dragData = new ClipData("", mimeTypes, item);
//    View.DragShadowBuilder myshadow;
//
//
//
//
//    public void setLDragListener(){
//
//        int size=gv_top.getCount();
//        for (int i=0;i<size;i++){
//
//            View g=gv_top.getChildAt(i);
//            TextView tv= (TextView) g.findViewById(R.id.tv);
//            Log.i(TAG, "setLDragListener: "+i+":"+tv.getText());
//            gv_top.getChildAt(i).setOnDragListener(new View.OnDragListener() {
//                @Override
//                public boolean onDrag(View v, DragEvent event) {
////                        TextView tv= (TextView) v.findViewById(R.id.tv);
////                        tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//
//                    switch (event.getAction()) {
//                        case DragEvent.ACTION_DRAG_STARTED:
//                            Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED"+v.getTag());
//                            // Do nothing
//                            break;
//                        case DragEvent.ACTION_DRAG_EXITED:
//                            Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED"+v.getTag());
//                            break;
//                        case DragEvent.ACTION_DRAG_ENTERED:
//                            Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED"+v.getTag());
////                                fromP= (int) v.getTag();
////                                Log.i(TAG, "onDrag:fromp "+fromP);
//                            break;
//                        case DragEvent.ACTION_DRAG_LOCATION:
//                            Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION"+v.getTag());
//                            toP=(int) v.getTag();
//                            Log.i(TAG, "onDrag:top "+toP);
//                            if (fromP!=toP){
//                                Collections.swap(ls_top_str,fromP,toP);
//                                top_adapter.notifyDataSetChanged();
//                                toP=fromP;
//                                setLDragListener();
//                                v.startDrag(dragData,myshadow,null,0);
//                                Log.i(TAG, "onDrag: 又设置了监听器");
//                            }
//                            break;
//                        case DragEvent.ACTION_DRAG_ENDED:
//                            Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED"+v.getTag());
//                            // Do nothing
//                            break;
//                        case DragEvent.ACTION_DROP:
//                            Log.d(TAG, "ACTION_DROP event"+v.getTag());
//                            // Do nothing
//
//                            break;
//                        default:
//                            break;
//                    }
//                    return true;
//                }
//            });
//        }
//    }
}

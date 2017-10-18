package com.hdu.newstagchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.hdu.newstagchange.R.id.gv_below;
import static com.hdu.newstagchange.R.id.gv_top;


public class MainActivity extends AppCompatActivity{

    private DragGridView gv_top;
    private GridView gv_below;
    private List<String> ls_top_str=new ArrayList<String>();
    private List<String> ls_below_str=new ArrayList<String>();
    private String TAG="lxz612";

    private DragGridViewAdapter top_adapter;
    private BelowGridViewAdapter below_adapter;

    private RelativeLayout activity_main;

    private TextView tv_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv_top= (DragGridView) findViewById(R.id.gv_top);
        gv_below= (GridView) findViewById(R.id.gv_below);
        activity_main= (RelativeLayout) findViewById(R.id.activity_main);
        tv_top= (TextView) findViewById(R.id.tv_top);
        initData();
        top_adapter=new DragGridViewAdapter(this,ls_top_str);
        gv_top.setAdapter(top_adapter);
        below_adapter=new BelowGridViewAdapter(this,ls_below_str);
        gv_below.setAdapter(below_adapter);

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

//        gv_top.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        //按下的位置
//                        int mDownX = (int) event.getX();
//                        int mDownY = (int) event.getY();
//                        int position=gv_top.pointToPosition(mDownX,mDownY);
//                        gv_top.addImageItem(position,ls_top_str.get(position));
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                }
//                return false;
//            }
//        });

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

        gv_top.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gv_top.addImageItem(position,ls_top_str.get(position));
                return false;
            }
        });
    }

    //初始化数据，Modle数据就是一个String
    private void initData(){
        ls_top_str.add("头条");
        ls_top_str.add("网易");
        ls_top_str.add("财经");
        ls_top_str.add("科技");
        ls_top_str.add("时尚");
        ls_top_str.add("直播");
        ls_top_str.add("热点");
        ls_top_str.add("股票");
        ls_top_str.add("十九大");
        ls_top_str.add("问吧");
        ls_top_str.add("NBA");
        ls_top_str.add("历史");
        ls_top_str.add("家居");
        ls_top_str.add("游戏");
        ls_top_str.add("航空");
        ls_top_str.add("社会");



        ls_below_str.add("图片");
        ls_below_str.add("影视");
        ls_below_str.add("彩票");
    }
}

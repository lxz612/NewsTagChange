package com.hdu.newstagchange;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 可拖动重排的GridView
 * Created by 612 on 2017/10/14.
 */
public class DragGridView extends GridView {

    private View imageItem; //镜像Item
    private GridView.LayoutParams params;//镜像Item布局参数
    private LayoutInflater inflater;
    private List<String> data;//GridView中数据
    private WindowManager manager;
    private WindowManager.LayoutParams  mWindowLayoutParams;
    private int statusBarHeight;//状态栏高度

    private int pointY,pointX; //按下的点到所在item的上边缘、和左边缘的距离

    private int screenY,screenX;//MyGridView距离屏幕最顶端（包括状态栏），和左边的距离

    private int dragPos;//镜像item对应的真实item位置

    private DragGridViewAdapter adpater;

    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        inflater=LayoutInflater.from(context);
        manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight=getStatusHeight(context);
    }

    //添加镜像Item
    public View addImageItem(int position,String str){
        //获取长按位置的真实Item
        View realItem=getChildAt(position);
        //realItem宽高
        int width=realItem.getWidth();
        int height=realItem.getHeight();
        //在屏幕位置
        int[] location=new int[2];
        realItem.getLocationOnScreen(location);

        
        imageItem=inflater.inflate(R.layout.item_del,null,false);
        TextView tv= (TextView) imageItem.findViewById(R.id.tv);
        tv.setBackground(getResources().getDrawable(R.drawable.tv_shape_normal));
        tv.setText(str);
        
        //设置镜像Item布局参数
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.height = height;
        mWindowLayoutParams.width =width;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        //x-镜像item的左边到屏幕最左边距离
        //y-镜像item的顶边到状态栏底边距离
        mWindowLayoutParams.x=location[0];
        mWindowLayoutParams.y=location[1]-statusBarHeight;


        manager.addView(imageItem,mWindowLayoutParams);

        //设置真实Item为拖动背景
        //TODO 这里有闪动的BUG
        realItem.findViewById(R.id.tv).setBackground(getResources().getDrawable(R.drawable.tv_shape_drag));
        Log.i(TAG, "colorAccent"+position);
        return imageItem;
    }

    //事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //事件拦截
    //设置位置参数
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //按下的位置
                int mDownX = (int) ev.getX();
                int mDownY = (int) ev.getY();

                //按下的那个item的position
                int tempPos = pointToPosition(mDownX, mDownY);
                View realItem=getChildAt(tempPos);

                //设置正在拖动item的位置
                dragPos=tempPos;

                if (realItem!=null){
                    //这些参数在移动过程中是固定的
                    pointY= mDownY - realItem.getTop();
                    pointX = mDownX - realItem.getLeft();
                    screenY = (int) (ev.getRawY() - mDownY);
                    screenX = (int) (ev.getRawX() - mDownX);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //事件处理(在这里实现Item的移动交换)
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (imageItem!=null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    //触摸位置到GridView左边和上边的距离
                    int moveX=(int)ev.getX();
                    int moveY=(int)ev.getY();
                    //拖动镜像Item并对GridView进行重排
                    onDragItem(moveX,moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    int dropX=(int)ev.getX();
                    int dropY=(int)ev.getY();
                    //放置view
                    onLayItem(dropX,dropY);
                    break;
            }
            return true;

        }
        return super.onTouchEvent(ev);
    }


    //拖动镜像Item并对GridView进行重排
    private void onDragItem(int moveX,int moveY){
        //设置移动之后window窗口的位置参数
        mWindowLayoutParams.x = moveX - pointX + screenX;
        mWindowLayoutParams.y = moveY - pointY + screenY - statusBarHeight;

        // 更新镜像item的位置
        if (imageItem!=null){
            manager.updateViewLayout(imageItem, mWindowLayoutParams);
        }

        //获取移动的到的item的位置
        final int toPos=pointToPosition(moveX,moveY);

        //当两者位置不一致时
        if(toPos!=dragPos&&toPos != AdapterView.INVALID_POSITION ){
            Log.i(TAG, "onDragItem: dragPos:"+dragPos+" toPos:"+toPos);
            //通知Adapter交换两个item位置
            adpater.swapItem(dragPos,toPos);
            dragPos=toPos;
        }
    }

    //停止拖动时，移除镜像Item，并设置realItem为正常背景
    private void onLayItem(int dropX, int dropY) {
        View view=getChildAt(dragPos);
        if (view!=null){
            view.findViewById(R.id.tv).setBackground(getResources().getDrawable(R.drawable.tv_shape_normal));
        }
        adpater.hideItem(-1);

        //移除镜像item
        if (imageItem!=null){
            manager.removeView(imageItem);
            imageItem=null;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adpater= (DragGridViewAdapter) adapter;
    }

    //获取状态栏高度
    private  int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}

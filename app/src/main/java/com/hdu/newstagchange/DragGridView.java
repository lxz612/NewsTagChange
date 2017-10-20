package com.hdu.newstagchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 可拖动重排的GridView
 * Created by 612 on 2017/10/14.
 */
public class DragGridView extends GridView {


    private WindowManager manager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private View imageItem; //镜像Item

    private int statusBarHeight;//状态栏高度
    private int pointY, pointX; //按下的点到所在item的上边缘、和左边缘的距离

    private int screenY, screenX;//MyGridView距离屏幕最顶端（包括状态栏）和左端的距离

    private int dragItemPos;//镜像item对应的真实item位置

    private boolean mAnimationEnd = true;//动画是否结束

    private DragGridViewAdapter dragGridViewAdapter;
    private LayoutInflater inflater;
    private List<String> data;//GridView中填充数据

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

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = getStatusHeight(context);
    }

    //设置可拖拽的镜像Item
    //位置，所在位置的内容
    public View setDragImageItem(int position, String str) {

        //设置正在拖动item的位置
        dragItemPos = position;

        //获取长按位置的真实Item
        View realItem = getChildAt(position);
        //realItem宽高
        int width = realItem.getWidth();
        int height = realItem.getHeight();
        //realItem在屏幕位置
        int[] location = new int[2];
        realItem.getLocationOnScreen(location);

        //创建镜像item
        imageItem = inflater.inflate(R.layout.item_del, null, false);
        TextView tv = (TextView) imageItem.findViewById(R.id.tv);
        tv.setText(str);

        //设置镜像Item布局参数
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.height = height;
        mWindowLayoutParams.width = width;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        //x-镜像item的左边到屏幕最左边距离
        //y-镜像item的顶边到状态栏底边距离
        mWindowLayoutParams.x = location[0];
        mWindowLayoutParams.y = location[1] - statusBarHeight;

        manager.addView(imageItem, mWindowLayoutParams);

        //设置真实Item为拖动背景
        //TODO 这里有闪动的BUG
        TextView tvv = (TextView) realItem.findViewById(R.id.tv);
        tvv.setBackground(getResources().getDrawable(R.drawable.tv_shape_drag));
        tvv.setText("");
        Log.i(TAG, "setDragImageItem:" + position);
        return imageItem;
    }

    //事件分发 分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //事件拦截 不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //设置位置参数
            case MotionEvent.ACTION_DOWN:
                //按下的位置
                int mDownX = (int) ev.getX();
                int mDownY = (int) ev.getY();

                //获得按下的那个item的在GridView中对应的位置
                int pos = pointToPosition(mDownX, mDownY);
                View realItem = getChildAt(pos);
                if (realItem != null) {
                    //这些参数在移动过程中是固定的
                    pointY = mDownY - realItem.getTop();
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
        if (imageItem != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    //触摸位置到GridView左边和上边的距离
                    int moveX = (int) ev.getX();
                    int moveY = (int) ev.getY();
                    //拖动镜像Item并对GridView进行重排
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    //获取触摸位置的绝对坐标
                    int dropX = (int) ev.getX();
                    int dropY = (int) ev.getY();
                    //放置view
                    onLayItem(dropX, dropY);
                    break;
            }
            return true;//将触摸事件拦截

        }
        return super.onTouchEvent(ev);
    }


    //拖动镜像Item并对GridView进行重排
    private void onDragItem(int moveX, int moveY) {
        //设置移动之后window窗口的位置参数
        mWindowLayoutParams.x = moveX - pointX + screenX;
        mWindowLayoutParams.y = moveY - pointY + screenY - statusBarHeight;

        // 更新镜像item的位置
        if (imageItem != null) {
            manager.updateViewLayout(imageItem, mWindowLayoutParams);
        }

        //获取移动的到的item的位置
        final int toPos = pointToPosition(moveX, moveY);

        //当两者位置不一致时
        if (toPos != dragItemPos && toPos != AdapterView.INVALID_POSITION && mAnimationEnd) {
            Log.i(TAG, "onDragItem: dragItemPos:" + dragItemPos + " toPos:" + toPos);
            //通知Adapter交换两个item位置
            dragGridViewAdapter.swapItem(dragItemPos, toPos);

            //TODO 在视图树绘制之前，设置Item交换动画
            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //调用之后立刻移除，否则会妨碍UI线程
                    observer.removeOnPreDrawListener(this);
                    setItemSwapAnimation(dragItemPos, toPos);
                    dragItemPos = toPos;
                    return true;
                }
            });
        }
    }



    //上下移动，从oldPos到NewPos，有多个item被移动
    //左右移动，从oldPos到NewPos，只有相邻item的移动
    private void setItemSwapAnimation(int oldPos, int newPos) {
        List<Animator> animations = new LinkedList<>();
        int mNumColumns=this.getNumColumns();
        boolean isForward = newPos > oldPos;
        if (isForward) {
            for (int pos = oldPos; pos < newPos; pos++) {
                View view = getChildAt(pos - getFirstVisiblePosition());
                if ((pos + 1) % mNumColumns == 0) {//重绘时最后一个item是下一行第一个item移动来的
                    animations.add(bindTranslationAnimations(view, -view.getWidth() * (mNumColumns - 1), 0, view.getHeight(), 0));
                } else {
                    animations.add(bindTranslationAnimations(view, view.getWidth(), 0, 0, 0));
                }
            }
        } else {
            for (int pos = oldPos; pos > newPos; pos--) {
                View view = getChildAt(pos);
                if ((pos + mNumColumns) % mNumColumns == 0) {//重绘时最后一个item是下一行第一个item移动来的
                    animations.add(bindTranslationAnimations(view, view.getWidth() * (mNumColumns - 1), 0, -view.getHeight(), 0));
                } else {
                    animations.add(bindTranslationAnimations(view, -view.getWidth(), 0, 0, 0));
                }
            }
        }

        //所有item动画一起发生
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animations);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationEnd = true;
            }
        });
        set.start();
    }

    //绑定item交换移动动画
    private AnimatorSet bindTranslationAnimations(View view, float startX, float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        //item组合动画
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    //停止拖动时，移除镜像Item，并设置realItem为正常背景
    private void onLayItem(int dropX, int dropY) {
        final View view = getChildAt(dragItemPos);
        if (view != null) {


            int realItemX = view.getLeft();
            int realItemY = view.getTop();
            //给真实Item设置动画
//            view.setVisibility(View.VISIBLE);//显示真实item
//            ObjectAnimator animX = ObjectAnimator.ofFloat(imageItem, "translationX", dropX - pointX - realItemX, 0);
//            ObjectAnimator animY = ObjectAnimator.ofFloat(imageItem, "translationY", dropY - pointY - realItemY, 0);
//            AnimatorSet animSetXY = new AnimatorSet();
//            animSetXY.playTogether(animX, animY);
//            animSetXY.setDuration(2000);
//            animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
//            animSetXY.start();
//            animSetXY.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    dragGridViewAdapter.hideItem(-1);
//                    //移除镜像item
//                    if (imageItem != null) {
//                        manager.removeView(imageItem);
//                        imageItem = null;
//                    }
//                }
//            });

            //给镜像Item设置动画
            ValueAnimator valueAnimatorX=ValueAnimator.ofInt(dropX - pointX + screenX,realItemX+screenX);
            valueAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mWindowLayoutParams.x= (int) animation.getAnimatedValue();
                    manager.updateViewLayout(imageItem,mWindowLayoutParams);
                }
            });

            ValueAnimator valueAnimatorY = ValueAnimator.ofInt(dropY - pointY + screenY - statusBarHeight, realItemY + screenY - statusBarHeight);
            valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mWindowLayoutParams.y= (int) animation.getAnimatedValue();
                    manager.updateViewLayout(imageItem,mWindowLayoutParams);
                }
            });
            AnimatorSet animatorSet=new AnimatorSet();
            animatorSet.playTogether(valueAnimatorX, valueAnimatorY);
            animatorSet.start();
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dragGridViewAdapter.hideItem(-1);
                    //移除镜像item
                    if (imageItem != null) {
                        manager.removeView(imageItem);
                        imageItem = null;
                    }
                    //显示真实item
                    view.setVisibility(View.VISIBLE);
                    ((TextView)view.findViewById(R.id.tv)).setBackground(getResources().getDrawable(R.drawable.tv_shape_normal));
                    data=dragGridViewAdapter.getData();
                    ((TextView)view.findViewById(R.id.tv)).setText(data.get(dragItemPos));
                }
            });
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.dragGridViewAdapter = (DragGridViewAdapter) adapter;
    }


    //获取状态栏高度
    private int getStatusHeight(Context context) {
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

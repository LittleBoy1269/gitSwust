package com.listview;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Scroller;

public class MySlideListView extends MyListview {

	/**
	 * 当前滑动listview的position
	 * @param context
	 */
	private int slidePosition;
	/**
	 * 手指按下的Y坐标
	 */
	private int downY;
	/**
	 * 手指按下的X坐标
	 */
	private int downX;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	/**
	 * Listview的item元素
	 */
	private View itemView;
	/**
	 * 滑动类
	 */
	private Scroller scroller;
	private static final int SNAP_VELOCITY=600;
	/**
	 * 速度追踪的对象
	 */
	private VelocityTracker velocityTracker;
	/**
	 * 是否响应滑动，默认为不滑动
	 */
	private boolean isSlide=false;
	/**
	 * 用户删除的最小滑动距离
	 */
	private int mTouchSlop;
	/**
	 * 移除item后回调的接口
	 */
	private RemoveListener mRemoveListener;
	/**
	 * 用来只是item滑出屏幕的方向，向左还是向右，用一个枚举值来标记
	 */
	private RemoveDirection removeDirection;
	/**
	 * 滑动方向的枚举值
	 */
	public enum RemoveDirection{
		RIGHT,LEFT;
	}
	
	public MySlideListView(Context context) {
		this(context,null);
	}
	public MySlideListView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public MySlideListView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		screenWidth=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
	    scroller=new Scroller(context);
	    mTouchSlop=ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	/**
	 *  数值滑动删除的回调接口
	 */
	public void  setRemoveListener(RemoveListener removeListener) {
		this.mRemoveListener=removeListener;
	}
	/**
	 * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			addVelocityTracker(event);
			//如果Scroller滚动还没有结束，我们直接返回
			if(!scroller.isFinished()){
				return super.dispatchTouchEvent(event);
			}
			downY=(int) event.getY();
			downX=(int) event.getX();
			
			slidePosition=pointToPosition(downX, downY);
			//无效的position，不做任何处理
			if(slidePosition==AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(event);
			}
			//获取我们点击的item
			itemView= getChildAt(slidePosition-getFirstVisiblePosition());
			break;
		}
		case MotionEvent.ACTION_MOVE:{
			if(Math.abs(getScrollVelocity())>SNAP_VELOCITY
					|| (Math.abs(event.getX()-downX)>mTouchSlop && 
							Math.abs(event.getY()-downY)<mTouchSlop)){
				isSlide=true;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		}
		return super.dispatchTouchEvent(event);
		
	}
	
	/**
	 * 向右滑动，getScrollX()返回与左边缘的距离，向右滑动为负值
	 */

	private void scrollRight(){
		removeDirection=RemoveDirection.RIGHT;
		final int delta=(screenWidth+itemView.getScrollX());
		//调用startScroll的方法来设置一些滚动参数，在computeScroll方法调用scrollTo来滚动Item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,Math.abs(delta));
		postInvalidate();//刷新listview
	}
	
	/**
	 * 向左滑动，为正值
	 */
	private void scrollLeft(){
		removeDirection=RemoveDirection.LEFT;
		final int delta=(screenWidth-itemView.getScrollX());
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,Math.abs(delta));
		postInvalidate();
	}
	
	/**
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX(){
		//如果向左滑动的距离大于屏幕的三分之一，让其删除
		if(itemView.getScrollX()>=screenWidth/3){
			scrollLeft();
		}
		else if(itemView.getScrollX()<=-screenWidth/3){
			scrollRight();
		}
		else {
			//滚回到原始位置
			itemView.scrollTo(0, 0);
		}
	}
	/**
	 * 处理我们拖动ListView item的逻辑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			addVelocityTracker(ev);
			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				int deltaX = downX - x;
				downX = x;

				// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
				itemView.scrollBy(deltaX, 0);
				break;
			case MotionEvent.ACTION_UP:
				int velocityX = getScrollVelocity();
				if (velocityX > SNAP_VELOCITY) {
					scrollRight();
				} else if (velocityX < -SNAP_VELOCITY) {
					scrollLeft();
				} else {
					scrollByDistanceX();
				}

				recycleVelocityTracker();
				// 手指离开的时候就不响应左右滚动
				isSlide = false;
				break;
			}

			return true; // 拖动的时候ListView不滚动
		}

		//否则直接交给ListView来处理onTouchEvent事件
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (scroller.computeScrollOffset()) {
			// 让ListView item根据当前的滚动偏移量进行滚动
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			
			postInvalidate();

			// 滚动动画结束的时候调用回调接口
			if (scroller.isFinished()) {
				if (mRemoveListener == null) {
					throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
				}
				
				itemView.scrollTo(0, 0);
				mRemoveListener.removeItem(removeDirection, slidePosition);
			}
		}
	}
	/**
	 * 添加用户的速度跟踪器
	 * 
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}

		velocityTracker.addMovement(event);
	}

	/**
	 * 移除用户的速度跟踪器
	 */
	private void recycleVelocityTracker(){
		if(velocityTracker!=null){
			velocityTracker.recycle();
			velocityTracker=null;
		}
	}
	/**
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}
	/**
	 * 当Listview滑出屏幕，回调这个接口
	 * 我们在回调方法removeItem()中移除该Item，然后刷新ListView
	 * @author Eva-Beta
	 *
	 */
	public interface RemoveListener{
		public void removeItem(RemoveDirection direction,int position);
	}
}

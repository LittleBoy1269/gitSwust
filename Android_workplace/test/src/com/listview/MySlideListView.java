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
	 * ��ǰ����listview��position
	 * @param context
	 */
	private int slidePosition;
	/**
	 * ��ָ���µ�Y����
	 */
	private int downY;
	/**
	 * ��ָ���µ�X����
	 */
	private int downX;
	/**
	 * ��Ļ�Ŀ��
	 */
	private int screenWidth;
	/**
	 * Listview��itemԪ��
	 */
	private View itemView;
	/**
	 * ������
	 */
	private Scroller scroller;
	private static final int SNAP_VELOCITY=600;
	/**
	 * �ٶ�׷�ٵĶ���
	 */
	private VelocityTracker velocityTracker;
	/**
	 * �Ƿ���Ӧ������Ĭ��Ϊ������
	 */
	private boolean isSlide=false;
	/**
	 * �û�ɾ������С��������
	 */
	private int mTouchSlop;
	/**
	 * �Ƴ�item��ص��Ľӿ�
	 */
	private RemoveListener mRemoveListener;
	/**
	 * ����ֻ��item������Ļ�ķ������������ң���һ��ö��ֵ�����
	 */
	private RemoveDirection removeDirection;
	/**
	 * ���������ö��ֵ
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
	 *  ��ֵ����ɾ���Ļص��ӿ�
	 */
	public void  setRemoveListener(RemoveListener removeListener) {
		this.mRemoveListener=removeListener;
	}
	/**
	 * �ַ��¼�����Ҫ�������жϵ�������Ǹ�item, �Լ�ͨ��postDelayed��������Ӧ���һ����¼�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			addVelocityTracker(event);
			//���Scroller������û�н���������ֱ�ӷ���
			if(!scroller.isFinished()){
				return super.dispatchTouchEvent(event);
			}
			downY=(int) event.getY();
			downX=(int) event.getX();
			
			slidePosition=pointToPosition(downX, downY);
			//��Ч��position�������κδ���
			if(slidePosition==AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(event);
			}
			//��ȡ���ǵ����item
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
	 * ���һ�����getScrollX()���������Ե�ľ��룬���һ���Ϊ��ֵ
	 */

	private void scrollRight(){
		removeDirection=RemoveDirection.RIGHT;
		final int delta=(screenWidth+itemView.getScrollX());
		//����startScroll�ķ���������һЩ������������computeScroll��������scrollTo������Item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,Math.abs(delta));
		postInvalidate();//ˢ��listview
	}
	
	/**
	 * ���󻬶���Ϊ��ֵ
	 */
	private void scrollLeft(){
		removeDirection=RemoveDirection.LEFT;
		final int delta=(screenWidth-itemView.getScrollX());
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,Math.abs(delta));
		postInvalidate();
	}
	
	/**
	 * ������ָ����itemView�ľ������ж��ǹ�������ʼλ�û�������������ҹ���
	 */
	private void scrollByDistanceX(){
		//������󻬶��ľ��������Ļ������֮һ������ɾ��
		if(itemView.getScrollX()>=screenWidth/3){
			scrollLeft();
		}
		else if(itemView.getScrollX()<=-screenWidth/3){
			scrollRight();
		}
		else {
			//���ص�ԭʼλ��
			itemView.scrollTo(0, 0);
		}
	}
	/**
	 * ���������϶�ListView item���߼�
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

				// ��ָ�϶�itemView����, deltaX����0���������С��0���ҹ�
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
				// ��ָ�뿪��ʱ��Ͳ���Ӧ���ҹ���
				isSlide = false;
				break;
			}

			return true; // �϶���ʱ��ListView������
		}

		//����ֱ�ӽ���ListView������onTouchEvent�¼�
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		// ����startScroll��ʱ��scroller.computeScrollOffset()����true��
		if (scroller.computeScrollOffset()) {
			// ��ListView item���ݵ�ǰ�Ĺ���ƫ�������й���
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			
			postInvalidate();

			// ��������������ʱ����ûص��ӿ�
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
	 * ����û����ٶȸ�����
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
	 * �Ƴ��û����ٶȸ�����
	 */
	private void recycleVelocityTracker(){
		if(velocityTracker!=null){
			velocityTracker.recycle();
			velocityTracker=null;
		}
	}
	/**
	 * ��ȡX����Ļ����ٶ�,����0���һ�������֮����
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}
	/**
	 * ��Listview������Ļ���ص�����ӿ�
	 * �����ڻص�����removeItem()���Ƴ���Item��Ȼ��ˢ��ListView
	 * @author Eva-Beta
	 *
	 */
	public interface RemoveListener{
		public void removeItem(RemoveDirection direction,int position);
	}
}

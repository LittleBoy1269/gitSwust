package com.listview;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.test.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MyListview extends ListView implements OnScrollListener{

	/**
	 * 添加上部下拉刷新
	 */
	View header;
	int headerHeight;
	int firstVisbleItem;//第一个可见的item的位置；
	int scrollState;
	boolean isRemark;//标记是否是最顶端按下
	int startY;//按下时的Y值
	int state;//当前的状态；
	final int NONE=0;//正常状态
	final int PULL=1;//提示下拉状态
	final int RELESE=2;//提示释放状态
	final int REFLASHING=3;//刷新状态
	IReflashListener iReflashListener;//刷新数据接口，在mainactivity中实现
	
	/**
	 * 添加下拉分页功能
	 * @param context
	 */
	View footer;
	boolean isBottom;//是否在最低端
	int totalItemCount;//listview的总数量
	int lastVisbleItem;//最后一个元素
	IBottomListener iBottomListener;
	public MyListview(Context context) {
		super(context);
		initView(context);
	}
    
	public MyListview(Context context,AttributeSet attr){
		super(context,attr);
		initView(context);
	}
	
	public MyListview(Context context,AttributeSet attr , int defStyle)
	{
		super(context, attr, defStyle);
		initView(context);
	}
	/**
	 * 初始化界面
	 * @param context
	 */
	private void initView(Context context){
		LayoutInflater inflagter=LayoutInflater.from(context);
	    header=inflagter.inflate(R.layout.header_layout, null);
	    measureView(header);
	    headerHeight=header.getMeasuredHeight();
	    topPadding(-headerHeight);
	    footer=inflagter.inflate(R.layout.footer_layout,null);
	    footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
	    this.addFooterView(footer);
	    this.addHeaderView(header);
	    this.setOnScrollListener(this);
	    
	    
	}
	/**
	 * 将下拉刷新头添加到头部
	 * @param view
	 */
	private void measureView(View view){
		ViewGroup.LayoutParams p=view.getLayoutParams();
		if(p==null){
			p=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width=ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight=p.height;
		if(tempHeight>0){
			height=MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		}
		else {
			height=MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	private void topPadding(int topPadding){
		header.setPadding(header.getPaddingLeft(), topPadding, 
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
	/**
	 * 捕捉手指在屏幕上的活动
	 */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	switch(ev.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		if(firstVisbleItem==0){
    			isRemark=true;
    			startY=(int)ev.getY();
    		}
    		break;
    		
    	case MotionEvent.ACTION_MOVE:
    		onMove(ev);
    		break;
    	
    	case MotionEvent.ACTION_UP:
    		if(state==RELESE){
    			state=REFLASHING;
    			reflashViewByState();
    			iReflashListener.onReflash();
    		}
    		else if(state==PULL){
    			state=NONE;
    			isRemark=false;
    			reflashViewByState();
    		}
    	}
    	return super.onTouchEvent(ev);
    }
	/**
	 * Move中的状态
	 * @param ev
	 */
    
    private void onMove(MotionEvent ev){
    	if(!isRemark){
    		return ;
    	}
    	int tempY=(int) ev.getY();
    	int space=tempY-startY;
    	int topPadding = space - headerHeight;
    	switch (state){
    	case NONE:
    		if(space>0){
    			state=PULL;
    			reflashViewByState();
    		}
    		break;
    	case PULL:
    		topPadding(topPadding);
    		if(space>headerHeight+20&&
    				scrollState==SCROLL_STATE_TOUCH_SCROLL){
    			state=RELESE;
    			reflashViewByState();
    		}
    		break;
    	case RELESE:
    		topPadding(topPadding);
    		if(space<headerHeight+20){
    			state=PULL;
    			reflashViewByState();
    		}
    		else if(space<=0){
    			state=NONE;
    			isRemark=false;
    			reflashViewByState();
    		}
    		break;
    	}
    }
    /**
     * 根据当前状态，改变界面的显示：
     */
    private void reflashViewByState(){
    	TextView tip=(TextView) header.findViewById(R.id.tip);
    	ImageView arrow=(ImageView) header.findViewById(R.id.arrow);
    	ProgressBar progress=(ProgressBar) header.findViewById(R.id.progress);
    	RotateAnimation anim=new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, 
    			RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    	anim.setDuration(500);
    	anim.setFillAfter(true);
    	RotateAnimation animl=new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, 
    			RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    	anim.setDuration(500);
    	anim.setFillAfter(true);
    	switch(state){
    	case NONE:
    		arrow.clearAnimation();
    		topPadding(-headerHeight);
    		break;
    		
    	case PULL:
    		arrow.setVisibility(View.VISIBLE);
    		progress.setVisibility(View.GONE);
    		tip.setText("下拉刷新！");
    		arrow.clearAnimation();
    		arrow.setAnimation(anim);
    		break;
    	
    	case RELESE:
    		arrow.setVisibility(View.VISIBLE);
    		progress.setVisibility(View.GONE);
    		arrow.clearAnimation();
    		arrow.setAnimation(anim);
    		break;
    		
    	case REFLASHING:
    		topPadding(50);
    		arrow.setVisibility(View.GONE);
    		progress.setVisibility(View.VISIBLE);
    		tip.setText("正在刷新...");
    		arrow.clearAnimation();
    		break;
    	}
    }
    /**
     * 上拉刷新加载完毕
     */
    public void reflashComplete(){
    	state=NONE;
        isRemark=false;
        reflashViewByState();
        TextView lastupdatetime=(TextView) header.findViewById(R.id.lastupdate);
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String time=format.format(date);
        lastupdatetime.setText(time);
    }
    /**
	 * 刷新数据接口
	 * @author Kai
	 */
	public interface IReflashListener{
		public void onReflash();
	}
	public void setInterface(IReflashListener TopListener,IBottomListener BottomListener){
    	this.iReflashListener=TopListener;
    	this.iBottomListener=BottomListener;
    }
    /**
     * 下拉分页加载完毕
     */
    public void onBottomComplete(){
    	isBottom=false;
    	footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
    	
    }
    
	/**
	 * 底部分页加载接口
	 * @author Kai
	 */
     public interface IBottomListener{
    	 public void onBottom();
     }
     @Override
 	public void onScroll(AbsListView view, int firstVisbleItem, int visibleItemCount, int totalItemCount) {
 		this.firstVisbleItem=firstVisbleItem;
 		this.lastVisbleItem=firstVisbleItem+visibleItemCount;
 		this.totalItemCount=totalItemCount;
 	}

 	@Override
 	public void onScrollStateChanged(AbsListView view, int scrollState) {
 		this.scrollState=scrollState;
 		if(totalItemCount==lastVisbleItem&&
 				scrollState==SCROLL_STATE_IDLE)
 		{
 			if(!isBottom){
 				isBottom=true;
 				footer.findViewById(R.id.bottom_layout).setVisibility(view.VISIBLE);
 				iBottomListener.onBottom();
 			}
 		}
 	}
}

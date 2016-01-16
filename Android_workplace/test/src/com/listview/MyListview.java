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
	 * ����ϲ�����ˢ��
	 */
	View header;
	int headerHeight;
	int firstVisbleItem;//��һ���ɼ���item��λ�ã�
	int scrollState;
	boolean isRemark;//����Ƿ�����˰���
	int startY;//����ʱ��Yֵ
	int state;//��ǰ��״̬��
	final int NONE=0;//����״̬
	final int PULL=1;//��ʾ����״̬
	final int RELESE=2;//��ʾ�ͷ�״̬
	final int REFLASHING=3;//ˢ��״̬
	IReflashListener iReflashListener;//ˢ�����ݽӿڣ���mainactivity��ʵ��
	
	/**
	 * ���������ҳ����
	 * @param context
	 */
	View footer;
	boolean isBottom;//�Ƿ�����Ͷ�
	int totalItemCount;//listview��������
	int lastVisbleItem;//���һ��Ԫ��
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
	 * ��ʼ������
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
	 * ������ˢ��ͷ��ӵ�ͷ��
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
	 * ��׽��ָ����Ļ�ϵĻ
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
	 * Move�е�״̬
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
     * ���ݵ�ǰ״̬���ı�������ʾ��
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
    		tip.setText("����ˢ�£�");
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
    		tip.setText("����ˢ��...");
    		arrow.clearAnimation();
    		break;
    	}
    }
    /**
     * ����ˢ�¼������
     */
    public void reflashComplete(){
    	state=NONE;
        isRemark=false;
        reflashViewByState();
        TextView lastupdatetime=(TextView) header.findViewById(R.id.lastupdate);
        SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String time=format.format(date);
        lastupdatetime.setText(time);
    }
    /**
	 * ˢ�����ݽӿ�
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
     * ������ҳ�������
     */
    public void onBottomComplete(){
    	isBottom=false;
    	footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
    	
    }
    
	/**
	 * �ײ���ҳ���ؽӿ�
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

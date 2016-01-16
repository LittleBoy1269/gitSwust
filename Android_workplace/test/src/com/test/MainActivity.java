package com.test;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.adapter.MyAdapter;
import com.entity.MyEntity;
import com.listview.MySlideListView.RemoveDirection;
import com.listview.MyListview.IBottomListener;
import com.listview.MyListview.IReflashListener;
import com.listview.MySlideListView;
import com.listview.MySlideListView.RemoveListener;


public class MainActivity extends Activity implements IReflashListener,IBottomListener,RemoveListener {

	private MySlideListView my_listview;
	private MyAdapter MyAdapter;
	private ArrayList<MyEntity> de_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setData();
        showList(de_list);
    }
    private void showList(ArrayList<MyEntity>de_list){
    	if(MyAdapter==null){
    		my_listview=(MySlideListView) findViewById(R.id.my_listview);
        	my_listview.setInterface(this,this);
        	my_listview.setRemoveListener(this);
        	MyAdapter=new MyAdapter(this,de_list);
            my_listview.setAdapter(MyAdapter);
    	}
    	else{
    		MyAdapter.onDataChange(de_list);
    	}
    }
    private void setData(){
    	de_list=new ArrayList<MyEntity>();
    	for(int i=0;i<10;i++){
    		MyEntity entity=new MyEntity();
    		entity.setTitle("米饭");
    		entity.setContent("好久好久没吃米饭");
    		de_list.add(entity);
    	}	
    }
    private void setReflashData(){
    	for(int i=0;i<2;i++){
    		MyEntity entity=new MyEntity();
    		entity.setTitle("上面的新米饭");
    		entity.setContent("哇哈哈哈哈哈哈");
    		de_list.add(0,entity);
    	}
    }
    private void setOnBottomData(){
    	for(int i=0;i<2;i++)
    	{
    		MyEntity entity=new MyEntity();
    		entity.setTitle("下面的新米饭");
    		entity.setContent("哇哈哈哈哈哈哈");
    		de_list.add(entity);
    	}
    }
    /**
     * 上拉刷新界面
     */
	@Override
	public void onReflash() {
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setReflashData();
				showList(de_list);
				my_listview.reflashComplete();
			}
		}, 2000);
	}
	/**
	 * 下拉加载数据
	 */
	@Override
	public void onBottom() {
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setOnBottomData();
				showList(de_list);
				my_listview.onBottomComplete();
			}
		}, 2000);
	}
	//滑动删除之后的回调方法
		@Override
		public void removeItem(RemoveDirection direction, int position) {
			if(position<de_list.size()){
				de_list.remove(position);
				showList(de_list);
				switch (direction) {
				case RIGHT:
					Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
					break;
				case LEFT:
					Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
			//当滑动的位置为底部footer时，就删除上一个数据
			//底部layout_footer也会算listview
			else if(position==de_list.size())
			{
				de_list.remove(position-1);
				showList(de_list);
				switch (direction) {
				case RIGHT:
					Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
					break;
				case LEFT:
					Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		}	
}

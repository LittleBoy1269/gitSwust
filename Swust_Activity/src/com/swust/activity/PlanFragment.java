package com.swust.activity;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.swust.http.SendMessage;
import com.swust.model.PersonCalender;
import com.swust.model.SchoolCalender;
import com.swust.notepad.DatabaseManage;
import com.swust.notepad.DatabaseHelper;
import com.swust.notepad.ListViewAdapter;
import com.swust.util.ConstantField;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class PlanFragment extends Fragment implements OnScrollListener
{
	private ImageView AddPlanImg;
	//用于表示当前界面是属于哪种状态
	public static final int CHECK_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int ALERT_STATE = 2;
		
		
	private ListView listView;
	private ListViewAdapter adapter;// 数据源对象
	private View longClickView ;///长按弹出的布局
	
		
	private Button deleteRecordButton;//删除
	private Button checkRecordButton;//查看
	private Button modifyRecordButton;//修改
		
	private DatabaseManage dm = null;// 数据库管理对象
	private Cursor cursor = null;
		
	private int id = -1;//被点击的条目
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View MyPlanView=inflater.inflate(R.layout.my_plan, container, false);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				// 获取日历列表
//				JSONObject res = obtainCalender();
//				try {
//					List<PersonCalender> personCalender = obtainPersonCalender_list(res);
//				} catch (Exception e) {
//					
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
		AddPlanImg=(ImageView) MyPlanView.findViewById(R.id.addPlan);
		longClickView = inflater.inflate(R.layout.long_click,null);
		deleteRecordButton = (Button)
        		longClickView.findViewById(R.id.deleteRecordButton);
        checkRecordButton = (Button)
        		longClickView.findViewById(R.id.checkRecordButton);
        modifyRecordButton = (Button)
        		longClickView.findViewById(R.id.modifyRecordButton);
        dm = new DatabaseManage(this.getActivity());//数据库操作对象
        listView=(ListView) MyPlanView.findViewById(R.id.listview);
        initAdapter();//初始化
        listView.setAdapter(adapter);//自动为id为list的ListView设置适配器
        
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	cursor.moveToPosition(position);//listview中加入header就从header开始算了
        		Intent intent = new Intent();       		
        		intent.putExtra("state", CHECK_STATE);
        		intent.putExtra("id", cursor.getString(cursor.getColumnIndex("_id")));
        		intent.putExtra("title", cursor.getString(cursor.getColumnIndex("title")));
        		intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
        		intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
        		//dm.close();
    			intent.setClass(getActivity(), com.swust.notepad.NotepadCheckActivity.class);
    			getActivity().startActivity(intent);

            }
        });
                   
        
        
        //设置滑动监听器
        listView.setOnScrollListener(this);
        listView.setOnCreateContextMenuListener(new myOnCreateContextMenuListener());
        
        
        //设置按钮监听器
        AddPlanImg.setOnClickListener(new AddPlanListener());//添加计划
        deleteRecordButton.setOnClickListener(new DeleteRecordListener());//删除
        checkRecordButton.setOnClickListener(new CheckRecordListener());//查看
        modifyRecordButton.setOnClickListener(new ModifyRecordListener());//修改
		return MyPlanView;
	}
	
	//初始化数据源
	public void initAdapter(){
	    	
	    	dm.open();//打开数据库操作对象
	    	
	    	cursor = dm.selectAll();//获取所有数据
	    	
	    	cursor.moveToFirst();//将游标移动到第一条数据，使用前必须调用
	    	
	    	int count = cursor.getCount();//个数
	    	
	    	ArrayList<String> items = new ArrayList<String>();
	    	ArrayList<String> times = new ArrayList<String>();
	    	for(int i= 0; i < count; i++){
	    		items.add(cursor.getString(cursor.getColumnIndex("title")));
	    		times.add(cursor.getString(cursor.getColumnIndex("time")));
	    		cursor.moveToNext();//将游标指向下一个
	    	}
	   // 	cursor.close();
	    	dm.close();//关闭数据操作对象
	    	adapter = new ListViewAdapter(getActivity(),items,times);//创建数据源
	   }
	@Override
	public void onDestroyView() {
		cursor.close();//关闭游标
		super.onDestroyView();
	}
	
	/*
	@Override
	protected void onDestroy() {//销毁Activity之前，所做的事
		// TODO Auto-generated method stub
		cursor.close();//关闭游标
		super.onDestroy();
	}
	*/
	//滑动事件
	public void onScrollStateChanged1(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}



	public void onScroll1(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	
	//长按
		public class myOnCreateContextMenuListener implements OnCreateContextMenuListener{

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				final AdapterView.AdapterContextMenuInfo info = 
						(AdapterView.AdapterContextMenuInfo) menuInfo;
				menu.setHeaderTitle("");
				//设置选项
				menu.add(0,0,0,"删除");
				menu.add(0,1,0,"修改");
				menu.add(0,2,0,"查看");
			}
			
		}
		
		
		
		//响应长按弹出菜单的点击事件
		public boolean onContextItemSelected(MenuItem item){
			AdapterView.AdapterContextMenuInfo menuInfo = 
					(AdapterView.AdapterContextMenuInfo)item
					.getMenuInfo();
			dm.open();
		//	cursor = dm.selectAll();
			switch(item.getItemId()){
			case 0://删除
				
				try{
					
					cursor.moveToPosition(menuInfo.position);
					int i = dm.delete(Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))));//删除数据
					adapter.removeListItem(menuInfo.position);//删除数据
					adapter.notifyDataSetChanged();//通知数据源，数据已经改变，刷新界面
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				break;
			case 1://修改
			//	Log.v("show", "chenggong2");
				try{
					cursor.moveToPosition(menuInfo.position);
					
					//用于Activity之间的通讯
					Intent intent = new Intent();
					//通讯时的数据传送
					intent.putExtra("id", cursor.getString(cursor.getColumnIndex("_id")));
					intent.putExtra("state", ALERT_STATE);
					intent.putExtra("title", cursor.getString(cursor.getColumnIndex("title")));
					intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
					intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
					//设置并启动另一个指定的Activity
					intent.setClass(this.getActivity(), com.swust.notepad.NotepadEditActivity.class);
					this.getActivity().startActivity(intent);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				break;
			case 2://查看
			//	Log.v("show", "chenggong3");
				try{
					cursor.moveToPosition(menuInfo.position);
					
					Intent intent = new Intent();
					
					intent.putExtra("id", cursor.getString(cursor.getColumnIndex("_id")));
					intent.putExtra("title", cursor.getString(cursor.getColumnIndex("title")));
					intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
					intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
					intent.setClass(this.getActivity(), com.swust.notepad.NotepadCheckActivity.class);
					this.getActivity().startActivity(intent);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				break;
				default:;
			}
		//	cursor.close();
			dm.close();
			return super.onContextItemSelected(item);
			
		}
		
	//短按，即点击
		/*
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			
//			Log.v("position", position+"");
//			Log.v("id", id+"");
			
			cursor.moveToPosition(position);
			
			Intent intent = new Intent();
			
			intent.putExtra("state", CHECK_STATE);
			intent.putExtra("id", cursor.getString(cursor.getColumnIndex("_id")));
			intent.putExtra("title", cursor.getString(cursor.getColumnIndex("title")));
			intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
			intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
			
		//	cursor.close();
			dm.close();
			intent.setClass(this.getActivity(), com.swust.notepad.NotepadCheckActivity.class);
			this.getActivity().startActivity(intent);
			
		}
		*/
	
	public class AddPlanListener implements OnClickListener{

		public void onClick(View v) {
			startActivity(new Intent(getActivity(), com.swust.notepad.NotepadEditActivity.class));
		}
		
	}	
	
	public class DeleteRecordListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class CheckRecordListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class ModifyRecordListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
//	private JSONObject obtainCalender() {
//		JSONObject req = new JSONObject();
//		JSONObject res;
//		try {
//			req.put(ConstantField.USERID, com.swust.load.LoadActivity.getUserid());
//			res = SendMessage.sendMessage(
//					"obtainPersonActivityAction.action", req);
//
//			if (res != null) {
//				int code = res.getInt(ConstantField.STATUS);
//				if (code == 0)
//					return res;
//				else
//					return null;
//			} else
//				return null;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private List<PersonCalender> obtainPersonCalender_list(JSONObject res)
//			throws Exception {
//		List<PersonCalender> personCalenderlist = new ArrayList<PersonCalender>();
//		if (res == null)
//			return null;
//		try {
//			JSONArray personCalenderSt = (JSONArray) res
//					.get(ConstantField.PERSONCALENDER_LIST);
//			if ( personCalenderSt.length() > 0)
//
//				for (int i = 0; i < personCalenderSt.length(); i++) {
//					PersonCalender personCalender = new PersonCalender();
//					JSONObject json_data = personCalenderSt.getJSONObject(i);
//					personCalender = (PersonCalender) fromJsonToJava(json_data,
//							PersonCalender.class);
//					personCalenderlist.add(personCalender);
//				}
//
//			else
//				personCalenderlist = null;
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return personCalenderlist;
//	}
//
//	
//
//	private Object fromJsonToJava(JSONObject json, Class pojo) throws Exception {
//		// 首先得到pojo所定义的字段
//		Field[] fields = pojo.getDeclaredFields();
//		// 根据传入的Class动态生成pojo对象
//		Object obj = pojo.newInstance();
//		for (Field field : fields) {
//			// 设置字段可访问（必须，否则报错）
//			field.setAccessible(true);
//			// 得到字段的属性名
//			String name = field.getName();
//			// 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
//			try {
//				json.get(name);
//			} catch (Exception ex) {
//				continue;
//			}
//			if (json.get(name) != null && !"".equals(json.getString(name))) {
//				// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
//				if (field.getType().equals(Long.class)
//						|| field.getType().equals(long.class)) {
//					field.set(obj, Long.parseLong(json.getString(name)));
//				} else if (field.getType().equals(String.class)) {
//					field.set(obj, json.getString(name));
//				} else if (field.getType().equals(Double.class)
//						|| field.getType().equals(double.class)) {
//					field.set(obj, Double.parseDouble(json.getString(name)));
//				} else if (field.getType().equals(Integer.class)
//						|| field.getType().equals(int.class)) {
//					field.set(obj, Integer.parseInt(json.getString(name)));
//				} else if (field.getType().equals(java.util.Date.class)) {
//					field.set(obj, Date.parse(json.getString(name)));
//				} else {
//					continue;
//				}
//			}
//		}
//		return obj;
//	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}

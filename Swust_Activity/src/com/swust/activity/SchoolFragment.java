package com.swust.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.util.Code;
import com.swust.delicious.DDatabaseManage;
import com.swust.delicious.DListViewAdapter;
import com.swust.delicious.DeliciousActivity;
import com.swust.delicious.DeliciousEntity;
import com.swust.http.SendMessage;
import com.swust.model.GoodFood;
import com.swust.model.GoodFoodList;
import com.swust.model.PersonCalender;
import com.swust.model.SchoolCalender;
import com.swust.schoolplan.ApkEntity;
import com.swust.schoolplan.ReFlashListView;
import com.swust.schoolplan.ReFlashListView.IReflashListener;
import com.swust.schoolplan.SDatabaseManage;
import com.swust.schoolplan.SListViewAdapter;
import com.swust.util.ConstantField;

public class SchoolFragment extends Fragment
{
	private ListView School_listview;//操作校园活动的listview
	private ListView Delicious_listview;//操作校园美食的listview
	private ImageView Add_Image;//图片添加活动和美食
	private View SchoolView;
	private View TopSchoolView;
	private TextView top_activity;
	private TextView top_delicious;
	private SListViewAdapter Sadapter=null;//校园活动listview适配器
	private DListViewAdapter Dadapter=null;
	private int top_state=1; //代表当前为活动还是美食状态，默认为 1 是校园美食，2为校园活动
	private Integer Userid=null;
	List<DeliciousEntity> delicious_list;
	List<ApkEntity> apk_list;
	private final int SUCCESS=0;
	private final int ERROR=1;
	private Handler handler =new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:

				delicious_list = (List<DeliciousEntity>) msg.obj;
				Collections.sort(delicious_list, myComparator);
				Dadapter = new DListViewAdapter(getActivity(),delicious_list,Delicious_listview);//创建数据源
				Delicious_listview.setAdapter(Dadapter);//自动为id为list的ListView设置适配器
				break;
			case ERROR:
				//Toast.makeText(getActivity(), "获取列表失败", 0).show();
				break;
			default:
				break;
			}
		}
	};
	private Handler handler_=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			       case SUCCESS:
			    	apk_list=(List<ApkEntity>) msg.obj;
				    Sadapter = new SListViewAdapter(getActivity(), apk_list);
					School_listview.setAdapter(Sadapter);
			       case ERROR:
						//Toast.makeText(getActivity(), "获取列表失败", 0).show();
						break;
					default:
						break;
					}
				}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
	    
		SchoolView=inflater.inflate(R.layout.school_plan, container, false);
		top_delicious= (TextView) SchoolView.findViewById(R.id.ac_top_delicious);
		top_activity= (TextView) SchoolView.findViewById(R.id.ac_top_activity);
		Add_Image=(ImageView) SchoolView.findViewById(R.id.addSchoolActivity);
		School_listview= (ListView) SchoolView.findViewById(R.id.school_listview);
		//School_listview.setInterface(this);
		Delicious_listview=(ListView) SchoolView.findViewById(R.id.delicious_listview);
		//Delicious_listview.setInterface(this);
		initEvents();
		
		
		Delicious_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent = new Intent();
        		intent.putExtra("imgUrl", delicious_list.get(position).getUrl());
        		intent.putExtra("title", delicious_list.get(position).getTitle());
        		intent.putExtra("keyword", delicious_list.get(position).getKeyword());
        		intent.putExtra("average", delicious_list.get(position).getAverage());
        		intent.putExtra("place", delicious_list.get(position).getPlace());
        		intent.putExtra("detail", delicious_list.get(position).getDetail());
        		intent.putExtra("foodid",delicious_list.get(position).getFoodid());
    			intent.setClass(getActivity(), com.swust.delicious.DeliciousActivity.class);
    			getActivity().startActivity(intent);
    			
            }
        });
//		Intent intent=new Intent();
//		isLoad=intent.getIntExtra("isLoad", isLoad);
		Add_Image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Userid=com.swust.load.LoadActivity.getUserid();
//				Userid=1;
			if(Userid==null)
			{
				Toast.makeText(getActivity(), "请登录", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getActivity(),com.swust.load.LoadActivity.class);
				startActivity(intent);
			}
			else{
					Intent intent=new Intent(getActivity(),com.swust.delicious.AddDelicious.class);
					startActivity(intent);
//				else
//				{
//					Intent intent=new Intent(getActivity(),com.swust.schoolplan.AddActivity.class);
//					startActivity(intent);
//			    }
			  }
			}
		});
		top_delicious.setOnClickListener(new ChangeToDeliciousList());
		top_activity.setOnClickListener(new ChangeToActivityList());
		
		return SchoolView;
	}
	
	public void initEvents()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取日历列表
				JSONObject res = obtainGoodFood();
				try {
					List<GoodFood> foodlist = obtainGoodFood_list(res);
					List<DeliciousEntity> De_list=showFoodList(foodlist);
					
					
					Message msg = new Message();
					Message msg_= new Message();
					if (De_list != null) {
						msg.what = SUCCESS;
						msg.obj = De_list;
					}
					
					else{
						msg.what = ERROR;
					}
					handler.sendMessage(msg);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取日历列表
				JSONObject res_= obtainCalender();
				try {
					
					List<SchoolCalender> activitylist=obtainSchoolCalender_list(res_);
					List<ApkEntity> Ac_list=showSchoolList(activitylist);
					
					
					Message msg_= new Message();
					if (Ac_list !=null) {
						msg_.what = SUCCESS;
						msg_.obj= Ac_list;
					}
					
					else{
						msg_.what = ERROR;
					}
					handler_.sendMessage(msg_);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public List<DeliciousEntity> showFoodList(List<GoodFood> foodlist)
	{
		List<DeliciousEntity> de_list = new ArrayList<DeliciousEntity>();
		for(int i=0;i<foodlist.size();i++)
		{
			DeliciousEntity entity=new DeliciousEntity();
			entity.setTitle(foodlist.get(i).getName());
			entity.setKeyword(foodlist.get(i).getFoodType());
			entity.setAverage(foodlist.get(i).getPersonPrice());
			entity.setPlace(foodlist.get(i).getPlace());
			entity.setDetail(foodlist.get(i).getDes());
			entity.setUrl(foodlist.get(i).getUrl());
			entity.setFoodid(foodlist.get(i).getFoodid());
			entity.setcommentSum(foodlist.get(i).getCommentsum());
			de_list.add(entity);
		}
//			byte[] in=d_cursor.getBlob(d_cursor.getColumnIndex("dimg"));  
//			Bitmap bmpout=BitmapFactory.decodeByteArray(in,0,in.length);  
//			d_entity.setDimg(bmpout);
		return de_list;	
	}
	
	public List<ApkEntity> showSchoolList(List<SchoolCalender> activitylist)
	{
		List<ApkEntity> ac_list=new ArrayList<ApkEntity>();
		for(int i=0;i<activitylist.size();i++)
		{
			ApkEntity entity=new ApkEntity();
			entity.setTitle(activitylist.get(i).getName());
			entity.setPerson(activitylist.get(i).getHolder());
			entity.setTime(activitylist.get(i).getStart_time());
			entity.setPlace(activitylist.get(i).getPlace());
			entity.setDetail(activitylist.get(i).getDes());
			entity.setUrl(activitylist.get(i).getUrl());
			ac_list.add(entity);
		}
		return ac_list;
	}



//	private void setReflashData() {
//		if(top_state==1)
//		{
//			for (int i = 0; i < 2; i++) {
//				DeliciousEntity d_entity = new DeliciousEntity();
//				d_entity.setTitle("刷新数据");
//			    d_entity.setKeyword("这是一个难吃的菜");
//				d_entity.setAverage("人均100元");
//				d_entity.setPlace("东十二B223");
//				delicious_list.add(0,d_entity);
//			}
//		}
//		if(top_state==2)
//		{
//			for (int i = 0; i < 2; i++) {
//				ApkEntity a_entity = new ApkEntity();
//				a_entity.setTitle("刷新数据");
//				a_entity.setPerson("这是一个神奇的应用");
//				a_entity.setTime("2015-11-7");
//				a_entity.setPlace("东六A501");
//				apk_list.add(0,a_entity);
//			}
//		}
//		
//	}
//	@Override
//	public void onReflash() {
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//获取最新数据
//				setReflashData();
//				//通知界面显示
//				if(top_state==1)
//				{
//					School_listview.setAdapter(Sadapter);
//					Sadapter.onDateChange(apk_list);
//				}
//				if(top_state==2)
//				{
//					Delicious_listview.setAdapter(Dadapter);
//					Dadapter.onDateChange(delicious_list);
//				}
//				//通知listview 刷新数据完毕；
//				School_listview.reflashComplete();
//				Delicious_listview.reflashComplete();
//			}
//		}, 2000);
//		
//	}

	
	
	public class ChangeToDeliciousList implements OnClickListener{

		public void onClick(View v) {
			if(top_state==1)return ;
			else{
				
				top_state=1;
				top_delicious.setTextColor(getResources().getColor(R.color.mainBack));
				
				top_delicious.setBackgroundResource(R.color.white);
				top_activity.setTextColor(getResources().getColor(R.color.white));
				if (Build.VERSION.SDK_INT <= 16) {
				    top_activity.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
				}
				else{
					top_activity.setBackground(getResources().getDrawable(R.drawable.border));
				}
				
				top_activity.setPadding(10, 10, 10, 10);
				top_delicious.setPadding(10, 10, 10, 10);
				if(School_listview.getVisibility()==v.VISIBLE
						&& Delicious_listview.getVisibility()==v.INVISIBLE)
				{
					School_listview.setVisibility(v.INVISIBLE);
					Delicious_listview.setVisibility(v.VISIBLE);
					Delicious_listview.setOnItemClickListener(new OnItemClickListener() {

			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id) {
			            	Intent intent = new Intent();
			            	intent.putExtra("imgUrl", delicious_list.get(position).getUrl());
			        		intent.putExtra("title", delicious_list.get(position).getTitle());
			        		intent.putExtra("keyword", delicious_list.get(position).getKeyword());
			        		intent.putExtra("average", delicious_list.get(position).getAverage());
			        		intent.putExtra("place", delicious_list.get(position).getPlace());
			        		intent.putExtra("detail", delicious_list.get(position).getDetail());
			        		intent.putExtra("foodid",delicious_list.get(position).getFoodid());
			        		//dm.close();
			    			intent.setClass(getActivity(), com.swust.delicious.DeliciousActivity.class);
			    			getActivity().startActivity(intent);
			    			
			            }
			        });
				}
				
			}
		}
		
	}	
	
	public class ChangeToActivityList implements OnClickListener{

		public void onClick(View v) {
			if(top_state==2)return ;
			else{
				top_state=2;
				if (Build.VERSION.SDK_INT <= 16) {
					top_delicious.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
				}
				else{
					top_delicious.setBackground(getResources().getDrawable(R.drawable.border));
				}
				top_delicious.setTextColor(getResources().getColor(R.color.white));
				//top_delicious.setBackground(getResources().getDrawable(R.drawable.border));
				top_activity.setTextColor(getResources().getColor(R.color.mainBack));
				top_activity.setBackgroundResource(R.color.white);
				top_activity.setPadding(10, 10, 10, 10);
				top_delicious.setPadding(10, 10, 10, 10);
				if(School_listview.getVisibility()==v.INVISIBLE 
						&& Delicious_listview.getVisibility()==v.VISIBLE)
				{
					School_listview.setVisibility(v.VISIBLE);
					Delicious_listview.setVisibility(v.INVISIBLE);

					School_listview.setOnItemClickListener(new OnItemClickListener() {

			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id) {
			            	Intent intent = new Intent();
			            	intent.putExtra("imgUrl", apk_list.get(position).getUrl());
			        		intent.putExtra("title", apk_list.get(position).getTitle());
			        		intent.putExtra("person",apk_list.get(position).getPerson());
			        		intent.putExtra("time", apk_list.get(position).getTime());
			        		intent.putExtra("place", apk_list.get(position).getPlace());
			        		intent.putExtra("detail", apk_list.get(position).getDetail());
			        		//dm.close();
			    			intent.setClass(getActivity(), com.swust.schoolplan.SchoolActivity.class);
			    			getActivity().startActivity(intent);
			    			
			            }
			        });
				}
			}
		}
		
	}	
	
	
	private JSONObject obtainCalender() {
		JSONObject req = new JSONObject();
		JSONObject res;
		try {
			req.put(null,null);
			res = SendMessage.sendMessage(
					"ObtainCalenderActivityAction.action", req);

			if (res != null) {
				int code = res.getInt(ConstantField.STATUS);
				if (code == 0)
					return res;
				else
					return null;
			} else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONObject obtainGoodFood() {
		JSONObject req = new JSONObject();
		JSONObject res;
		try {
			req.put(null,null);
			res = SendMessage.sendMessage(
					"obtainGoodFoodAction.action", req);

			if (res != null) {
				int code = res.getInt(ConstantField.STATUS);
				if (code == 0)
					return res;
				else
					return null;
			} else
				return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	private List<SchoolCalender> obtainSchoolCalender_list(JSONObject res)
			throws Exception {
		List<SchoolCalender> schoolCalenderlist = new ArrayList<SchoolCalender>();
		if (res == null)
			return null;
		try {
			JSONArray schoolCalenderSt = (JSONArray) res
					.get(ConstantField.SCHOOLCALENDER_LIST);
			if (schoolCalenderSt.length() > 0)
				for (int i = 0; i < schoolCalenderSt.length(); i++) {
					SchoolCalender schoolCalender = new SchoolCalender();
					JSONObject json_data = schoolCalenderSt.getJSONObject(i);
					schoolCalender = (SchoolCalender) fromJsonToJava(json_data,
							SchoolCalender.class);
					schoolCalenderlist.add(schoolCalender);
				}
			else
				schoolCalenderlist = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return schoolCalenderlist;
		//返回校园活动的列表项
	}
	private String readString(InputStream is)
	{
		InputStreamReader isr;
		String result = "";
		try {
			String line = "";
			//字节流转换为字符流
			isr = new InputStreamReader(is,"utf-8");		
			//以Buffer的形式读取
			BufferedReader br = new BufferedReader(isr);
			while((line = br.readLine()) != null)
			{
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private List<GoodFood> obtainGoodFood_list(JSONObject res)
	        throws Exception{
		List<GoodFood> goodfoodlist=new ArrayList<GoodFood>();
		if(res==null)
			return null;
		try{
			JSONArray foodListSt=(JSONArray) res.get(ConstantField.FOOD_LIST);
			if(foodListSt.length()>0)
				for(int i=0;i<foodListSt.length();i++){
					GoodFood FoodList=new GoodFood();
					JSONObject json_data=foodListSt.getJSONObject(i);
					FoodList=(GoodFood) fromJsonToJava(json_data,
							GoodFood.class);
					goodfoodlist.add(FoodList);
				}
			else
				goodfoodlist=null;
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return goodfoodlist;
		//返回校园美食的列表项
	}

	private Object fromJsonToJava(JSONObject json, Class pojo) throws Exception {
		// 首先得到pojo所定义的字段
		Field[] fields = pojo.getDeclaredFields();
		// 根据传入的Class动态生成pojo对象
		Object obj = pojo.newInstance();
		for (Field field : fields) {
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);
			// 得到字段的属性名
			String name = field.getName();
			// 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
			try {
				json.get(name);
			} catch (Exception ex) {
				continue;
			}
			if (json.get(name) != null && !"".equals(json.getString(name))) {
				// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
				if (field.getType().equals(Long.class)
						|| field.getType().equals(long.class)) {
					field.set(obj, Long.parseLong(json.getString(name)));
				} else if (field.getType().equals(String.class)) {
					field.set(obj, json.getString(name));
				} else if (field.getType().equals(Double.class)
						|| field.getType().equals(double.class)) {
					field.set(obj, Double.parseDouble(json.getString(name)));
				} else if (field.getType().equals(Integer.class)
						|| field.getType().equals(int.class)) {
					field.set(obj, Integer.parseInt(json.getString(name)));
				}
				else if (field.getType().equals(java.util.Date.class)) {
					field.set(obj, Date.parse(json.getString(name)));
				} else {
					continue;
				}
			}
		}
		return obj;
	}
	
	Comparator<DeliciousEntity> myComparator = new Comparator<DeliciousEntity>() {
	    public int compare(DeliciousEntity obj1,DeliciousEntity obj2) {
	        return obj2.getcommentSum().compareTo(obj1.getcommentSum());
	    }	
	 };
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
}


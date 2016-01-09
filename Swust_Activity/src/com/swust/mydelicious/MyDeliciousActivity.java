package com.swust.mydelicious;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swust.activity.R;
import com.swust.delicious.DeliciousEntity;
import com.swust.http.SendMessage;
import com.swust.ilikedelicious.ILikeDeliciousActivity;
import com.swust.ilikedelicious.ILikeListViewAdapter;
import com.swust.model.GoodFood;
import com.swust.util.ConstantField;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyDeliciousActivity extends Activity {

	private ListView my_listview;
    private TextView title_text;
    private MyListViewAdapter Dadapter;
    public List<DeliciousEntity> delicious_list;
    private final int SUCCESS=0;
    private final int ERROR=1;
    public Integer userid;
    private Handler handler = new Handler()
    {
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				delicious_list = (List<DeliciousEntity>) msg.obj;
				Dadapter = new MyListViewAdapter(MyDeliciousActivity.this,delicious_list);//创建数据源
				my_listview.setAdapter(Dadapter);//自动为id为list的ListView设置适配器
				break;
			case ERROR:
				Toast.makeText(MyDeliciousActivity.this, "获取列表失败", 0).show();
				break;
			default:
				break;
			}
		}
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_delicious);
		initView();
		init();
		initEvents();
	}
	public void initView()
	{
		title_text=(TextView) findViewById(R.id.my_delicious_text);
	    my_listview=(ListView) findViewById(R.id.my_delicious_listview);
		//my_listview.setInterface((IReflashListener) this);
	}
	
	public void init()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 获取日历列表
				JSONObject res = obtainLoveFood();
				try {
					
					List<GoodFood> sharedfoodlist = obtainSharedFood_list(res);
					List<DeliciousEntity> de_list=showShareFood(sharedfoodlist);
					Message msg=new Message();
					if(de_list!=null)
					{
						msg.what=SUCCESS;
						msg.obj=de_list;
					}
					else 
					{
						msg.what=ERROR;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}
	public List<DeliciousEntity> showShareFood(List<GoodFood> sharedfoodlist)
	{
		List<DeliciousEntity> de_list=new ArrayList<DeliciousEntity>();
		for(int i=0;i<sharedfoodlist.size();i++)
		{
			DeliciousEntity entity=new DeliciousEntity();
			entity.setTitle(sharedfoodlist.get(i).getName());
			entity.setKeyword(sharedfoodlist.get(i).getFoodType());
			entity.setAverage(sharedfoodlist.get(i).getPersonPrice());
			entity.setPlace(sharedfoodlist.get(i).getPlace());
			entity.setDetail(sharedfoodlist.get(i).getDes());
			entity.setUrl(sharedfoodlist.get(i).getUrl());
			de_list.add(entity);
		}
		return de_list;
	}
	public void initEvents()
	{
		my_listview.setOnItemClickListener(new OnItemClickListener() {

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
        		intent.putExtra("foodid", delicious_list.get(position).getFoodid());
    			intent.setClass(MyDeliciousActivity.this, com.swust.delicious.OwnDelicious.class);
    			startActivity(intent);
    			
            }
        });
	}
	
	private JSONObject obtainLoveFood() {
		JSONObject req = new JSONObject();
		JSONObject res;
		userid=com.swust.load.LoadActivity.getUserid();
		try {
			req.put(ConstantField.USERID,userid);
			res = SendMessage.sendMessage(
					"obtainSharedFoodAction.action", req);

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
	private List<GoodFood> obtainSharedFood_list(JSONObject res)
	        throws Exception{
		List<GoodFood> sharedfoodlist=new ArrayList<GoodFood>();
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
					sharedfoodlist.add(FoodList);
				}
			else
				sharedfoodlist=null;
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sharedfoodlist;
		//返回校园活动的列表项
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
				} else if (field.getType().equals(java.util.Date.class)) {
					field.set(obj, Date.parse(json.getString(name)));
				} else {
					continue;
				}
			}
		}
		return obj;
	}
}

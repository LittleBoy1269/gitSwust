package com.swust.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMessage {

	//private static final String baseUrl="http://192.168.1.149:2000/Calender/";
	private static final String baseUrl="http://115.28.52.87:8080/";
	private static final int REQUEST_TIMEOUT = 10*1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
    
	public static JSONObject sendMessage(String url,JSONObject req){
		HttpClient httpClient = new DefaultHttpClient();
		JSONObject response = null; 
		try {
			
			 HttpPost httpPost = new HttpPost(baseUrl + url);	
			 if(req!=null){
			 StringEntity s = new StringEntity(URLEncoder.encode(req.toString(),"UTF-8"));
			 s.setContentEncoding("UTF-8");    
			 s.setContentType("application/json");    
			 httpPost.setEntity(s); 
			 }
			 HttpResponse res = httpClient.execute(httpPost);    
			 if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){    
				 response=getRes(res.getEntity().getContent());
			 }    
			 
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return response;
	}
	
	private static JSONObject getRes(InputStream in) throws JSONException, IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader (in));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        // 将资料解码
        String reqBody = sb.toString();
       
        JSONObject json= new JSONObject(reqBody);
        return json;
        
	}
}

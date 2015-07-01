package com.robo.store.http;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robo.store.BaseApplication;
import com.robo.store.util.LogUtil;


public class RoboHttpClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static RequestParams getHeaderParameter(Map data) throws JsonGenerationException, JsonMappingException, IOException{
		if(TextUtils.isEmpty(HttpParameter.channelId)){
			HttpParameter.initRequestHeader(BaseApplication.mInstance);
		}
		RequestParams params = new RequestParams();
		ObjectMapper objectMapper = new ObjectMapper();
		params.put("paramList", objectMapper.writeValueAsString(data));
		return params;
	}
	
	public static void get(String url, String type, Map param, AsyncHttpResponseHandler responseHandler) {
		try {
			RequestParams params = getHeaderParameter(param);
			client.addHeader("request-type", type);
			client.addHeader("softVer", HttpParameter.softVer);
			client.addHeader("channelId", HttpParameter.channelId);
			client.addHeader("platform", HttpParameter.platform);
			client.addHeader("systemVer", HttpParameter.systemVer);
			client.addHeader("machId", HttpParameter.machId);
			LogUtil.DefalutLog("RoboHttpClient---post:"+params.toString());
			client.get(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void post(String url, String type, Map param, AsyncHttpResponseHandler responseHandler) {
		try {
			RequestParams params = getHeaderParameter(param);
			client.addHeader("request-type", type);
			client.addHeader("softVer", HttpParameter.softVer);
			client.addHeader("channelId", HttpParameter.channelId);
			client.addHeader("platform", HttpParameter.platform);
			client.addHeader("systemVer", HttpParameter.systemVer);
			client.addHeader("machId", HttpParameter.machId);
			LogUtil.DefalutLog("RoboHttpClient---post:"+params.toString());
			client.post(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void get(String type, Map param, AsyncHttpResponseHandler responseHandler) {
		get(HttpParameter.baseUrl, type, param, responseHandler);
	}
	
	public static void post(String type, Map param, AsyncHttpResponseHandler responseHandler) {
		post(HttpParameter.baseUrl, type, param, responseHandler);
	}

}

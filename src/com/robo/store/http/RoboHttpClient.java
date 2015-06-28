package com.robo.store.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robo.store.BaseApplication;
import com.robo.store.util.LogUtil;


public class RoboHttpClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static RequestParams getHeaderParameter(RequestParams data){
		if(TextUtils.isEmpty(HttpParameter.channelId)){
			HttpParameter.initRequestHeader(BaseApplication.mInstance);
		}
		RequestParams params = new RequestParams();
		params.put("paramList", new Gson().toJson(data));
		return params;
	}
	
	public static void get(String url, String type, RequestParams param, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = getHeaderParameter(param);
		client.addHeader("request-type", type);
		client.addHeader("softVer", HttpParameter.softVer);
		client.addHeader("channelId", HttpParameter.channelId);
		client.addHeader("platform", HttpParameter.platform);
		client.addHeader("systemVer", HttpParameter.systemVer);
		client.addHeader("machId", HttpParameter.machId);
		LogUtil.DefalutLog("RoboHttpClient---post:"+params.toString());
		client.get(url, params, responseHandler);
	}
	
	public static void post(String url, String type, RequestParams param, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = getHeaderParameter(param);
		client.addHeader("request-type", type);
		client.addHeader("softVer", HttpParameter.softVer);
		client.addHeader("channelId", HttpParameter.channelId);
		client.addHeader("platform", HttpParameter.platform);
		client.addHeader("systemVer", HttpParameter.systemVer);
		client.addHeader("machId", HttpParameter.machId);
		LogUtil.DefalutLog("RoboHttpClient---post:"+params.toString());
		client.post(url, params, responseHandler);
	}
	
	public static void get(String type, RequestParams param, AsyncHttpResponseHandler responseHandler) {
		get(HttpParameter.baseUrl, type, param, responseHandler);
	}
	
	public static void post(String type, RequestParams param, AsyncHttpResponseHandler responseHandler) {
		post(HttpParameter.baseUrl, type, param, responseHandler);
	}

}

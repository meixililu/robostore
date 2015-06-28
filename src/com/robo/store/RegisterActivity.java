package com.robo.store;

import org.apache.http.Header;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ToastUtil;

public class RegisterActivity extends BaseActivity {

	private FrameLayout login_cover;
	private EditText username_input;
	private TextView error_txt;
	private Button register_btn;
	private String userName;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.register));
		setContentView(R.layout.activity_register);
		init();
	}
	
	private void init(){
		login_cover = (FrameLayout) findViewById(R.id.login_cover);
		username_input = (EditText) findViewById(R.id.username_input);
		error_txt = (TextView) findViewById(R.id.error_txt);
		register_btn = (Button) findViewById(R.id.register_btn);
		
		login_cover.setOnClickListener(this);
		register_btn.setOnClickListener(this);
	}
	
	private void RequestData(){
		if(validData()){
			showSucceeDialog();
			RequestParams params = new RequestParams();
			params.put("mobile", userName);
			RoboHttpClient.post("","userLogin", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					LogUtil.DefalutLog("register---RequestData---onFailure:"+arg2);
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					LogUtil.DefalutLog("register---RequestData---onSuccess:"+arg2);
					
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}
	
	private boolean validData(){
		boolean isvalid = true;
		userName = username_input.getText().toString().trim();
		if(TextUtils.isEmpty(userName)){
			ToastUtil.diaplayMesShort(this, "请输入手机号");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void showSucceeDialog(){
		progressDialog = new ProgressDialog(this, "正在提交");
		progressDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.login_cover:
			toActivity(LoginActivity.class,null);
			RegisterActivity.this.finish();
			break;
		case R.id.register_btn:
			
			break;
		}
	}
	
	
}

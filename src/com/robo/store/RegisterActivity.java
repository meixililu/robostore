package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.dao.CommonResponse;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.ResultParse;
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
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("mobile", userName);
			RoboHttpClient.post("userRegister", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(RegisterActivity.this, "连接失败，请重试！");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					CommonResponse mCommonResponse = ResultParse.parseResult(arg2,CommonResponse.class);
					if(ResultParse.handleResutl(RegisterActivity.this, mCommonResponse)){
						ToastUtil.diaplayMesLong(RegisterActivity.this, "注册成功");
					}
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
		}else if(userName.length() != 11){
			ToastUtil.diaplayMesShort(this, "请输入正确的手机号");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void showSucceeDialog(){
		progressDialog = ProgressDialog.show(this, "", "正在注册...", true, false);
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
			RequestData();
			break;
		}
	}
	
	
}

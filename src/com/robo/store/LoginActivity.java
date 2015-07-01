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

import com.loopj.android.http.TextHttpResponseHandler;
import com.robo.store.dao.ResultDao;
import com.robo.store.dao.UserLoginResponse;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.LogUtil;
import com.robo.store.util.Md5;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class LoginActivity extends BaseActivity {

	private FrameLayout register_cover,forget_pwd_cover;
	private EditText username_input,pwd_input;
	private TextView error_txt;
	private Button login_btn;
	private String userName,pwd;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.login));
		setContentView(R.layout.activity_login);
		init();
	}
	
	private void init(){
		register_cover = (FrameLayout) findViewById(R.id.register_cover);
		forget_pwd_cover = (FrameLayout) findViewById(R.id.forget_pwd_cover);
		username_input = (EditText) findViewById(R.id.username_input);
		pwd_input = (EditText) findViewById(R.id.pwd_input);
		error_txt = (TextView) findViewById(R.id.error_txt);
		login_btn = (Button) findViewById(R.id.login_btn);
		
		register_cover.setOnClickListener(this);
		forget_pwd_cover.setOnClickListener(this);
		login_btn.setOnClickListener(this);
	}
	
	private boolean validData(){
		boolean isvalid = true;
		userName = username_input.getText().toString().trim();
		pwd = pwd_input.getText().toString().trim();
		if(TextUtils.isEmpty(userName)){
			ToastUtil.diaplayMesShort(this, "请输入账号");
			isvalid = false;
		}
		if(TextUtils.isEmpty(pwd)){
			ToastUtil.diaplayMesShort(this, "请输入密码");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void RequestData(){
		if(validData()){
			showSucceeDialog();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("mobile", userName);
			params.put("password", Md5.d5(pwd));
			RoboHttpClient.get("userLogin", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(LoginActivity.this, "连接失败，请重试！");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					LogUtil.DefalutLog(result);
					UserLoginResponse mUserLoginResponse = (UserLoginResponse) ResultParse.parseResult(result,UserLoginResponse.class);
					if(ResultParse.handleResutl(LoginActivity.this, mUserLoginResponse)){
						ToastUtil.diaplayMesLong(LoginActivity.this, "登录成功");
					}
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}
	
	private void showSucceeDialog(){
		progressDialog = ProgressDialog.show(this, "", "正在登录...", true, false);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.register_cover:
			toActivity(RegisterActivity.class,null);
			break;
		case R.id.forget_pwd_cover:
			toActivity(ForgetPWDActivity.class,null);
			break;
		case R.id.login_btn:
			RequestData();
			break;
		}
	}
	
	
}

package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.dao.CommonResponse;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.Md5;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ValidUtil;

public class ForgetPWDActivity extends BaseActivity {

	private EditText username_input,code_input,pwd_input;
	private TextView error_txt;
	private Button login_btn,getcode_btn;
	private String userName,identity_code,pwd;
	
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("密码找回");
		setContentView(R.layout.activity_forget_pwd);
		init();
	}
	
	private void init(){
		username_input = (EditText) findViewById(R.id.username_input);
		code_input = (EditText) findViewById(R.id.code_input);
		pwd_input = (EditText) findViewById(R.id.pwd_input);
		error_txt = (TextView) findViewById(R.id.error_txt);
		login_btn = (Button) findViewById(R.id.login_btn);
		getcode_btn = (Button) findViewById(R.id.getcode_btn);
		
		getcode_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
	}
	
	private void RequestIdentityCodeData(){
		userName = username_input.getText().toString().trim();
		if(ValidUtil.validPhoneData(this, userName)){
			showDialog();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("mobile", userName);
			RoboHttpClient.post("applyCheckCode", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(ForgetPWDActivity.this, "连接失败，请重试！");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					CommonResponse mCommonResponse = ResultParse.parseResult(result,CommonResponse.class);
					if(ResultParse.handleResutl(ForgetPWDActivity.this, mCommonResponse)){
						getcode_btn.setClickable(false);
						timer.start();
						ToastUtil.diaplayMesLong(ForgetPWDActivity.this, "验证码已发送");
					}
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}
	
	private void RequestData(){
		userName = username_input.getText().toString().trim();
		identity_code = code_input.getText().toString().trim();
		pwd = pwd_input.getText().toString().trim();
		if(ValidUtil.validData(this, userName, "请输入手机号") && ValidUtil.validData(this, identity_code, "请输入验证码") &&
				ValidUtil.validData(this, pwd, "请输入新密码")){
			showDialog();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("mobile", userName);
			params.put("checkCode", identity_code);
			params.put("newPassword", Md5.d5(pwd));
			RoboHttpClient.post("resetPassword", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(ForgetPWDActivity.this, "连接失败，请重试！");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					CommonResponse mCommonResponse = ResultParse.parseResult(result,CommonResponse.class);
					if(ResultParse.handleResutl(ForgetPWDActivity.this, mCommonResponse)){
						if (timer != null) {
							timer.cancel(); 
						}
						getcode_btn.setClickable(true);
						getcode_btn.setText("获取验证码");
						ToastUtil.diaplayMesLong(ForgetPWDActivity.this, "修改成功");
					}
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}
	
	private void showDialog(){
		progressDialog = ProgressDialog.show(this, "", "正在提交...", true, false);
	}
	
	CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			getcode_btn.setText(millisUntilFinished / 1000 + "秒");
		}
		
		@Override
		public void onFinish() {
			getcode_btn.setClickable(true);
			getcode_btn.setText("获取验证码");
		}
	};
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.getcode_btn:
			RequestIdentityCodeData();
			break;
		case R.id.login_btn:
			RequestData();
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	
}

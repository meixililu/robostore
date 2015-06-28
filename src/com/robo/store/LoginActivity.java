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
import com.robo.store.util.Md5;
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
			ToastUtil.diaplayMesShort(this, "«Î ‰»Î’À∫≈");
			isvalid = false;
		}
		if(TextUtils.isEmpty(pwd)){
			ToastUtil.diaplayMesShort(this, "«Î ‰»Î√‹¬Î");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void RequestData(){
		if(validData()){
			showSucceeDialog();
			RequestParams params = new RequestParams();
			params.put("mobile", userName);
			params.put("password", Md5.d5(pwd));
			RoboHttpClient.get("userLogin", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					LogUtil.DefalutLog("login---RequestData---onFailure:"+arg2);
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					LogUtil.DefalutLog("login---RequestData---onSuccess:"+arg2);
					
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}
	
	private void showSucceeDialog(){
		progressDialog = new ProgressDialog(this, "’˝‘⁄µ«¬º", getResources().getColor(R.color.app_color));
		progressDialog.show();
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

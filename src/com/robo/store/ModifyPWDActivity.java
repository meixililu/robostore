package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.dao.CommonResponse;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.Md5;
import com.robo.store.util.ResultParse;
import com.robo.store.util.SPUtil;
import com.robo.store.util.ToastUtil;

public class ModifyPWDActivity extends BaseActivity {

	private EditText old_pwd_input,new_pwd_input,repeat_pwd_input;
	private TextView error_txt;
	private Button submit_btn;
	
	private ProgressDialog progressDialog;
	private SharedPreferences mSharedPreferences;
	private String userName;
	private String newPWD,newPWDRepeat,oldPWD;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.modify_pwd));
		setContentView(R.layout.activity_modify_pwd);
		init();
	}
	
	private void init(){
		mSharedPreferences = SPUtil.getSharedPreferences(this);
		userName = mSharedPreferences.getString(KeyUtil.UserNameKey, "");
		
		old_pwd_input = (EditText) findViewById(R.id.old_pwd_input);
		new_pwd_input = (EditText) findViewById(R.id.new_pwd_input);
		repeat_pwd_input = (EditText) findViewById(R.id.repeat_pwd_input);
		error_txt = (TextView) findViewById(R.id.error_txt);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		
		submit_btn.setOnClickListener(this);
	}
	
	private boolean validData(){
		boolean isvalid = true;
		oldPWD = old_pwd_input.getText().toString().trim();
		newPWD = new_pwd_input.getText().toString().trim();
		newPWDRepeat = repeat_pwd_input.getText().toString().trim();
		if(TextUtils.isEmpty(oldPWD)){
			ToastUtil.diaplayMesShort(this, "请输入旧密码");
			isvalid = false;
		}
		if(TextUtils.isEmpty(newPWD)){
			ToastUtil.diaplayMesShort(this, "请输入新密码");
			isvalid = false;
		}
		if(TextUtils.isEmpty(newPWDRepeat)){
			ToastUtil.diaplayMesShort(this, "请再次输入新密码");
			isvalid = false;
		}
		if(!newPWDRepeat.equals(newPWD)){
			ToastUtil.diaplayMesShort(this, "两次输入的新密码不一致");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void RequestData(){
		error_txt.setText("");
		if(validData()){
			showSucceeDialog();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("currentPwd", Md5.d5(oldPWD));
			params.put("newPwd", Md5.d5(newPWD));
			params.put("mobile", userName);
			RoboHttpClient.get("modifyPassword", params, new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(ModifyPWDActivity.this, "连接失败，请重试！");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					LogUtil.DefalutLog(result);
					CommonResponse mCommonResponse = (CommonResponse) ResultParse.parseResult(result,CommonResponse.class);
					if(ResultParse.handleResutl(ModifyPWDActivity.this, mCommonResponse)){
						ToastUtil.diaplayMesLong(ModifyPWDActivity.this, "修改成功");
						SPUtil.saveSharedPreferences(mSharedPreferences, KeyUtil.UserPWDKey, Md5.d5(newPWD));
					}else{
						error_txt.setText(mCommonResponse.getErrorMsg());
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
		progressDialog = ProgressDialog.show(this, "", "正在提交...", true, false);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.submit_btn:
			RequestData();
			break;
		}
	}
	
	
}

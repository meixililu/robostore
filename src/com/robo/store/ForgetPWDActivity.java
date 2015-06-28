package com.robo.store;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ForgetPWDActivity extends BaseActivity {

	private FrameLayout register_cover,forget_pwd_cover;
	private EditText username_input,pwd_input;
	private TextView error_txt;
	private Button login_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("µÇÂ½");
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
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.register_cover:
//			toActivity();
			break;
		case R.id.forget_pwd_cover:
			
			break;
		case R.id.login_btn:
			
			break;
		}
	}
	
	
}

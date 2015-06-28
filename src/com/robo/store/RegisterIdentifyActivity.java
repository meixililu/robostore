package com.robo.store;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RegisterIdentifyActivity extends BaseActivity {

	private FrameLayout login_cover;
	private EditText username_input;
	private TextView error_txt;
	private Button register_btn;
	
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
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.login_cover:
			toActivity(LoginActivity.class,null);
			RegisterIdentifyActivity.this.finish();
			break;
		case R.id.register_btn:
			
			break;
		}
	}
	
	
}

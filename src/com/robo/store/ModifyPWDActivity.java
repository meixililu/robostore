package com.robo.store;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ModifyPWDActivity extends BaseActivity {

	private EditText old_pwd_input,new_pwd_input,repeat_pwd_input;
	private TextView error_txt;
	private Button submit_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.modify_pwd));
		setContentView(R.layout.activity_modify_pwd);
		init();
	}
	
	private void init(){
		old_pwd_input = (EditText) findViewById(R.id.old_pwd_input);
		new_pwd_input = (EditText) findViewById(R.id.new_pwd_input);
		repeat_pwd_input = (EditText) findViewById(R.id.repeat_pwd_input);
		error_txt = (TextView) findViewById(R.id.error_txt);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		
		submit_btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.submit_btn:
			break;
		}
	}
	
	
}

package com.robo.store;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.robo.store.util.KeyUtil;
import com.robo.store.util.ToastUtil;

public class SearchActivity extends BaseActivity implements OnClickListener{

	public static final String SearchGoods = "SearchGoods";
	public static final String SearchShops = "SearchShops";
	
	private FrameLayout search_cover;
	private EditText search_et;
	private String searchContent;
	private String searchType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.register));
		setContentView(R.layout.activity_search);
		init();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			searchType = mBundle.getString(KeyUtil.SearchTypeKey);
		}
		search_cover = (FrameLayout) findViewById(R.id.search_cover);
		search_et = (EditText) findViewById(R.id.search_tv);
		search_cover.setOnClickListener(this);
		search_et.requestFocus();
		showIME();
	}
	
	private void showIME(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager)search_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
				inputManager.showSoftInput(search_et, 0);
			}
		}, 500);
	}
	
	private boolean validData(){
		boolean isvalid = true;
		searchContent = search_et.getText().toString().trim();
		if(TextUtils.isEmpty(searchContent)){
			ToastUtil.diaplayMesShort(this, "请输入搜索内容");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void toSearchResultActivity(){
		if(validData()){
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.SearchTypeKey, searchType);	
			mBundle.putString(KeyUtil.SearchContentKey, searchContent);	
			toActivity(SearchResultActivity.class, mBundle);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.search_cover:
			toSearchResultActivity();
			break;
		}
	}
	
	
}

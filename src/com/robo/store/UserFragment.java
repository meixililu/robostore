package com.robo.store;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.robo.store.util.LogUtil;

public class UserFragment extends BaseFragment implements OnClickListener{
	
	public static final String AccountKey = "com.robo.store";
	private FrameLayout logout_cover;
	private Button mButton;
	private RelativeLayout login_layout;
	private LinearLayout user_info_layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addAccount();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(UserFragment.class.getName()+"---onCreateView");
		setLayoutId(R.layout.fragment_user);
        return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void initView(){
		logout_cover = (FrameLayout) getView().findViewById(R.id.logout_cover);
		login_layout = (RelativeLayout) getView().findViewById(R.id.login_layout);
		user_info_layout = (LinearLayout) getView().findViewById(R.id.user_info_layout);
		mButton = (Button) getView().findViewById(R.id.login_btn);
		
		logout_cover.setOnClickListener(this);
		mButton.setOnClickListener(this);
	}
	
	private void isLogin(){
		
	}
	
	private void addAccount(){
		AccountManager accountManager = AccountManager.get(getActivity());
		Account account = new Account("Messi",AccountKey);
        accountManager.addAccountExplicitly(account,"123456",null);
	}
	
	private void getAccount(){
		AccountManager accountManager = AccountManager.get(getActivity());
		Account[] mAccount = accountManager.getAccountsByType(AccountKey);
		if(mAccount != null && mAccount.length > 0){
			String name = mAccount[0].name;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(UserFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.edit_cover:
			
			break;
		}
	}
	
}

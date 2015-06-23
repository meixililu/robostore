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
import android.widget.TextView;

import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;

public class UserFragment extends BaseFragment implements OnClickListener{
	
	public static final String AccountKey = "com.robo.store";
	private FrameLayout logout_cover;
	private Button mButton;
	private RelativeLayout login_layout;
	private LinearLayout user_info_layout;
	
	private FrameLayout obligation_cover,to_pick_up_cover,refund_cover;
	private FrameLayout modify_pwd_cover,retrieve_pwd_cover;
	private TextView check_my_order;
	private TextView check_soft_update;
	
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
		
		obligation_cover = (FrameLayout) getView().findViewById(R.id.obligation_cover);
		to_pick_up_cover = (FrameLayout) getView().findViewById(R.id.to_pick_up_cover);
		refund_cover = (FrameLayout) getView().findViewById(R.id.refund_cover);
		modify_pwd_cover = (FrameLayout) getView().findViewById(R.id.modify_pwd_cover);
		retrieve_pwd_cover = (FrameLayout) getView().findViewById(R.id.retrieve_pwd_cover);
		check_my_order = (TextView) getView().findViewById(R.id.check_my_order);
		check_soft_update = (TextView) getView().findViewById(R.id.check_soft_update);
		
		logout_cover.setOnClickListener(this);
		obligation_cover.setOnClickListener(this);
		to_pick_up_cover.setOnClickListener(this);
		refund_cover.setOnClickListener(this);
		modify_pwd_cover.setOnClickListener(this);
		retrieve_pwd_cover.setOnClickListener(this);
		mButton.setOnClickListener(this);
		check_my_order.setOnClickListener(this);
		check_soft_update.setOnClickListener(this);
		isLogin();
	}
	
	private void isLogin(){
		if(LoginUtil.isLogin){
			logout_cover.setVisibility(View.VISIBLE);
			login_layout.setVisibility(View.GONE);
			user_info_layout.setVisibility(View.VISIBLE);
		}else{
			logout_cover.setVisibility(View.GONE);
			login_layout.setVisibility(View.VISIBLE);
			user_info_layout.setVisibility(View.GONE);
		}
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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.logout_cover:
			loginout();
			break;
		case R.id.login_btn:
			toLoginActivity();
			break;
		case R.id.check_my_order:
			checkMyAllOrder();
			break;
		case R.id.check_soft_update:
			checkSoftUpdate();
			break;
		}
	}
	
	private void loginout(){
		
	}
	
	private void toLoginActivity(){
		toActivity(LoginActivity.class,null); 
	}
	
	private void checkMyAllOrder(){
		
	}
	
	private void checkSoftUpdate(){
		
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

	
}

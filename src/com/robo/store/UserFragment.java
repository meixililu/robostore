package com.robo.store;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.util.APKDownloadUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.Md5;
import com.robo.store.util.SPUtil;

public class UserFragment extends BaseFragment implements OnClickListener{
	
	public static final String AccountKey = "com.robo.store";
	public static final int requestLoginCode = 101;
	private FrameLayout logout_cover;
	private Button mButton;
	private TextView my_account_name,soft_version_text;
	private RelativeLayout login_layout,about_us_layout;
	private LinearLayout user_info_layout;
	
	private FrameLayout obligation_cover,to_pick_up_cover,refund_cover;
	private FrameLayout modify_pwd_cover,retrieve_pwd_cover,check_update_cover;
	private TextView check_my_order;
	public static UserFragment mUserFragment;
	private SharedPreferences mSharedPreferences;
	private String userName,userPWD;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserFragment = this;
		mSharedPreferences = SPUtil.getSharedPreferences(getActivity());
		userPWD = mSharedPreferences.getString(KeyUtil.UserPWDKey, "");
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
		my_account_name = (TextView) getView().findViewById(R.id.my_account_name);
		soft_version_text = (TextView) getView().findViewById(R.id.soft_version_text);
		
		obligation_cover = (FrameLayout) getView().findViewById(R.id.obligation_cover);
		to_pick_up_cover = (FrameLayout) getView().findViewById(R.id.to_pick_up_cover);
		refund_cover = (FrameLayout) getView().findViewById(R.id.refund_cover);
		modify_pwd_cover = (FrameLayout) getView().findViewById(R.id.modify_pwd_cover);
		retrieve_pwd_cover = (FrameLayout) getView().findViewById(R.id.retrieve_pwd_cover);
		check_update_cover = (FrameLayout) getView().findViewById(R.id.check_update_cover);
		about_us_layout = (RelativeLayout) getView().findViewById(R.id.about_us_layout);
		check_my_order = (TextView) getView().findViewById(R.id.check_my_order);
		
		soft_version_text.setText(getVersion());
		logout_cover.setOnClickListener(this);
		obligation_cover.setOnClickListener(this);
		to_pick_up_cover.setOnClickListener(this);
		refund_cover.setOnClickListener(this);
		modify_pwd_cover.setOnClickListener(this);
		retrieve_pwd_cover.setOnClickListener(this);
		mButton.setOnClickListener(this);
		check_my_order.setOnClickListener(this);
		check_update_cover.setOnClickListener(this);
		about_us_layout.setOnClickListener(this);
		isLogin();
		
	}
	
	private void isLogin(){
		if(LoginUtil.isLogin){
			logout_cover.setVisibility(View.VISIBLE);
			login_layout.setVisibility(View.GONE);
			user_info_layout.setVisibility(View.VISIBLE);
			userName = mSharedPreferences.getString(KeyUtil.UserNameKey, "");
			if(userName.length() >= 11){
				my_account_name.setText(userName.substring(0, 3) + "****" + userName.substring(7));
			}
		}else{
			logout_cover.setVisibility(View.GONE);
			login_layout.setVisibility(View.VISIBLE);
			user_info_layout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(logout_cover != null){
			isLogin();
		}
	}
	
	private void addAccount(){
		AccountManager accountManager = AccountManager.get(getActivity());
		Account account = new Account(userName,AccountKey);
        accountManager.addAccountExplicitly(account,userPWD,null);
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.logout_cover:
			loginout();
			break;
		case R.id.login_btn:
			toActivityForResult(LoginActivity.class,null, requestLoginCode); 
			break;
		case R.id.obligation_cover:
			toOrderListActivity(1);
			break;
		case R.id.to_pick_up_cover:
			toOrderListActivity(3);
			break;
		case R.id.refund_cover:
			toOrderListActivity(4);
			break;
		case R.id.check_my_order:
			toOrderListActivity(0);
			break;
		case R.id.check_update_cover:
			checkSoftUpdate();
			break;
		case R.id.modify_pwd_cover:
			toActivity(ModifyPWDActivity.class, null);
			break;
		case R.id.retrieve_pwd_cover:
			toActivity(ForgetPWDActivity.class, null);
			break;
		case R.id.about_us_layout:
			toActivity(AboutUsActivity.class, null);
			break;
		}
	}
	
	private void toOrderListActivity(int type){
		Bundle mBundle = new Bundle();
		mBundle.putInt(KeyUtil.OrderTypeKey, type);
		toActivity(CheckAllOrdersActivity.class, mBundle);
	}
	
	private void loginout(){
		Dialog dialog = new Dialog(getActivity(), "温馨提示", "您确定要注销退出吗？");
		dialog.addAcceptButton("确定");
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtil.saveSharedPreferences(mSharedPreferences, KeyUtil.UserPWDKey, "");
				LoginUtil.isLogin = false;
				isLogin();
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	public String getVersion() {
		try {
			PackageManager manager = getActivity().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "1.0";
		}
	}
	
	private void checkSoftUpdate(){
		APKDownloadUtil.CheckUpdate(getActivity());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestLoginCode == requestCode && Activity.RESULT_OK == resultCode){
			isLogin();
		}
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(UserFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mUserFragment != null){
			mUserFragment = null;
		}
	}

	
}

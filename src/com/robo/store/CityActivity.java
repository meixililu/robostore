package com.robo.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.liucanwen.citylist.adapter.CityAdapter;
import com.liucanwen.citylist.data.CityData;
import com.liucanwen.citylist.model.CityItem;
import com.liucanwen.citylist.widget.ContactItemInterface;
import com.liucanwen.citylist.widget.ContactListViewImpl;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.SPUtil;
import com.robo.store.util.ToastUtil;

public class CityActivity extends BaseActivity implements TextWatcher {
	private Context context_ = CityActivity.this;

	private ContactListViewImpl listview;

	private EditText searchBox;
	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "MainActivity2";

	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;
	private CityAdapter adapter;
	
	private SharedPreferences mSharedPreferences;
	private String city;
	private String locationCity;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.select_city));
		setContentView(R.layout.city_library_activity);
		mSharedPreferences = SPUtil.getSharedPreferences(this);
		city = mSharedPreferences.getString(KeyUtil.CityKey, "");
		
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener( myListener ); 
		
		
		filterList = new ArrayList<ContactItemInterface>();
		contactList = CityData.getSampleContactList();
		adapter = new CityAdapter(this, R.layout.city_item, contactList);
		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);
		listview = (ContactListViewImpl) this.findViewById(R.id.city_listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,long id) {
				List<ContactItemInterface> searchList = inSearchMode ? filterList : contactList;
				String select = searchList.get(position).getDisplayInfo();
				saveCity(select);
//				ToastUtil.diaplayMesLong(CityActivity.this, select);
				CityActivity.this.finish();
			}
		});
		
		InitLocation();
		mLocationClient.start();
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setOpenGps(false);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) return ;
			LogUtil.DefalutLog("city---code:"+location.getCityCode());
			locationCity = location.getCity();
			CityItem item = (CityItem) contactList.get(0);
			item.setNickName(locationCity);
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onBackPressed() {
		if(TextUtils.isEmpty(locationCity)){
			if(TextUtils.isEmpty(city) ){
				ToastUtil.diaplayMesLong(this, "请选择您所在的城市");
			}else{
				saveCity(city);
			}
		}else{
			saveCity(locationCity);
			super.onBackPressed();
		}
	}
	
	private void saveCity(String city){
		SPUtil.saveSharedPreferences(mSharedPreferences, KeyUtil.CityKey, city);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLocationClient != null){
			mLocationClient.stop();
		}
	}
	
	@Override
	protected void onStop() {
		if(mLocationClient != null){
			mLocationClient.stop();
		}
		super.onStop();
	}

	@Override
	public void afterTextChanged(Editable s) {
		searchString = searchBox.getText().toString().trim().toUpperCase();
		if (curSearchTask != null
				&& curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
			try {
				curSearchTask.cancel(true);
			} catch (Exception e) {
				Log.i(TAG, "Fail to cancel running search task");
			}
		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}

	private class SearchListTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			filterList.clear();

			String keyword = params[0];

			inSearchMode = (keyword.length() > 0);

			if (inSearchMode) {
				// get all the items matching this
				for (ContactItemInterface item : contactList) {
					CityItem contact = (CityItem) item;

					boolean isPinyin = contact.getFullName().toUpperCase()
							.indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

					if (isPinyin || isChinese) {
						filterList.add(item);
					}

				}

			}
			return null;
		}

		protected void onPostExecute(String result) {

			synchronized (searchLock) {

				if (inSearchMode) {

					CityAdapter adapter = new CityAdapter(context_,
							R.layout.city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else {
					CityAdapter adapter = new CityAdapter(context_,
							R.layout.city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}

}

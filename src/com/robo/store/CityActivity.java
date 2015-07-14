package com.robo.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

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
import com.robo.store.dao.GetAllCityResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.CityUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.SPUtil;
import com.robo.store.util.SaveData;
import com.robo.store.util.ToastUtil;

public class CityActivity extends BaseActivity implements TextWatcher {
	
	public static final String SaveCityDataName = "CityListData";
	
	private Context context_ = CityActivity.this;

	private ContactListViewImpl listview;

	private EditText searchBox;
	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "MainActivity2";

	private List<ContactItemInterface> contactList;
	private List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;
	private CityAdapter adapter;
	
	private SharedPreferences mSharedPreferences;
	private String city;
	private String cityId;
	private String locationCity;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean isAddLocationCity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.select_city));
		setContentView(R.layout.city_library_activity);
		mSharedPreferences = SPUtil.getSharedPreferences(this);
		city = mSharedPreferences.getString(KeyUtil.CityKey, "");
		cityId = mSharedPreferences.getString(KeyUtil.CityIdKey, "");
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener( myListener ); 
		filterList = new ArrayList<ContactItemInterface>();
		contactList = new ArrayList<ContactItemInterface>();
		
		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);
		listview = (ContactListViewImpl) this.findViewById(R.id.city_listview);
		listview.setFastScrollEnabled(true);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,long id) {
//				List<ContactItemInterface> searchList = inSearchMode ? filterList : contactList;
				CityItem mCityItem= (CityItem)contactList.get(position);
				cityId = mCityItem.getCityId();
				saveCity(cityId, mCityItem.getDisplayInfo());
				CityActivity.this.finish();
			}
		});
		getCacheData();
		InitLocation();
		mLocationClient.start();
	}
	
	private void getCacheData(){
		try {
			Object mObject = SaveData.getObject(this, SaveCityDataName);
			if(mObject != null){
				contactList = (ArrayList<ContactItemInterface>) mObject;
				setAdapter();
				setLocationCity();
			}else{
				RequestData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			contactList = new ArrayList<ContactItemInterface>();
			e.printStackTrace();
		}
	}
	
	private void setAdapter(){
		adapter = new CityAdapter(CityActivity.this, R.layout.city_item, contactList);
		listview.setAdapter(adapter);
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
			locationCity = location.getCity();
			ShopFragment.longitude = location.getLongitude();
			ShopFragment.latitude = location.getLatitude();
			setLocationCity();
		}
	}
	
	private void setLocationCity(){
		if(contactList.size() > 0 && !TextUtils.isEmpty(locationCity)) {
			if(!isAddLocationCity){
				isAddLocationCity = true;
				CityUtil.setLocationCity(locationCity, contactList);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void RequestData(){
		showProgressbar();
		RoboHttpClient.get(HttpParameter.shopsUrl,"getAllCities", null, new TextHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				showEmptyLayout_Error();
				ToastUtil.diaplayMesLong(CityActivity.this, "连接失败，请重试！");
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetAllCityResponse mResponse = (GetAllCityResponse) ResultParse.parseResult(result,GetAllCityResponse.class);
				if(ResultParse.handleResutl(CityActivity.this, mResponse)){
					contactList = CityUtil.getSampleContactList(mResponse.getList());
					setAdapter();
					setLocationCity();
				}else{
					showEmptyLayout_Empty();
				}
			}
			@Override
			public void onFinish() {
				hideProgressbar();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(TextUtils.isEmpty(locationCity)){
			if(TextUtils.isEmpty(cityId) || TextUtils.isEmpty(city)){
				ToastUtil.diaplayMesLong(this, "请选择您所在的城市");
			}else{
				saveCity(cityId,city);
			}
		}else{
			CityItem mCityItem= (CityItem)contactList.get(0);
			saveCity(mCityItem.getCityId(), mCityItem.getNickName());
			super.onBackPressed();
		}
	}
	
	private void saveCity(String cityId, String city){
		SPUtil.saveSharedPreferences(mSharedPreferences, KeyUtil.CityKey, city);
		SPUtil.saveSharedPreferences(mSharedPreferences, KeyUtil.CityIdKey, cityId);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLocationClient != null){
			mLocationClient.stop();
		}
		if(contactList != null){
			if(contactList.size() > 0){
				SaveData.saveObject(this, SaveCityDataName, contactList);
			}
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
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
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
					boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
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
					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else {
					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}

}

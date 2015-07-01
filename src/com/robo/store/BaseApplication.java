package com.robo.store;

import com.baidu.mapapi.SDKInitializer;
import com.robo.store.http.HttpParameter;

import android.app.Application;

public class BaseApplication extends Application {

//	private static DaoMaster daoMaster;  
//    private static DaoSession daoSession; 
    public static BaseApplication mInstance;
//    public static 
    
    @Override  
    public void onCreate() {  
        super.onCreate();  
        if(mInstance == null)  
            mInstance = this; 
        HttpParameter.initRequestHeader(this);
        SDKInitializer.initialize(getApplicationContext());
    }  
    
    
    
//	/** 
//     * 取得DaoMaster 
//     * @param context 
//     * @return 
//     */  
//    public static DaoMaster getDaoMaster(Context context) {  
//        if (daoMaster == null) {  
//            OpenHelper helper = new DaoMaster.DevOpenHelper(context,LHContract.DATABASE_NAME, null);  
//            daoMaster = new DaoMaster(helper.getWritableDatabase());  
//        }  
//        return daoMaster;  
//    }  
//      
//    /** 
//     * 取得DaoSession 
//     * @param context 
//     * @return 
//     */  
//    public static DaoSession getDaoSession(Context context) {  
//        if (daoSession == null) {  
//            if (daoMaster == null) {  
//                daoMaster = getDaoMaster(context);  
//            }  
//            daoSession = daoMaster.newSession();  
//        }  
//        return daoSession;  
//    }  

}

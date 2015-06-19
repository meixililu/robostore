package com.robo.store.service;

import com.robo.store.util.Authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountService extends Service {

	private Authenticator authenticator;
	
	@Override
    public void onCreate() {
        super.onCreate();
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

}

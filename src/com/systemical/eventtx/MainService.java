package com.systemical.eventtx;

import com.systemical.eventtx.Factory.K;
import com.systemical.system.IAgent;
import com.systemical.system.MsgSwitch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service {

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Intent intent=new Intent("com.systemical.eventtx.MainService");
		this.startService(intent);
	}//
	
	public void onDestroy() {
		
	}//
	
	public void onStart(Intent intent, int startid) {

		Factory.setObject(Factory.K.ACTIVITY, MainService.class, this);
        
        MsgSwitch ms=(MsgSwitch) Factory.get(K.MSG_SWITCH);
        ms.start();

        Thread mdns=(Thread) Factory.get(K.MDNS_THREAD);
        mdns.start();
		
        Thread debug=(Thread) Factory.get(K.DEBUG_THREAD);
        debug.start();
        
        ms.registerAgent((IAgent) mdns);
        ms.registerAgent((IAgent) debug);
	}//
	
}///

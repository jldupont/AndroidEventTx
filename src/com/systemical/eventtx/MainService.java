package com.systemical.eventtx;

import com.systemical.eventtx.Factory.K;
import com.systemical.system.IAgent;
import com.systemical.system.MsgSwitch;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MainService extends IntentService {

	final static String TAG="MainService";
	
	public MainService() {
		super(TAG);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "MainService.onStartCommand: start");
		
		Factory.setObject(Factory.K.SERVICE, MainService.class, this);
        
        MsgSwitch ms=(MsgSwitch) Factory.get(K.MSG_SWITCH);
        ms.start();

        Thread mdns=(Thread) Factory.get(K.MDNS_THREAD);
        mdns.start();
		
        Thread debug=(Thread) Factory.get(K.DEBUG_THREAD);
        debug.start();
        
        ms.registerAgent((IAgent) mdns);
        ms.registerAgent((IAgent) debug);
        
        Log.d(TAG, "MainService.onStartCommand: end");
        return START_STICKY;
	}//

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}///

package com.systemical.eventtx;

import android.os.Message;
import android.util.Log;

import com.systemical.system.BaseThread;

public class DebugThread extends BaseThread {

	protected DebugThread() {
		super();
	}
	

	protected void handleMsg(Message msg) {
		Log.d("debug", "Message.what: "+msg.what);
	}

	public String getAgentName() {
		return "Debug";
	}

}

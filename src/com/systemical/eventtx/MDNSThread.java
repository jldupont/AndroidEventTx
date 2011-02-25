package com.systemical.eventtx;

import android.os.Handler;
import android.os.Message;

import com.systemical.system.IAgent;
import com.systemical.system.IMsgSwitch;
import com.systemical.system.MsgSwitch;
import com.systemical.system.MsgTypesList;

public class MDNSThread extends Thread implements IAgent {

	MsgTypesList mtInterests=new MsgTypesList(
			MsgMap.T.TICK
	);
	IMsgSwitch ms=null;
	Handler h=new Handler();
	
	
	public MDNSThread(IMsgSwitch _ms) {
		super("net");
		ms=_ms;
	}

	public boolean dispatch(Message msg) {
		h.dispatchMessage(msg);
		return false;
	}

	public Handler getHandler() {

		return null;
	}

	public void registerMsgSwitch(MsgSwitch ms) {
	}
	
	
}///

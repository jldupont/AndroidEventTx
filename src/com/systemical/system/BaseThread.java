package com.systemical.system;

import android.os.Handler;
import android.os.Message;

public abstract class BaseThread extends Thread implements IAgent {

	IMsgSwitch ms=null;
	Handler h=new Handler();
	protected MsgTypesList mtInterests=null;
	
	public BaseThread() {
		super("net");
	}

	/**
	 * Dispatch
	 * 
	 * Checks if the Agent is interested in the message before
	 *  passing on to "handleMsg"
	 */
	public boolean dispatch(Message msg) {
		boolean interest=false;
		if (mtInterests.contains(msg.what)) {
			handleMsg(msg);
			interest=true;
		}
		return interest;
	}

	public Handler getHandler() {
		return h;
	}

	public void registerMsgSwitch(IMsgSwitch ms) {
		this.ms=ms;
	}
	
	/**
	 * Implemented by derived classes 
	 * Real dispatch point
	 * 
	 * @param msg
	 */
	protected abstract void handleMsg(Message msg);
	
	
}///

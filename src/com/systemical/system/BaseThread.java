/**
 * BaseThread class
 * 
 * @author jldupont
 */
package com.systemical.system;

import com.systemical.eventtx.Factory;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public abstract class BaseThread extends Thread implements IAgent {

	MsgSwitch ms=null;
	Handler h=new Handler();
	protected MsgTypesList mtInterests=null;
	Activity activity=null;
	
	public BaseThread() {
		super();
		
		ms=(MsgSwitch) Factory.get(Factory.K.MSG_SWITCH);
		activity=(Activity) Factory.get(Factory.K.ACTIVITY);
	}

	/**
	 * Convenience method
	 * 
	 * @param msg
	 */
	protected void send(int what, Object obj) {
		Message m=Message.obtain(ms.handler, what, obj);
		h.sendMessage(m);
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

	/**
	 * Implemented by derived classes 
	 * Real dispatch point
	 * 
	 * @param msg
	 */
	protected abstract void handleMsg(Message msg);
	
	
}///

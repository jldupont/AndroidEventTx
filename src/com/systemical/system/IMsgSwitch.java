/**
 * @author jldupont
 */
package com.systemical.system;

import android.os.Message;

public interface IMsgSwitch {

	/**
	 * Used by to register an Agent with the MsgSwitch
	 * 
	 * @param agent
	 */
	public void registerAgent(IAgent agent);
	
	/**
	 * Sends a message to interested recipients
	 * 
	 * @param msg
	 */
	public void send(Message msg);
	
	/**
	 * Registers a message map i.e. {integer -> string}
	 */
	public void registerMessageMap(IMsgMap mm);
	
	
}//

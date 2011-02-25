/**
 * @author jldupont
 */
package com.systemical.system;

import android.os.Handler;
import android.os.Message;

public interface IAgent {

	/**
	 * Used to register the MsgSwitch with the Agent
	 * 
	 * @param ms
	 */
	public void registerMsgSwitch(IMsgSwitch ms);
	
	/**
	 * Used by the MsgSwitch to retrieve the 'handler'
	 *  for an Agent
	 *  
	 * @return Handler
	 */
	public Handler getHandler();
	
	/**
	 * Used to dispatch a message to the Agent
	 * 
	 * The MsgSwitch uses this method to dispatch a message to the Agent
	 * 
	 * @param msg
	 * @return boolean: true if the Agent is interested in the message 'type'
	 */
	public boolean dispatch(Message msg);
	
	
	/**
	 * An Agent returns its "name"
	 * 
	 * @return
	 */
	public String getAgentName();
	
}//

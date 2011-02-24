/**
 * @author jldupont
 */
package com.systemical.system;

import android.os.Handler;
import android.os.Message;

public interface IAgent {

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
	 * @param msg
	 * @return boolean: true if the Agent is interested in the message 'type'
	 */
	public boolean dispatch(Message msg);
	
}//

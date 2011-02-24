/**
 * MsgSwitch - Inter-thread Message Switch
 * 
 * @author jldupont
 */
package com.systemical.system;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MsgSwitch extends Thread implements IMsgSwitch {

	ArrayList<IAgent> agents=new ArrayList<IAgent>();
	HashMap<Integer, IAgent[]> map=new HashMap<Integer, IAgent[]>();
	
	
	Handler handler=null;
	
	public void run() {
		
		Looper.prepare();
		
		handler=new Handler() {
			
			/**
			 * Android Message
			 * 	- int arg1
			 *  - int arg2
			 *  - Object object
			 *  - int what
			 */
			public void handleMessage(Message msg) {
				
			}
			
		};
		
		
	}//run

	/**
	 * IMsgSwitch interface
	 */
	public void registerAgent(IAgent agent) {
		agents.add(agent);
	}//

	/**
	 * IMsgSwitch interface
	 */
	public void send(Message msg) {

		
	}
	
}//class

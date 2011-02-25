/**
 * MsgSwitch - Inter-thread Message Switch
 * 
 * Usage:
 * 	- main thread creates MsgSwitch
 *  - main thread creates Agents
 *  - main thread registers Agents to MsgSwitch
 * 
 * @author jldupont
 */
package com.systemical.system;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MsgSwitch extends Thread implements IMsgSwitch {

	ArrayList<IAgent> agents=new ArrayList<IAgent>();
	HashMap<Integer, ArrayList<IAgent>> map=new HashMap<Integer, ArrayList<IAgent>>();
	
	IMsgMap im=null;
	
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
				try {
					doSend(msg);
				} catch(SendException se) {
					Log.e("MsgSwitch", se.target.getAgentName()+":msg("+im.translate(se.msg.what)+"), exception: "+se.e.toString());
				}
			}
			
		};
		
		Looper.loop();
		
	}//run

	/**
	 * 
	 */
	public void registerMessageMap(IMsgMap mm) {
		im=mm;
	}
	
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
		handler.dispatchMessage(msg);
	}
	
	
	/**
	 * @param msg
	 */
	protected void doSend(Message msg) throws SendException {
		ArrayList<IAgent> interested=map.get(msg.what);
		
		// first time around? Send to all
		if (interested==null) {
			for (IAgent agent : agents) {
				try {
					boolean interest=agent.dispatch(msg);

					// did the recipient show an interest in the message?
					if (interest) {
						ArrayList<IAgent> startList=new ArrayList<IAgent>();
						startList.add(agent);
						map.put(msg.what, startList);
					}
					
				} catch(Exception e) {
					SendException se=new SendException();
					se.target=agent;
					se.e=e;
					se.msg=msg;
					throw se;
				}
				
			}//for
			return;
		}//first time
		
		for(IAgent agent : interested) {
			try {
				agent.dispatch(msg);
			} catch(Exception e) {
				SendException se=new SendException();
				se.target=agent;
				se.e=e;
				se.msg=msg;
				throw se;
			}
		}
		
	}//
	
	class SendException extends Exception {
		private static final long serialVersionUID = -6289833878562064793L;
		public IAgent target=null;
		public Exception e=null;
		public Message msg=null;
	}

	
}//class

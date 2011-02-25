/**
 * 
 * @author jldupont
 */
package com.systemical.eventtx;

import android.os.Message;

import com.systemical.system.BaseThread;
import com.systemical.system.MsgTypesList;

public class MDNSThread extends BaseThread {


	public MDNSThread() {
		super();

		mtInterests=new MsgTypesList(
			MsgMap.T.TICK.ordinal()
		);
	}//

	public String getAgentName() {
		return "MDNS";
	}

	/**
	 * Only messages of interest to this Agent
	 *  end-up here.
	 */
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}//
	
	
}///

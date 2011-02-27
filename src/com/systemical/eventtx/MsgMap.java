/**
 * @author jldupont
 */
package com.systemical.eventtx;

import com.systemical.system.IMsgMap;

public class MsgMap implements IMsgMap {

	public enum T {
		TICK
		,PACKET
		,NETWORK_INIT_ERROR
		,NETWORK_SOCKET_OPEN_ERROR
		,DNS_PARSE_ERROR
	};
	
	public MsgMap() {
	}

	
	public String translate(int type) {
		T res=T.values()[type];
		return res.name();
	}
	
}//

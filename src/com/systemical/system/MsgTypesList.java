/**
 * @author jldupont
 */
package com.systemical.system;

import java.util.ArrayList;

public class MsgTypesList {

	public ArrayList<Integer> mtl=new ArrayList<Integer>(); 
	
	public MsgTypesList(int ... types) {
		for (int i=0;i<types.length;i++) {
			mtl.add(types[i]);
		}
	}//
	
}//

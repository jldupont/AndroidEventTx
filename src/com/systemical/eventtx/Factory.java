/**
 * Factory
 * 
 * @author jldupont
 */
package com.systemical.eventtx;

import java.util.HashMap;

import android.os.Message;

import com.systemical.system.Debug;

public class Factory {

	public enum K {
		ACTIVITY
		,MSG_SWITCH
		,MSG_MAP
		,MESSAGE
		,MDNS_THREAD
		,DEBUG_THREAD
	};
	
	public enum T {
		NORMAL
		,SINGLETON
		,
	};
	
	protected static boolean isInit=false;
	
	@SuppressWarnings("unchecked")
	static HashMap<K, Class>      kmap=new HashMap<K, Class>();
	static HashMap<K, T>          tmap=new HashMap<K, T>();
	
	// for storing singletons
	@SuppressWarnings("unchecked")
	static HashMap<Class, Object> omap=new HashMap<Class, Object>();
	
	/**
	 * Sets an object relative to a class
	 * Useful with, e.g, Activity
	 * 
	 * @param klass
	 * @param o
	 */
	@SuppressWarnings("unchecked")
	public static void setObject(K klass, Class classe, Object o) {
		kmap.put(klass, classe);
		tmap.put(klass, T.SINGLETON);
		omap.put(classe, o);
	}//
	
	@SuppressWarnings("unchecked")
	public static Object get(K klass) {
		if (!isInit) 
			Factory.init();
		Object res=null;
		try {
			T type=tmap.get(klass);
			Class classe=kmap.get(klass);
			if (type==T.SINGLETON) {
				res=handleSingleton(classe);
			} else {
				res=kmap.get(klass).newInstance();
			}
		} catch(Exception e) {
			Debug.wtf("Issue whilst factoring a '"+klass.toString()+"'");
			return null;
		}
		return res;
	}
	
	protected static void init() {
		isInit=true;

		kmap.put(K.MDNS_THREAD,	MDNSThread.class);
		tmap.put(K.MDNS_THREAD, T.SINGLETON);

		kmap.put(K.DEBUG_THREAD,	DebugThread.class);
		tmap.put(K.DEBUG_THREAD, 	T.SINGLETON);
		
		kmap.put(K.MSG_SWITCH,	MsgMap.class);
		tmap.put(K.MESSAGE, 	T.SINGLETON);
		
		kmap.put(K.MESSAGE, 	Message.class);
		tmap.put(K.MESSAGE, 	T.NORMAL);
		
		kmap.put(K.MSG_MAP, 	MsgMap.class);
		tmap.put(K.MSG_MAP, 	T.SINGLETON);
		
	}//
	
	@SuppressWarnings("unchecked")
	protected static Object handleSingleton(Class classe) throws IllegalAccessException, InstantiationException  {
		Object o=omap.get(classe);
		if (o==null) {
			o=classe.newInstance();
			omap.put(classe, o);
		}
		
		return o;
	}//
	
}///

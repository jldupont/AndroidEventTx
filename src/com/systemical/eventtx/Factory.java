/**
 * Factory
 * 
 * @author jldupont
 */
package com.systemical.eventtx;

import java.util.HashMap;

import android.os.Message;
import android.util.Log;

import com.systemical.system.Debug;
import com.systemical.system.MsgSwitch;

public class Factory {

	final static String TAG="Factory";
	
	public enum K {
		SERVICE
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
		Log.d(TAG, "setObject: start, klass:"+klass.toString());
		kmap.put(klass, classe);
		tmap.put(klass, T.SINGLETON);
		omap.put(classe, o);
		Log.d(TAG, "setObject: end");
	}//
	
	@SuppressWarnings("unchecked")
	public static Object get(K klass) {
		Log.d(TAG, "get: start, klass:"+klass.toString());
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
			Debug.e("Issue whilst factoring a '"+klass.toString()+"' with exception:"+e.toString());
			return null;
		}
		Log.d(TAG, "get: end");
		return res;
	}
	
	protected static void init() {
		isInit=true;

		Log.d(TAG, "init: start");
		
		kmap.put(K.MDNS_THREAD,	MDNSThread.class);
		tmap.put(K.MDNS_THREAD, T.SINGLETON);

		kmap.put(K.DEBUG_THREAD,	DebugThread.class);
		tmap.put(K.DEBUG_THREAD, 	T.SINGLETON);
		
		kmap.put(K.MSG_SWITCH,	MsgSwitch.class);
		tmap.put(K.MSG_SWITCH, 	T.SINGLETON);
		
		kmap.put(K.MESSAGE, 	Message.class);
		tmap.put(K.MESSAGE, 	T.NORMAL);
		
		kmap.put(K.MSG_MAP, 	MsgMap.class);
		tmap.put(K.MSG_MAP, 	T.SINGLETON);
		
		Log.d(TAG, "init: end");
	}//
	
	@SuppressWarnings("unchecked")
	protected static Object handleSingleton(Class classe) throws IllegalAccessException, InstantiationException  {
		Log.d(TAG, "handleSingleton: start, classe:"+classe.toString());
		Object o=omap.get(classe);
		if (o==null) {
			Log.d(TAG, ">> Creating new instance of:"+classe.toString());
			o=classe.newInstance();
			omap.put(classe, o);
		}
		Log.d(TAG, "handleSingleton: end");
		return o;
	}//
	
}///

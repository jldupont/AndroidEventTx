/**
 * Factory
 * 
 * @author jldupont
 */
package com.systemical.eventtx;

import java.util.HashMap;

import com.systemical.system.Debug;

public class Factory {

	public enum K {
		MSG_MAP
		,
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
		
		kmap.put(K.MSG_MAP, MsgMap.class);
		tmap.put(K.MSG_MAP, T.SINGLETON);
		
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

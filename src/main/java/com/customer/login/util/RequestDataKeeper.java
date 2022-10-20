package com.customer.login.util;

import java.util.HashMap;
import java.util.Map;


public class RequestDataKeeper {


  private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

	public static Object get(String key) {
		if (threadLocal.get() == null) {
			return null;
		}
		return threadLocal.get().get(key);
	}

	public static void set(String key, Object value) {
		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<>());
		}
		threadLocal.get().put(key, value);
	}

	public static void setAll(Map<String, Object> map) {
		threadLocal.set(map);
	}

	public static Map<String, Object> get() {
		return threadLocal.get();
	}

	public static void remove() {
		threadLocal.remove();
	}
	public static long getLoggedInUserPK() {
		return (long) get("USER_PK");
	}
	public static String getUserType() {
		return (String) get("USER_TYPE");
	}
}


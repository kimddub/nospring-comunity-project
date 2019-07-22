package sbs.demo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtil {
	public static Object getClassObj(String fullClassName) {
		Class cls = null;
		Object obj = null;

		try {
			cls = Class.forName(fullClassName);
			obj = cls.newInstance();
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException : " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("InstantiationException : " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException : " + e.getMessage());
		}

		return obj;
	}

	public static boolean callMethodWithNoReturn(Object obj, String methodName) {
		boolean called = false;
		try {
			for (Method method : obj.getClass().getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					method.invoke(obj);
					called = true;
					break;
				}
			}
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException : " + e.getMessage());
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException : " + e.getMessage());
		}

		return called;
	}

	public static Object callMethod(Object obj, String methodName) {
		Object rs = null;

		try {
			for (Method method : obj.getClass().getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					rs = method.invoke(obj);
					break;
				}
			}
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException : " + e.getMessage());
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException : " + e.getMessage());
		}

		return rs;
	}
}
package sbs.demo.util;

public class StringUtil {
	public static String cap(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
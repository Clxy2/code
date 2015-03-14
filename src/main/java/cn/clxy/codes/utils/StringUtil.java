package cn.clxy.codes.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Utils for operating string.
 * @author clxy
 */
public final class StringUtil {

	/**
	 * Hate english.
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return isBlank(string);
	}

	/**
	 * Return true when Null or length = 0.
	 * @param string
	 * @return
	 */
	public static boolean isBlank(String string) {

		boolean result = string == null || string.length() == 0;
		return result;
	}

	public static String join(Collection<Object> list, String sep) {

		Iterator<Object> it = list.iterator();
		if (!it.hasNext()) {
			return "";
		}

		String start = it.next().toString();
		if (!it.hasNext()) {
			return start;
		}

		StringBuilder sb = new StringBuilder(64).append(start);
		while (it.hasNext()) {
			sb.append(sep);
			sb.append(it.next());
		}
		return sb.toString();
	}

	private StringUtil() {
	}
}

package cn.clxy.codes.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {

	public static String find(Pattern pattern, String text) {

		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private RegexUtil() {
	}
}

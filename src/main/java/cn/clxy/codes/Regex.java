package cn.clxy.codes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 收集正则表达式。
 * @author clxy
 */
public class Regex {

	private static final String[] dirty = { "126", "yahoo" };

	/**
	 * @param line
	 * @return
	 */
	private static String clear(String line) {

		String result = line;
		for (String d : dirty) {
			result = result.replaceAll("\"" + d + "\\.", "@" + d + "\\.");
		}
		return result;
	}

	public static void main(String[] args) {
		println(new String[] { clear("\"126."), clear("\"12\""), clear("\"yahoo."),
				clear("\"yahoo\"") });
		// testCookie();
		// testDuplicate();
	}

	private static void testCookie() {

		String pattern = ",(?=\\s*\\w+\\=)";
		String[] s = "x=a;httponly, y=b".split(pattern);
		println(s);

		s = "x=a,b, y=b".split(pattern);
		println(s);

		s = "remember_me=no; domain=iteye.com; path=/; expires=Sun, 09-Mar-2014 01:14:41 GMT, _javaeye3_session_=BAh7CDoQX2NzcmZfdG9rZW4iMURpK05lbTYxaG5BRUtITGxxcmFOUkVZR0pncy9nVG4zYTcycjRaaUhlQnM9Ogx1c2VyX2lkaQPJxw86D3Nlc3Npb25faWQiJTEyMjljNDBkN2FlNTE5YzM5MGUzMzZjNDFlNDU5OWMy--78694cbd014b5615712927d157942f336490f41f; domain=.iteye.com; path=/; HttpOnly"
				.split(pattern);
		println(s);

		s = "wordpress=lovelywcm%7C1248344625%7C26c45bab991dcd0b1f3bce6ae6c78c92; expires=Thu, 23-Jul-2009 10:23:45 GMT; path=/wp-content/plugins; domain=.wordpress.com; httponly, wordpress=lovelywcm%7C1248344625%7C26c45bab991dcd0b1f3bce6ae6c78c92; expires=Thu, 23-Jul-2009 10:23:45 GMT; path=/wp-content/plugins; domain=.wordpress.com; httponly,wordpress=lovelywcm%7C1248344625%7C26c45bab991dcd0b1f3bce6ae6c78c92; expires=Thu, 23-Jul-2009 10:23:45 GMT; path=/wp-content/plugins; domain=.wordpress.com; httponly"
				.split(pattern);
		println(s);

		s = "x=a;httponly, HttpOnly".split(pattern);
		println(s);
	}

	/**
	 * 寻找重复的字符。<br>
	 * (\\w)任意字符，\\1第一组（上面的任意字符），+重复<br>
	 */
	private static void testDuplicate() {
		Pattern p = Pattern.compile("(\\w)\\1+");
		p = Pattern.compile("(\\w).*?\\1+");
		Matcher m = p.matcher("aaabbcddee11");
		println(m);

		m = p.matcher("sbcabdfgc");
		println(m);
	}

	public static void println(Matcher m) {

		// 不总是正确。因为测试整体是否匹配？
		// System.out.println("是否匹配：" + (m.matches() ? "是" : "否"));
		StringBuilder sb = new StringBuilder();
		while (m.find()) {
			for (int i = 0, l = m.groupCount(); i <= l; i++) {
				sb.append(m.group(i)).append(" - ");
			}
			sb.append("\r\n");
		}
		if (sb.length() == 0) {
			System.out.println("是否匹配：否");
			return;
		}

		System.out.println("是否匹配：是");
		System.out.println(sb.toString());
	}

	private static void println(Object[] ary) {
		for (Object o : ary) {
			System.out.println(o.toString());
		}
	}
}

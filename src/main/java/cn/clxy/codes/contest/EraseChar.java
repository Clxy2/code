package cn.clxy.codes.contest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * http://hero.pongo.cn/Question/Details?ID=85&ExamID=83
 * 
 * <pre>
 * 给定一个字符串，仅由a,b,c 3种小写字母组成。当出现连续两个不同的字母时，你可以用另外一个字母替换它，如
 * - 有ab或ba连续出现，你把它们替换为字母c；
 * - 有ac或ca连续出现时，你可以把它们替换为字母b；
 * - 有bc或cb 连续出现时，你可以把它们替换为字母a。
 * 你可以不断反复按照这个规则进行替换，你的目标是使得最终结果所得到的字符串尽可能短，求最终结果的最短长度。 
 * 
 * 输入：字符串。长度不超过200，仅由abc三种小写字母组成。 
 * 输出：按照上述规则不断消除替换，所得到的字符串最短的长度。 
 * 
 * 例如：
 * 	输入cab，输出2。因为我们可以把它变为bb或者变为cc。 
 * 	输入bcab，输出1。尽管我们可以把它变为aab -> ac ->	b，也可以把它变为bbb，但因为前者长度更短，所以输出1。
 * </pre>
 * @author clxy
 */
public class EraseChar {

	private static Character[] chars = { 'a', 'b', 'c' };

	public static void main(String args[]) {

		Set<String> strings = new HashSet<>();

		// start === 测试数据。
		for (int i = 0; i < 100000; i++) {
			fillRandom(strings);
		}
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("ab");
		strings.add("abc");
		strings.add("aa");
		strings.add("aaa");
		strings.add("aaaa");
		strings.add("cacccc");
		// end === 测试数据。

		for (String s : strings) {
			int min1 = gg(s);
			int min2 = mm(s);
			System.out.println("min1=" + min1 + ", min2=" + min2 + " {" + s + "}");
			if (min1 != min2) {
				throw new RuntimeException("Noooooooo!");
			}
		}
	}

	/**
	 * c替换成ab；统计ab的个数；当a和b的个数都是偶数时，结果是两个；否则是一个。
	 * @param s
	 * @return
	 */
	public static int gg(String s) {

		char[] chars = s.toCharArray();

		int length = chars.length;
		if (isAllSame(chars)) {
			return length;
		}

		int countA = 0;
		int countB = 0;

		for (char c : chars) {
			switch (c) {
			case 'c':
				countA++;
				countB++;
				break;
			case 'a':
				countA++;
				break;
			case 'b':
				countB++;
				break;
			}
		}

		if (countA % 2 == 0 && countB % 2 == 0) {
			return 2;
		}

		return 1;
	}

	public static int mm(String s) {

		char[] chars = s.toCharArray();

		int length = chars.length;
		if (isAllSame(chars)) {
			return length;
		}

		LinkedList<Character> list = new LinkedList<>();
		for (char c : chars) {
			list.add(c);
		}

		return getMimi(list);
	}

	/**
	 * <ol>
	 * <li>尝试消除字符；
	 * <li>如不能消除则继续；
	 * <li>如能消除，用再前一位和再后一位字符评价是否能保证最短(=即不同)，能则消，否则继续；
	 * <li>一旦有消除发生，重新遍历消除后的新字符串；
	 * <li>遍历结束时检查是否存在可以消除但却没通过评价的情况，如果有则消除，重新遍历。
	 * </ol>
	 * @param list
	 * @return
	 */
	public static Integer getMimi(List<Character> list) {

		int size = list.size();
		if (size == 1) {
			return 1;
		}

		if (size == 2) {
			return list.get(0).equals(list.get(1)) ? 2 : 1;
		}

		Integer position = null;
		for (int i = 0, l = list.size() - 1; i < l; i++) {

			Character first = list.get(i);
			Character second = getElement(list, i + 1);
			Character test = test(first, second);
			if (test == null) {
				continue;
			}

			Set<Character> comps = new HashSet<>();
			Character prev = getElement(list, i - 1);
			if (prev != null) {
				comps.add(prev);
			}
			Character next = getElement(list, i + 2);
			if (next != null) {
				comps.add(next);
			}
			comps.remove(test);
			if (comps.isEmpty()) {
				position = (position == null) ? i : position;
				continue;
			}

			replace(list, i, test);
			return getMimi(list);
		}

		if (position == null) {
			return list.size();
		}

		Character first = list.get(position);
		Character second = getElement(list, position + 1);
		Character test = test(first, second);
		replace(list, position, test);
		return getMimi(list);
	}

	private static boolean isAllSame(char[] chars) {

		Character temp = null;
		for (char c : chars) {
			if (temp == null) {
				temp = c;
				continue;
			}

			if (!temp.equals(c)) {
				return false;
			}
		}
		return true;
	}

	private static Character test(char c1, char c2) {
		return (c1 == c2) ? null : minus(c1, c2);
	}

	private static void replace(List<Character> list, int i, char c) {
		list.remove(i);
		list.remove(i);
		list.add(c);
	}

	private static Character getElement(List<Character> list, int i) {
		return (i >= list.size() || i < 0) ? null : list.get(i);
	}

	private static char minus(char c1, char c2) {
		for (char c : chars) {
			if (c != c1 && c != c2) {
				return c;
			}
		}
		throw new RuntimeException("wrong char！");
	}

	private static void fillRandom(Set<String> strings) {

		Random r = new Random();
		int size = r.nextInt(200);
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			sb.append(chars[r.nextInt(3)]);
		}
		String s = sb.toString();
		if (strings.contains(s)) {
			fillRandom(strings);
		}
		strings.add(s);
	}
}
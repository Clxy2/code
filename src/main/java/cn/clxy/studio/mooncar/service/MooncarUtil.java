package cn.clxy.studio.mooncar.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MooncarUtil {

	static List<String> clean(String name) {

		List<String> result = new ArrayList<>();

		String[] ary = name.split(regxSplit);
		for (String n : ary) {
			n = n
					.replaceAll(regxMidChar, "")
					.replaceAll(regxTail, "");
			n = n.trim();
			if (numbers.contains(n)) {
				continue;
			}
			result.add(n);
		}

		return result;
	}

	private static final Set<String> numbers = new HashSet<>(Arrays.asList("暂无", ""));

	private static final String regxSplit =
			" |,|　|，|》|《|】|【|、|;|/|／|；|：|:|。|（|）|\"|”|“|<|>|\\(|\\)|\\*|&|’|‘|" +
					"\\d{1}\\.|简称|或(者){0,1}(叫){0,1}";
	private static final String regxTail =
			"(((?i)no)*(—|-|·|\\.)*(\\d|一|二|三|I|壹|X|１)*(号|號)*(月球|探月|探测)*(车|器)*)*$";
	private static final String regxMidChar = "…";

	private MooncarUtil() {
	}
}

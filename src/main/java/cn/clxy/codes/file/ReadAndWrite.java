/**
 * Copyright (C) 2013 CLXY Studio.
 * This content is released under the (Link Goes Here) MIT License.
 * http://en.wikipedia.org/wiki/MIT_License
 */
package cn.clxy.codes.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author clxy
 */
public class ReadAndWrite {

	private static final String path = "E:\\vms\\share\\2000W";
	private static final Charset charset = StandardCharsets.UTF_8;

	public static void main(String[] args) throws IOException {

		doFile(Paths.get(path + "\\5000.csv"));
		// File[] files = new File(path).listFiles();
		//
		// for (File file : files) {
		// Path input = file.toPath();
		// doFile(input);
		// }
	}

	private static void doFile(Path input) throws IOException {

		Path output = Paths.get(input.toString() + ".new");

		String outLine = "";
		try (BufferedReader br = Files.newBufferedReader(input, charset);
				BufferedWriter bw = Files.newBufferedWriter(output, charset)) {

			int lineNo = 0;
			int errCount = 0;
			for (String line = null; (line = br.readLine()) != null;) {

				lineNo++;
				outLine += clear(line);
				int commaCount = countComma(outLine);

				if (commaCount == 32) {
					bw.write(outLine);
					bw.newLine();
					outLine = "";
					errCount = 0;
					continue;
				}

				log(lineNo + " " + line);
				errCount++;
				if (errCount >= 5) {
					break;
				}
			}
		}
		log("Done!");
	}

	/**
	 * @param line
	 * @return
	 */
	private static String clear(String line) {

		char[] chars = line.toCharArray();

		for (int i = 1, len = chars.length; i < len; i++) {

			if (chars[i] != '"') {
				continue;
			}

			int preIdx = i - 1;
			int nextIdx = i + 1;
			boolean isPreComma = preIdx >= 0 && chars[preIdx] == ',';
			boolean isNextComma = nextIdx < len && chars[nextIdx] == ',';

			if (isPreComma || isNextComma) {
				continue;
			}

			chars[i] = '@';
		}

		return new String(chars);
	}

	private static void log(String str) {
		System.out.println(str);
	}

	private static int countComma(String str) {

		boolean hasQuote = false;
		int result = 0;

		for (char c : str.toCharArray()) {
			if (c == '"') {
				hasQuote = !hasQuote;
			}

			if (c == ',') {
				result += (hasQuote ? 0 : 1);
			}
		}

		return result;
	}
}

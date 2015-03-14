package cn.clxy.codes.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils for file.<br>
 * 注意！！！JDK7开始的java.nio.file.File.Files提供了大量方法。
 * @see Files
 * @author clxy
 */
public final class FileUtil {

	public static File getCurrent() {

		String dir = System.getProperty("user.dir");
		return new File(dir);
	}

	/**
	 * 如果是JDK7，使用Files#copy.<br>
	 * 下面是Channel的使用例子。
	 * @param destFile
	 * @param inputFile
	 * @throws Exception
	 * @deprecated
	 */
	@SuppressWarnings("resource")
	public static void copy(File destFile, File inputFile) throws Exception {

		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		try (FileChannel dest = new FileOutputStream(destFile).getChannel();
				FileChannel input = new FileOutputStream(inputFile).getChannel()) {
			dest.transferFrom(input, 0, input.size());
		}
	}

	public static List<String> readAllLines(File file) {

		List<String> result = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

		return result;
	}

	public static String readAsString(String path) {
		return readAsString(path, StandardCharsets.UTF_8);
	}

	public static String readAsString(String path, Charset encoding) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Deprecated
	public static String readFileAsString(String file) {
		return readFileAsString(new File(file));
	}

	@Deprecated
	public static String readFileAsString(File file) {

		byte[] buffer = new byte[(int) file.length()];
		try (BufferedInputStream f = new BufferedInputStream(
				new FileInputStream(file))) {
			f.read(buffer);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

		return new String(buffer);
	}

	/**
	 * Bad Performance!!!
	 * @param root
	 * @param name
	 * @return
	 */
	public static List<File> listByName(File root, String name) {

		List<File> result = new ArrayList<File>();

		if (root.isFile()) {
			if (name.equalsIgnoreCase(root.getName())) {
				result.add(root);
			}
			return result;
		}

		File[] fs = root.listFiles();
		if (fs == null) {
			return result;
		}

		for (File f : fs) {
			result.addAll(listByName(f, name));
		}

		return result;
	}

	private FileUtil() {
	}
}

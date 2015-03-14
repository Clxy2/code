package cn.clxy.codes.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public final class ZipUtil {

	public static boolean isZip(String fileName) {

		if (StringUtil.isEmpty(fileName)) {
			return false;
		}
		fileName = fileName.toLowerCase();
		return fileName.endsWith(".zip");
	}

	public static void zip(OutputStream os, Map<String, Object> data) throws Exception {
		zip(os, data, defaultReaders);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static void zip(OutputStream os, Map<String, Object> data,
			Map<Class, ByteConverter> converters) throws Exception {

		try (ZipOutputStream out = new ZipOutputStream(os)) {
			for (Entry<String, Object> e : data.entrySet()) {

				out.putNextEntry(new ZipEntry(e.getKey()));
				Object obj = e.getValue();
				ByteConverter converter = converters.get(obj.getClass());
				if (converter == null) {
					throw new RuntimeException("Do not have converter for class" + obj.getClass());
				}
				out.write(converter.toByte(obj));
				out.closeEntry();
			}
			out.flush();
			out.close();
		}
	}

	/**
	 * Zip里有不同类型的文件。 TODO 应该根据文件扩展名自动？
	 * @param is
	 * @param converters key=文件名,value=处理器。
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public static Map<String, Object> unzip(InputStream is, Map<String, ByteConverter> converters)
			throws Exception {

		Map<String, Object> result = new HashMap<String, Object>(converters.size());
		try (ZipInputStream zis = new ZipInputStream(is)) {

			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {

				String key = entry.getName();
				ByteConverter converter = converters.get(key);
				if (converter == null) {
					converter = CONVERTER_BYTES;
				}
				result.put(key, converter.toObject(read(zis)));
			}
		}

		return result;
	}

	/**
	 * 类型全部相同。
	 * @param is
	 * @param converter 共通的转换器
	 * @return
	 * @throws Exception
	 */
	public static <T> Map<String, T> unzip(
			InputStream is, ByteConverter<T> converter) throws Exception {

		Map<String, T> result = new HashMap<String, T>();
		try (ZipInputStream zis = new ZipInputStream(is)) {
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				String key = entry.getName();
				result.put(key, converter.toObject(read(zis)));
			}
		}

		return result;
	}

	/**
	 * 直接读取解压后字节。
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static Map<String, byte[]> unzip(InputStream is) throws Exception {
		return unzip(is, CONVERTER_BYTES);
	}

	/**
	 * 转换器。字节<->对象。
	 * @author clxy
	 * @param <T>
	 */
	public static interface ByteConverter<T> {

		byte[] toByte(T t) throws Exception;

		T toObject(byte[] bytes) throws Exception;
	}

	/**
	 * 文字类型转换。
	 * @author clxy
	 */
	public static class StringConverter implements ByteConverter<String> {

		private String charset;

		public StringConverter() {
			this("UTF8");
		}

		public StringConverter(String charset) {
			this.charset = charset;
		}

		@Override
		public byte[] toByte(String string) throws Exception {
			return string.getBytes(charset);
		}

		@Override
		public String toObject(byte[] bytes) throws Exception {
			return new String(bytes, charset);
		}
	}

	public static final ByteConverter<String> CONVERTER_STRING = new StringConverter();
	public static final ByteConverter<byte[]> CONVERTER_BYTES = new ByteConverter<byte[]>() {
		@Override
		public byte[] toByte(byte[] bytes) throws Exception {
			return bytes;
		}

		@Override
		public byte[] toObject(byte[] bytes) throws Exception {
			return bytes;
		}
	};

	private static byte[] read(ZipInputStream zis) throws Exception {
		return IOUtils.toByteArray(zis);
	}

	@SuppressWarnings({ "rawtypes" })
	private static final Map<Class, ByteConverter> defaultReaders = new HashMap<Class, ByteConverter>() {
		{
			put(String.class, CONVERTER_STRING);
			put(byte[].class, CONVERTER_BYTES);
		}
		private static final long serialVersionUID = 1L;
	};

	private ZipUtil() {
	}
}

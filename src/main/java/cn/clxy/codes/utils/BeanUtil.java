package cn.clxy.codes.utils;

import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Utils for operating Object.
 * @author clxy
 */
public final class BeanUtil {

	public static boolean in(Object obj, Object[] values) {

		for (Object value : values) {
			if (BeanUtil.equals(obj, value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Deep copy.
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepCopy(T object) {
		return (T) SerializationUtils.deserialize(SerializationUtils.serialize(object));
	}

	/**
	 * Get object from file. Just ONE Object!
	 * @param fileName
	 * @return
	 */
	public static Object decodeXML(File file) {

		try (BufferedInputStream bis =
				new BufferedInputStream(new FileInputStream(file))) {
			return new XMLDecoder(bis).readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save object to file.
	 * @param object
	 * @param fileName
	 */
	public static void encodeXML(Object object, File file,
			Map<Class<?>, PersistenceDelegate> pds) {

		try (FileOutputStream fos = new FileOutputStream(file)) {
			XMLEncoder encoder = new XMLEncoder(fos);
			if (pds != null) {
				for (Entry<Class<?>, PersistenceDelegate> entry : pds.entrySet()) {
					encoder.setPersistenceDelegate(entry.getKey(), entry.getValue());
				}
			}
			encoder.writeObject(object);
			encoder.flush();
			encoder.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get object from file. Just ONE Object!
	 * @param fileName
	 * @return
	 */
	public static Object deserialize(String fileName) {

		try (BufferedInputStream bis =
				new BufferedInputStream(new FileInputStream(fileName))) {
			return SerializationUtils.deserialize(bis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save object to file.
	 * @param object
	 * @param fileName
	 */
	public static void serialize(Serializable object, String fileName) {

		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			SerializationUtils.serialize(object, new BufferedOutputStream(fos));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Equal object by {@link Object#equals(Object)}, or specified properties.
	 * @param orig
	 * @param dest
	 * @param props
	 * @return
	 */
	public static boolean equals(Object orig, Object dest, String... props) {

		if (orig == dest) {
			return true;
		}

		if (orig == null || dest == null) {
			return false;
		}

		if (props == null || props.length == 0) {
			return orig.equals(dest);
		}

		try {
			for (String prop : props) {
				Object op = PropertyUtils.getProperty(orig, prop);
				Object dp = PropertyUtils.getProperty(dest, prop);
				if (!equals(op, dp)) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Generate hash code.
	 * @see HashCodeBuilder
	 * @param objs
	 * @return
	 */
	public static int hashCode(Object... objs) {
		return new HashCodeBuilder().append(objs).toHashCode();
	}

	/**
	 * Generate toString.
	 * @see ToStringBuilder
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return ToStringBuilder.reflectionToString(obj);
	}

	/**
	 * Get specified property.
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Object getProperty(Object obj, String property) {
		try {
			return PropertyUtils.getProperty(obj, property);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private BeanUtil() {
	}
}

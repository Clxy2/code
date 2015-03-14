package cn.clxy.codes.utils;

/**
 * Utils for operating string.
 * @author clxy
 */
public final class ClassUtil {

    public static <T> T getInstance(Class<T> clazz) {

        if (clazz == null) {
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Get specified class. if no package specified,
	 * <ol>
	 * <li>java.lang for String, Boolean etc.
	 * <li>java.util for java.util.date.
	 * </ol>
	 * @param className
	 * @return
	 */
	public static Class<?> forName(String className) {

		if (className == null) {
			return null;
		}

		if (className.indexOf(".") < 0) {

			Class<?> langClass = forName("java.lang." + className);
			if (langClass != null) {
				return langClass;
			}

			Class<?> utilClass = forName("java.util." + className);
			if (utilClass != null) {
				return utilClass;
			}
		}

		try {
			return Class.forName(className);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	private ClassUtil() {
	}
}

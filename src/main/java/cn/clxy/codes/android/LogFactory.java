package cn.clxy.codes.android;

public class LogFactory {

	public static Log getLog(Class<?> clazz) {
		return new Log(clazz.getName());
	}

	public static Log getLog(String clazz) {
		return new Log(clazz);
	}
}

package cn.clxy.codes.android;

public class Log {

	protected String tag;

	Log(String className) {
		this.tag = className;
	}

	/*
	public boolean isInfoEnabled() {
		return isEnabled(android.util.Log.INFO);
	}

	public void info(String msg) {
		android.util.Log.i(tag, msg);
	}

	public void info(String msg, Throwable t) {
		android.util.Log.i(tag, msg, t);
	}

	public boolean isDebugEnabled() {
		return isEnabled(android.util.Log.DEBUG);
	}

	public void debug(String msg) {
		android.util.Log.d(tag, msg);
	}

	public void debug(String msg, Throwable t) {
		android.util.Log.d(tag, msg, t);
	}

	public boolean isErrorEnabled() {
		return isEnabled(android.util.Log.ERROR);
	}

	public void error(String msg) {
		android.util.Log.e(tag, msg);
	}

	public void error(String msg, Throwable t) {
		android.util.Log.e(tag, msg, t);
	}

	public boolean isFatalEnabled() {
		return isErrorEnabled();
	}

	public void fatal(String msg) {
		android.util.Log.e(tag, msg);
	}

	public void fatal(String msg, Throwable t) {
		android.util.Log.e(tag, msg, t);
	}

	public boolean isTraceEnabled() {
		return isEnabled(android.util.Log.VERBOSE);
	}

	public void trace(String msg) {
		android.util.Log.v(tag, msg);
	}

	public void trace(String msg, Throwable t) {
		android.util.Log.v(tag, msg, t);
	}

	public boolean isWarnEnabled() {
		return isEnabled(android.util.Log.WARN);
	}

	public void warn(String msg) {
		android.util.Log.w(tag, msg);
	}

	public void warn(String msg, Throwable t) {
		android.util.Log.w(tag, msg, t);
	}

	private boolean isEnabled(int level) {
		return android.util.Log.isLoggable(tag, level);
	}
	*/
}

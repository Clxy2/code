/**
 * Copyright (C) 2013 CLXY Studio.
 * This content is released under the (Link Goes Here) MIT License.
 * http://en.wikipedia.org/wiki/MIT_License
 */
package cn.clxy.codes.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author clxy
 */
public final class DateUtil {

	public static Date truncate(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int offset) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, offset);
		return calendar.getTime();
	}

	public static String[] getWeekdayNames() {
		DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
		// String[] dayNames = symbols.getShortWeekdays();
		String[] dayNames = symbols.getWeekdays();
		return Arrays.copyOfRange(dayNames, 1, 7);
	}

	public static List<Date> getAllDates(Date date) {

		List<Date> dates = new ArrayList<>(42);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);

		int min = c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK);
		min = min > 0 ? min - 7 : min;
		int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int mod = (max + Math.abs(min)) % 7;
		max += mod == 0 ? 0 : (7 - mod);

		c.add(Calendar.DAY_OF_MONTH, min);
		for (int i = min; i < max; i++) {
			dates.add(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, 1);
		}

		return dates;
	}

	public static void main(String[] args) {

		System.out.println(Arrays.toString(getWeekdayNames()));

		Date d = new Date();
		println(getAllDates(d));
		println(getAllDates(addMonth(d, 1)));
		println(getAllDates(addMonth(d, 2)));

		// do nothing !!!你耍我？！！
		// Calendar.getInstance().setFirstDayOfWeek(Calendar.MONDAY);
	}

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");

	private static void println(List<Date> dates) {
		for (Date date : dates) {
			System.out.print(format.format(date));
		}
		System.out.println();
	}

	private DateUtil() {
	}

}

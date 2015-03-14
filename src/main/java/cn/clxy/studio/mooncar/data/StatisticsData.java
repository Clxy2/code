package cn.clxy.studio.mooncar.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.clxy.studio.mooncar.service.Site;

public class StatisticsData implements Serializable {

	private Date updateAt;
	private Map<String, SiteData> map = new HashMap<>();

	/**
	 * 如果不存在则添加。<br>
	 * 这样好吗？写作get实则set。
	 * @param site
	 * @return
	 */
	public SiteData getSite(String site) {

		SiteData result = map.get(site);
		if (result != null) {
			return result;
		}

		if (Site.valueOf(site) == null) {
			throw new RuntimeException("Wrong site  :" + site);
		}

		result = new SiteData();
		map.put(site, result);
		return result;
	}

	public void analyzeOrigin(OriginNameData origin) {

		SiteData site = getSite(origin.getSite());

		Integer id = origin.getOriginId();
		site.setMax(Math.max(site.getMax(), id));
		site.setCount(site.getCount() + 1);

		Map<Date, Integer> days = site.getDays();
		Date d = getDay(origin.getCreateAt());
		Integer count = days.get(d);
		if (count == null) {
			count = 0;
		}
		days.put(d, ++count);
	}

	public void analyzeName(NameData name) {

		SiteData site = getSite(name.getSite());
		site.setCountUnduplicated(site.getCountUnduplicated() + 1);
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Map<String, SiteData> getMap() {
		return map;
	}

	public void setMap(Map<String, SiteData> map) {
		this.map = map;
	}

	public static class SiteData implements Serializable {

		private int max;
		private int count;
		private int countUnduplicated;
		private Map<Date, Integer> days = new TreeMap<>();

		public Map<Date, Integer> getDays() {
			return days;
		}

		public void setDays(Map<Date, Integer> days) {
			this.days = days;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getCountUnduplicated() {
			return countUnduplicated;
		}

		public void setCountUnduplicated(int countUnduplicated) {
			this.countUnduplicated = countUnduplicated;
		}

		private static final long serialVersionUID = 1L;
	}

	private Date getDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	private static final long serialVersionUID = 1L;
}

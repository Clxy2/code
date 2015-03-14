package cn.clxy.studio.mooncar.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.clxy.studio.mooncar.data.NameData;
import cn.clxy.studio.mooncar.data.OriginNameData;
import cn.clxy.studio.mooncar.data.StatisticsData;

/**
 * GAE上存储条件无法满足需求，所以全部数据放在内存。
 * @author clxy
 */
public class MooncarService {

	static StatisticsData statistics;
	static List<NameData> names;
	static List<NameData> topCount;
	static List<NameData> topLatest;

	public void init() {

		reset();
	}

	/**
	 * 全部数据清空。
	 * @return
	 */
	public Integer deleteAll() {
		reset();
		return new Random().nextInt();
	}

	public List<NameData> search(String name) {

		List<NameData> result = new ArrayList<>();

		for (NameData data : names) {
			if (data.getName().indexOf(name) >= 0) {
				result.add(data);
			}
		}

		return result;
	}

	public void setNames(List<NameData> names) {
		MooncarService.names = names;
		sort();
		processTop();
	}

	public List<NameData> getNames() {
		return names;
	}

	public void setStatistics(StatisticsData statistics) {
		MooncarService.statistics = statistics;
	}

	public StatisticsData getStatistics() {
		return statistics;
	}

	public void analyze(final List<OriginNameData> datas) {

		log.warn("Latest name have " + datas.size());

		Map<String, NameData> nameMap = new HashMap<>();
		for (NameData data : names) {
			nameMap.put(data.getName(), data);
		}

		for (OriginNameData odata : datas) {

			statistics.analyzeOrigin(odata);

			for (String name : MooncarUtil.clean(odata.getName())) {
				NameData nameData = nameMap.get(name);
				if (nameData == null) {
					nameData = new NameData(name);
					nameMap.put(name, nameData);
				}
				nameData.countUp();
				nameData.mergeOrigin(odata);
			}
		}

		for (Entry<String, NameData> e : nameMap.entrySet()) {
			NameData data = e.getValue();
			statistics.analyzeName(data);
		}

		names.clear();
		names.addAll(nameMap.values());
		sort();
		processTop();

		statistics.setUpdateAt(new Date());

		log.warn("Unduplicatied names have " + nameMap.size());
	}

	public void refresh() {

		List<OriginNameData> newNames = new ArrayList<>();
		try {
			for (Site site : Site.values()) {
				site.fetch(newNames);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		analyze(newNames);
	}

	private void reset() {
		statistics = new StatisticsData();
		names = new ArrayList<NameData>();
		topCount = new ArrayList<NameData>();
		topLatest = new ArrayList<NameData>();
	}

	private void processTop() {

		topCount = TestHelper.getSub(names, 0, topLimit);

		List<NameData> tmp = new ArrayList<>(names);
		Collections.sort(tmp, latestComparator);
		topLatest = TestHelper.getSub(tmp, 0, topLimit);
	}

	private void sort() {
		Collections.sort(names, countComparator);
	}

	private static final Comparator<NameData> countComparator = new Comparator<NameData>() {
		@Override
		public int compare(NameData o1, NameData o2) {
			return o2.getCount() - o1.getCount();
		}
	};
	private static final Comparator<NameData> latestComparator = new Comparator<NameData>() {
		@Override
		public int compare(NameData o1, NameData o2) {
			return o2.getEarliest().compareTo(o1.getEarliest());
		}
	};

	private static final int topLimit = 10;
	private static final Log log = LogFactory.getLog(MooncarService.class);
}

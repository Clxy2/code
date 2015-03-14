package cn.clxy.studio.mooncar.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.clxy.codes.utils.ZipUtil;
import cn.clxy.studio.mooncar.data.NameData;
import cn.clxy.studio.mooncar.data.OriginNameData;
import cn.clxy.studio.mooncar.data.StatisticsData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

public final class TestHelper {

	private static final String originZip = "origin.zip";

	private static final String analyzeZip = "analyze.zip";
	private static final String namesJson = "names.json";
	private static final String statisticsJson = "statistics.json";

	private static final CsvMapper csvMapper = new CsvMapper();
	private static final ObjectMapper objectMapper =
			new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

	private static final String dir = System.getProperty("user.dir") + File.separator + "temp";

	public static void main(String[] args) throws Exception {

		MooncarService service = new MooncarService();
		service.init();

		// fetcthAndSaveRemoteOrigin();

		// List<OriginNameData> datas = loadLocalOrigin();
		// service.analyze(datas);
		// saveAnalyze(service);

		loadLocalAnalyze(service);

		// service.refresh();

	}

	private static void loadLocalAnalyze(MooncarService service) throws Exception {

		log.warn("--- start load local analyze data ---");
		Map<String, byte[]> contents = ZipUtil.unzip(openFile(analyzeZip));

		List<NameData> names = objectMapper.readValue(
				contents.get(namesJson), getListType(objectMapper, NameData.class));
		StatisticsData statistics = objectMapper.readValue(
				contents.get(statisticsJson), StatisticsData.class);

		service.setNames(names);
		service.setStatistics(statistics);

		log.warn("--- end load local analyze data ---");
	}

	private static void saveAnalyze(MooncarService service) throws Exception {

		Collection<NameData> datas = service.getNames();
		StatisticsData statistics = service.getStatistics();
		String ss = objectMapper.writeValueAsString(statistics);
		String sn = objectMapper.writeValueAsString(datas);

		Map<String, Object> map = new HashMap<>();
		map.put(namesJson, sn);
		map.put(statisticsJson, ss);

		File file = new File(dir + File.separator + analyzeZip);
		try (OutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			ZipUtil.zip(bos, map);
		}
	}

	private static void fetcthAndSaveRemoteOrigin() throws Exception {

		log.warn("--- start get remote origin data ---");

		fetchSite(Site.xinhua);
		fetchSite(Site.qq);

		log.warn("--- Done ---");
	}

	private static void fetchSite(Site site) throws Exception {

		List<OriginNameData> newNames = new ArrayList<>();
		try {
			site.fetch(newNames);
			log.warn("--- done with：" + Site.xinhua.name() + ", " + newNames.size() + " ---");
		} finally {

			log.warn("--- start output origin data:" + newNames.size() + " ---");

			ObjectWriter writer = csvMapper.writer(csvMapper.schemaFor(OriginNameData.class));

			File file = new File(dir + File.separator + site.name() + ".csv");
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));
			writer.writeValue(bos, newNames);
			bos.close();
		}
	}

	private static List<OriginNameData> loadLocalOrigin() throws Exception {

		log.warn("--- start load local origin data ---");

		List<OriginNameData> datas = new ArrayList<>();
		ObjectReader reader = csvMapper
				.reader(OriginNameData.class)
				.with(csvMapper.schemaFor(OriginNameData.class));

		Map<String, byte[]> contents = ZipUtil.unzip(openFile(originZip));

		for (Entry<String, byte[]> e : contents.entrySet()) {
			MappingIterator<OriginNameData> iterator = reader.readValues(e.getValue());
			while (iterator.hasNextValue()) {
				datas.add(iterator.next());
			}
		}

		log.warn("--- local origin data:" + datas.size() + " ---");

		return datas;
	}

	public static <T> List<T> getSub(List<T> list, int start, int end) {

		List<T> result = new ArrayList<T>();
		int size = list.size();

		if (list.isEmpty() || size < start) {
			return result;
		}

		end = Math.min(end, size);
		result.addAll(list.subList(start, end));
		return result;
	}

	public static JavaType getListType(ObjectMapper mapper, Class<?> clazz) {
		return mapper.getTypeFactory().constructParametricType(List.class, clazz);
	}

	private static InputStream openFile(String name) throws Exception {
		File file = new File(dir + File.separator + name);
		return new FileInputStream(file);
	}

	private TestHelper() {
	}

	private static final Log log = LogFactory.getLog(TestHelper.class);
}

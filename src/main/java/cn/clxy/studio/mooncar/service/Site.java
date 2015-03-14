package cn.clxy.studio.mooncar.service;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.clxy.studio.mooncar.data.OriginNameData;

public enum Site {

	qq {
		@Override
		public void fetch(List<OriginNameData> list) throws Exception {

			Integer latestMax = getLatestMax();
			Integer max = getMax();

			for (int i = latestMax; i > max; i--) {
				read(i, list);
			}
		}

		protected void read(int id, List<OriginNameData> list) throws Exception {

			String url = "http://act3.news.qq.com/10240/work/show-id-%d.html";
			url = String.format(url, id);
			log.warn("fetch:" + url);

			Document document = get(url);
			Elements elements = document.select(".flr677 .article");
			if (elements.isEmpty()) {
				return;
			}

			String name = clean(elements.select("h1").text());
			Elements info = elements.select(".authorInfo .lv1");
			String author = clean(info.get(0).text());
			String time = info.get(1).text();

			OriginNameData data = new OriginNameData(id, name, name());
			data.setCreateAt(dateFormat.parse(time));
			data.setCreateBy(author);

			list.add(data);
		}

		@Override
		protected Integer getLatestMax() throws Exception {

			String url = "http://act3.news.qq.com/10240/work/list-page-%d.html";
			Document document = get(String.format(url, 1));
			Elements element = document.select("#tplb2 .item:first-child");

			String href = element.select(".red1 a").attr("href");
			String id = href.substring(href.lastIndexOf('-') + 1, href.lastIndexOf('.'));

			return Integer.parseInt(id);
		}

		private String clean(String text) {

			int pos = text.indexOf("/h1>");
			if (pos > 0) {
				return text.substring(0, pos);
			}
			pos = text.indexOf("/span>");
			if (pos > 0) {
				return text.substring(0, pos);
			}
			return text;
		}

		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	},

	xinhua {

		@Override
		public void fetch(List<OriginNameData> list) throws Exception {

			int latestMaxPage = getLatestMax();
			int max = Math.max(getMax(), 1);
			int maxPage = max / 20;

			for (int i = latestMaxPage; i >= maxPage; i--) {
				read(i, list, max);
			}
		}

		protected void read(int pid, List<OriginNameData> list, Integer max) throws Exception {

			String url = "http://forum.home.news.cn/detail/126673699/%d.html";
			url = String.format(url, pid);
			log.warn(url);

			Document document = get(url);
			for (int j = 2; j <= 21; j++) {

				Elements infoElement = document.select("#mess_" + j);
				if (infoElement.isEmpty()) {
					return;
				}

				String sid = clearBlank(infoElement.select("font").text());
				Integer id = Integer.parseInt(sid);
				if (max.equals(id)) {
					return;
				}

				String author = infoElement.select("a.zuozhe01").text();
				Date time = getCreateAt(infoElement.select(".ip").text());

				String text = document.select("#message_" + j).text();
				for (String ns : split(text)) {
					OriginNameData data = new OriginNameData(id, ns, name());
					data.setCreateAt(time);
					data.setCreateBy(author);
					list.add(data);
				}
			}
		}

		@Override
		protected Integer getLatestMax() throws Exception {

			String url = "http://forum.home.news.cn/detail/126673699/1.html";
			Document document = get(url);
			Elements element = document.select("a:contains(尾页)");

			String href = element.get(0).attr("href");
			String id = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

			return Integer.parseInt(id);
		}

		private Date getCreateAt(String text) throws Exception {
			text = text.replaceAll("关注|于|发表", "").trim();
			return dateFormat.parse(clearBlank(text));
		}

		/**
		 * replace char 160 to empty.
		 * @param text
		 * @return
		 */
		private String clearBlank(String text) {
			return text.replaceAll(" ", "").trim();
		}

		private List<String> split(String text) {

			List<String> result = new ArrayList<>();
			Matcher matcher = p.matcher(text);
			while (matcher.find()) {
				result.add(clearBlank(matcher.group()).replaceAll("号号$", "号"));
			}
			return result;
		}

		private Pattern p = Pattern.compile(
				"(?<=作品名称：).*?(?=作品描述：|作品名称：|创意说明和背景阐述：|$)",
				Pattern.MULTILINE);
		private SimpleDateFormat dateFormat =
				new SimpleDateFormat("yyyy-MM-dd HH:mm");
	};

	public abstract void fetch(List<OriginNameData> list) throws Exception;

	protected abstract Integer getLatestMax() throws Exception;

	protected int getMax() {
		return MooncarService.statistics.getSite(this.name()).getMax();
	}

	protected Document get(String url) throws Exception {

		for (int i = 0;; i++) {
			try {
				return Jsoup.connect(url).timeout(timeout).get();
			} catch (SocketTimeoutException e) {

				if (i > retry) {
					throw e;
				}
				log.warn("Timeout (" + i + "): " + url);

				Thread.sleep(timeout / 2);
				continue;
			}
		}
	}

	protected static final int retry = 5;
	protected static final int timeout = 10000;
	private static final Log log = LogFactory.getLog(Site.class);
}

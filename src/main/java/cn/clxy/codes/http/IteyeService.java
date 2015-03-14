package cn.clxy.codes.http;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class IteyeService {

	@Resource
	protected HttpClient httpClient;

	public void refresh() {

		try {
			Document document = Jsoup.connect("http://clxy.iteye.com/blog/answered_problems").get();
			log.debug(document.select("th:contains(问答积分)+td").text());
			log.debug(document.select("span.gold.gray").text());
			log.debug(document.select("span.silver.gray").text());
			log.debug(document.select("span.bronze.gray").text());
		} catch (Exception e) {
			log.debug(e);
			return;
		}

		HttpRequestBase request = new HttpGet("http://clxy.iteye.com/blog/answered_problems");
		int statusCode = HttpStatus.SC_OK;
		String string = null;

		try {
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			string = EntityUtils.toString(entity);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			request.abort();
			throw new RuntimeException(e);
		} finally {
			request.releaseConnection();
		}

		log.debug(statusCode);
		log.debug(string);

		if (statusCode != HttpStatus.SC_OK) {
			return;
		}
	}

	private static final Log log = LogFactory.getLog(IteyeService.class);
}

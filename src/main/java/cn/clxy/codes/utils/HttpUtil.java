package cn.clxy.codes.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

public final class HttpUtil {

	public static String simpleGet(HttpClient client, String url) {
		return simpleGet(client, url, "UTF-8");
	}

	public static String simpleGet(HttpClient client, String url,
			final String encoding) {

		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> handler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws HttpResponseException, IOException {

				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(),
							statusLine.getReasonPhrase());
				}

				HttpEntity entity = response.getEntity();
				return entity == null ? null : EntityUtils.toString(entity,
						encoding);
			}
		};

		return execute(client, httpget, handler);
	}

	public static HttpEntity createFormEntity(List<NameValuePair> nvps,
			String code) {

		try {
			return new UrlEncodedFormEntity(nvps, code);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <E> E execute(HttpClient client, HttpUriRequest request,
			ResponseHandler<E> handler) {

		try {
			return client.execute(request, handler);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Cookie createCookie(String name, String value, String domain) {

		BasicClientCookie cookie = new BasicClientCookie(name, value);

		cookie.setVersion(1);
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookie.setSecure(true);
		// Set attributes EXACTLY as sent by the server
		cookie.setAttribute(ClientCookie.VERSION_ATTR, "1");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, domain);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		cookie.setExpiryDate(c.getTime());

		return cookie;
	}

	public static String getRefresh(String content) {
		return RegexUtil.find(pattern_refresh, content);
	}

	private HttpUtil() {
	}

	private static final Pattern pattern_refresh = Pattern.compile(
			"<meta http-equiv=\"refresh\" content=\".*url=([^\"]+)\">",
			Pattern.CASE_INSENSITIVE);
}

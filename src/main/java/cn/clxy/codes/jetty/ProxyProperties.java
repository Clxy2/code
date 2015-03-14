package cn.clxy.codes.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

public class ProxyProperties {

	private String user;
	private int httpPort = 9999;
	private int httpsPort = 9998;

	private Set<String> gfw = new HashSet<>();
	private List<String> gaes = new ArrayList<>();
	private Properties servlet = new Properties();

	public URI getGaeURI() {
		int i = new Random().nextInt(gaes.size());
		return URI.create(gaes.get(i));
	}

	public boolean needProxy(String url) {

		String s = url.toLowerCase();
		int index = 0;
		do {
			if (gfw.contains(s)) {
				return true;
			}
			index = s.indexOf('.');
			s = s.substring(index + 1);
		} while (index > 0);

		return false;
	}

	public static void loadApp(InputStream is) {

		Properties p = loadProperties(is);

		instance.httpPort = Integer.parseInt(p.getProperty("httpPort"));
		instance.httpsPort = Integer.parseInt(p.getProperty("httpsPort"));
		instance.user = p.getProperty("user");
		instance.gaes.addAll(Arrays.asList(p.getProperty("gae").split("\\|")));
	}

	public static void loadGfw(InputStream is) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null) {
				instance.gfw.add(line);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void loadServlet(InputStream is) {
		instance.servlet = loadProperties(is);
	}

	public static ProxyProperties get() {
		return instance;
	}

	public String getUser() {
		return user;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public int getHttpsPort() {
		return httpsPort;
	}

	public Properties getServlet() {
		return servlet;
	}

	private static Properties loadProperties(InputStream is) {

		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return p;
	}

	private ProxyProperties() {
	}

	private static ProxyProperties instance = new ProxyProperties();
}

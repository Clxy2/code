package cn.clxy.codes.jetty;

import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Password;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * 演示如何使用Jetty。<br>
 * 1. 添加Servlet。 2. 支持SSL连接。<br>
 * 有趣的地方：<br>
 * 1. 作为浏览器的Proxy，HTTP可以由Servlet接受到。但HTTPS接受不到，怀疑是自制证书的问题。<br>
 * 2. Servlet接受不到HTTPS，可以使用Connect Handler接受HTTPS的connect连接。<br>
 * @author clxy
 */
public class Main {

	public static void main(String[] args) throws Exception {

		// load properties.
		ProxyProperties pp = loadProxyProperties();

		HandlerCollection handlers = new HandlerCollection();

		// Set servlet.
		ServletContextHandler servlet = new ServletContextHandler();
		ServletHolder holder = new ServletHolder(MyServlet.class);
		// Set servlet parameters.
		for (Entry<Object, Object> e : pp.getServlet().entrySet()) {
			holder.setInitParameter((String) e.getKey(), (String) e.getValue());
		}
		servlet.addServlet(holder, "/*");
		handlers.addHandler(servlet);

		// Set connector.
		ConnectHandler proxy = new ConnectHandler();
		handlers.addHandler(proxy);

		Server server = createServer(pp);
		server.setHandler(handlers);
		server.start();
		server.join();
	}

	private static ProxyProperties loadProxyProperties() {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		ProxyProperties.loadApp(cl.getResourceAsStream("app.properties"));
		ProxyProperties.loadGfw(cl.getResourceAsStream("gfw.txt"));
		ProxyProperties.loadServlet(cl.getResourceAsStream("servlet.properties"));

		return ProxyProperties.get();
	}

	/**
	 * Create SSL support server.
	 * @param pp
	 * @return
	 * @throws Exception
	 */
	private static Server createServer(ProxyProperties pp) throws Exception {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// keystore使用JDK的keytool生成。
		// 参考http://wiki.eclipse.org/Jetty/Howto/Configure_SSL
		Resource keystore = new FileResource(cl.getResource("keystore"));
		String password = Password.obfuscate("clxystudio");

		Server server = new Server(pp.getHttpPort());

		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setSecureScheme("https");
		httpConfig.setSecurePort(pp.getHttpsPort());

		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStoreResource(keystore);
		sslContextFactory.setKeyStorePassword(password);
		sslContextFactory.setKeyManagerPassword(password);
		sslContextFactory.setTrustStoreResource(keystore);
		sslContextFactory.setTrustStorePassword(password);
		sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA",
				"SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
				"SSL_RSA_EXPORT_WITH_RC4_40_MD5", "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
		HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
		httpsConfig.addCustomizer(new SecureRequestCustomizer());
		ServerConnector sslConnector = new ServerConnector(
				server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"),
				new HttpConnectionFactory(httpsConfig));
		sslConnector.setPort(pp.getHttpsPort());
		server.addConnector(sslConnector);

		return server;
	}
}

class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
}

class ConnectHandler extends HandlerWrapper {
}
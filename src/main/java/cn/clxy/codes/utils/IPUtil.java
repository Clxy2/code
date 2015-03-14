/**
 * Copyright (C) 2013 CLXY Studio.
 * This content is released under the (Link Goes Here) MIT License.
 * http://en.wikipedia.org/wiki/MIT_License
 */
package cn.clxy.codes.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author clxy
 */
public class IPUtil {

	private IPUtil() {
	}

	public static Set<String> getIPList(String address) {

		Set<String> ips = new HashSet<>();

		try {
			InetAddress[] machines = InetAddress.getAllByName(address);
			for (InetAddress machine : machines) {
				ips.add(machine.getHostAddress());
			}
		} catch (UnknownHostException e) {
			// Do nothing, just return empty result.
		}
		return ips;
	}

	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static void main(String[] args) {

		// ～2013.8 使用 203.208.46.145 clxystudios.appspot.com
		System.out.println(getIPList("google.cn"));
		System.out.println(getIPList("appspot.com"));
		System.out.println(getIPList("google.com.hk"));

		System.out.println(getLocalIP());
		System.out.println(getLocalHostName());
	}
}

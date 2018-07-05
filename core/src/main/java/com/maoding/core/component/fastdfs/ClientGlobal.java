/**
 * Copyright (C) 2008 Happy Fish / YuQing
 *
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package com.maoding.core.component.fastdfs;

import java.net.*;
import java.io.*;
import java.util.Map;

import com.maoding.core.component.fastdfs.common.*;

/**
 * Global variables
 * 
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class ClientGlobal {
	public static int g_connect_timeout; // millisecond
	public static int g_network_timeout; // millisecond
	public static String g_charset;
	public static int g_tracker_http_port;
	public static boolean g_anti_steal_token; // if anti-steal token
	public static String g_secret_key; // generage token secret key
	public static TrackerGroup g_tracker_group;

	public static final int DEFAULT_CONNECT_TIMEOUT = 5; // second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30; // second

	private ClientGlobal() {
	}

	public static void init(Map<String, String> initProperties) throws
			IOException, FastDFSException {

		//配置连接超时时间
		String gct=initProperties.get("connect_timeout");
		if(gct!=null&&!"".equals(gct)){
			g_connect_timeout=Integer.valueOf(gct);
		}else{
			g_connect_timeout=DEFAULT_CONNECT_TIMEOUT;
		}
		g_connect_timeout *= 1000;

		//配置请求超时时间
		String nct=initProperties.get("network_timeout");
		if(nct!=null&&!"".equals(nct)){
			g_network_timeout=Integer.valueOf(nct);
		}else{
			g_network_timeout=DEFAULT_NETWORK_TIMEOUT;
		}
		g_network_timeout *=1000;

		//配置编码格式
		g_charset = initProperties.get("charset");
		if (g_charset == null || g_charset.length() == 0) {
			g_charset = "ISO8859-1";
		}

		//配置tracker_server
		String[] szTrackerServers;
		String[] parts;
		String tss=initProperties.get("tracker_server");
		if(tss==null||"".equals(tss)){
			throw new FastDFSException("您没有配置tracker_server");
		}else{
			szTrackerServers=tss.split(",|\\|");
			InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
			for (int i = 0; i < szTrackerServers.length; i++) {
				parts = szTrackerServers[i].split("\\:", 2);
				if (parts.length != 2) {
					throw new FastDFSException("tracker_server的值格式错误, 正确的格式为： host:port");
				}
				tracker_servers[i] = new InetSocketAddress(parts[0].trim(),Integer.parseInt(parts[1].trim()));
			}
			//配置tracer_server连接池
			g_tracker_group = new TrackerGroup(tracker_servers);
			//配置http_port
			String thp=initProperties.get("tracker_http_port");
			if(thp!=null&&!"".equals(thp)){
				g_tracker_http_port=Integer.valueOf(thp);
			}else{
				g_tracker_http_port=80;
			}
		}
		//配置防盗链令牌
		String ast=initProperties.get("anti_steal_token");
		if(ast!=null&&!"".equals(ast)){
			if("yes".equalsIgnoreCase(ast)){
				g_anti_steal_token=true;
				g_secret_key = initProperties.get("secret_key");
			}else{
				g_anti_steal_token=true;
			}
		}else{
			g_anti_steal_token=false;
		}
	}

	/**
	 * load global variables
	 * 
	 * @param conf_filename
	 *            config filename
	 */
	public static void init(String conf_filename) throws FileNotFoundException,
			IOException, FastDFSException {
		IniFileReader iniReader;
		String[] szTrackerServers;
		String[] parts;

		iniReader = new IniFileReader(conf_filename);

		g_connect_timeout = iniReader.getIntValue("connect_timeout",
				DEFAULT_CONNECT_TIMEOUT);
		if (g_connect_timeout < 0) {
			g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
		}
		g_connect_timeout *= 1000; // millisecond

		g_network_timeout = iniReader.getIntValue("network_timeout",
				DEFAULT_NETWORK_TIMEOUT);
		if (g_network_timeout < 0) {
			g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
		}
		g_network_timeout *= 1000; // millisecond

		g_charset = iniReader.getStrValue("charset");
		if (g_charset == null || g_charset.length() == 0) {
			g_charset = "ISO8859-1";
		}

		szTrackerServers = iniReader.getValues("tracker_server");
		if (szTrackerServers == null) {
			throw new FastDFSException("item \"tracker_server\" in " + conf_filename
					+ " not found");
		}

		InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
		for (int i = 0; i < szTrackerServers.length; i++) {
			parts = szTrackerServers[i].split("\\:", 2);
			if (parts.length != 2) {
				throw new FastDFSException(
						"the value of item \"tracker_server\" is invalid, the correct format is host:port");
			}

			tracker_servers[i] = new InetSocketAddress(parts[0].trim(),
					Integer.parseInt(parts[1].trim()));
		}
		g_tracker_group = new TrackerGroup(tracker_servers);

		g_tracker_http_port = iniReader.getIntValue("http.tracker_http_port",
				80);
		g_anti_steal_token = iniReader.getBoolValue("http.anti_steal_token",
				false);
		if (g_anti_steal_token) {
			g_secret_key = iniReader.getStrValue("http.secret_key");
		}
	}

	public static int getG_connect_timeout() {
		return g_connect_timeout;
	}

	public static void setG_connect_timeout(int connect_timeout) {
		ClientGlobal.g_connect_timeout = connect_timeout;
	}

	public static int getG_network_timeout() {
		return g_network_timeout;
	}

	public static void setG_network_timeout(int network_timeout) {
		ClientGlobal.g_network_timeout = network_timeout;
	}

	public static String getG_charset() {
		return g_charset;
	}

	public static void setG_charset(String charset) {
		ClientGlobal.g_charset = charset;
	}

	public static int getG_tracker_http_port() {
		return g_tracker_http_port;
	}

	public static void setG_tracker_http_port(int tracker_http_port) {
		ClientGlobal.g_tracker_http_port = tracker_http_port;
	}

	public static boolean getG_anti_steal_token() {
		return g_anti_steal_token;
	}

	public static boolean isG_anti_steal_token() {
		return g_anti_steal_token;
	}

	public static void setG_anti_steal_token(boolean anti_steal_token) {
		ClientGlobal.g_anti_steal_token = anti_steal_token;
	}

	public static String getG_secret_key() {
		return g_secret_key;
	}

	public static void setG_secret_key(String secret_key) {
		ClientGlobal.g_secret_key = secret_key;
	}

	public static TrackerGroup getG_tracker_group() {
		return g_tracker_group;
	}

	public static void setG_tracker_group(TrackerGroup tracker_group) {
		ClientGlobal.g_tracker_group = tracker_group;
	}
}

package com.maoding.core.util;

import javax.servlet.http.HttpServletRequest;



/**
 * 深圳市设计同道技术有限公司
 * 类    名：IPUtil
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月16日-上午9:08:59
 */
public class IPUtil {
	
	/**
	 * 方法描述：
	 * 作        者：Chenxj
	 * 日        期：2015年9月16日-上午9:08:55
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp= request.getRemoteHost();
        }
        if("0:0:0:0:0:0:0:1".equals(remoteIp)){
        	remoteIp="localhost";
        }
		return remoteIp;
	}
}

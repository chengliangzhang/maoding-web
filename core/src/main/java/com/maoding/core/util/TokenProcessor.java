package com.maoding.core.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：TokenProcessor
 * 类描述：令牌生成器
 * 作    者：Chenxj
 * 日    期：2015年8月7日-下午3:01:45
 */
public class TokenProcessor {
	private static Logger log = LoggerFactory.getLogger(TokenProcessor.class);
	private TokenProcessor(){};
	private static TokenProcessor instance=new TokenProcessor();
	public static TokenProcessor getInstance(){
		return instance;
	}
	public String generateTokeCode(HttpServletRequest request){
		String value=System.currentTimeMillis()+request.getSession().getId()+IPUtil.getIpAddr((HttpServletRequest) request);
		try{
			byte[]b=MessageDigest.getInstance("SHA-1").digest(value.getBytes(Charset.forName("UTF-8")));
			//不使用java内部专用API
//			BASE64Encoder be=new BASE64Encoder();
			value=Base64.encodeBase64String(b);
			request.getSession().setAttribute("token", value);
			return value;
		}catch(Exception e){
			log.error("生成令牌失败", e);
		}
		return null;
	}
}

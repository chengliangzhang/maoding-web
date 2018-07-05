package com.maoding.core.constant;

import java.util.Locale;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：Constant
 * 类描述：核心常量类
 * 作    者：Chenxj
 * 日    期：2015年6月29日-上午11:34:27
 */
public class Constant {
	public static final int EXCEL_MAX_ROW=65535;
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();
	public static final String FILE_SEPARATOR = System.getProperty( "file.separator" );
	public static final String FTP_FILE_SEPARATOR = "/";
	public static final String UTF8="UTF-8";
	public static final int DEFAULT_BUFFER=1024;
	/**验证码时效（2分钟）*/
	public static final long SECURITY_CODE_MAX_LIVE_TIME=180000;
	/**验证码时效（10分钟）*/
	public static final long SECURITY_CODE_10_MAX_LIVE_TIME=600000;
	/**令牌时效（一星期）*/
	public static final long TOKEN_MAX_LIVE_TIME=604800000;
}

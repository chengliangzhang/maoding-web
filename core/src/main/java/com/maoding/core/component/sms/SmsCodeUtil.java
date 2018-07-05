package com.maoding.core.component.sms;

import java.util.HashMap;
import java.util.Map;

/**深圳市设计同道技术有限公司
 * 类    名：SmsCodeUtil
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-下午3:47:56
 */
public abstract class SmsCodeUtil {
	protected static Map<String, String>map=new HashMap<String, String>();
	public static String getSmsCodeDesc(String code){
		return map.get(code);
	}
}

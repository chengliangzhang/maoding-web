package com.maoding.core.component.sms.yp;

import java.util.Map;

import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.SmsSenderFactory;


/**深圳市设计同道技术有限公司
 * 类    名：YpSmsSenderFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午11:31:42
 */
public class YpSmsSenderFactory extends SmsSenderFactory{
	
	protected YpSmsSenderFactory(){
		super();
	}
	public static synchronized SmsSenderFactory getInstance(){
		if(instance==null){
			instance=new YpSmsSenderFactory();
		}
		return instance;
	}

	@Override
	protected SmsSender smsSender() {
		Map<String, String>initP=getInitProperties();
		return new YpSmsSender(initP.get("url_yp"), initP.get("apikey"), initP.get("extend"), initP.get("uid"), initP.get("callback_url"));
	}
}

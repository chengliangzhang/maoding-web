package com.maoding.core.component.sms.cl;

import java.util.Map;

import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.SmsSenderFactory;

/**深圳市设计同道技术有限公司
 * 类    名：ClSmsSenderFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午11:08:06
 */
public class ClSmsSenderFactory extends SmsSenderFactory{

	protected ClSmsSenderFactory(){
		super();
	}
	public static synchronized SmsSenderFactory getInstance(){
		if(instance==null){
			instance=new ClSmsSenderFactory();
		}
		return instance;
	}

	@Override
	protected SmsSender smsSender() {
		Map<String, String>initP=getInitProperties();
		return new ClSmsSender(initP.get("url_cl"), initP.get("account"), initP.get("pswd"), initP.get("product"), initP.get("extno"));
	}
}

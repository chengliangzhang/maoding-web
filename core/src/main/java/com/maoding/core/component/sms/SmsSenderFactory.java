package com.maoding.core.component.sms;

import java.util.Map;

import com.maoding.core.component.sms.cl.ClSmsSenderFactory;
import com.maoding.core.component.sms.yp.YpSmsSenderFactory;



/**深圳市设计同道技术有限公司
 * 类    名：SmsSenderFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午10:53:35
 */
public abstract class SmsSenderFactory {
	protected static SmsSenderFactory instance;
	private SmsSender smsSender;
	private Map<String, String>initProperties;
	public static synchronized SmsSenderFactory getInstance(String type){
		SmsSenderEnum ftype=SmsSenderEnum.valueOf(type);
		if(instance==null){
			if(ftype==SmsSenderEnum.CL){
				instance=ClSmsSenderFactory.getInstance();
			}else if(ftype==SmsSenderEnum.YP){
				instance=YpSmsSenderFactory.getInstance();
			}
		}
		return instance;
	}
	public synchronized SmsSender createSmsSender(){
		if(smsSender==null){
			smsSender=instance.smsSender();
		}
		return smsSender;
	}
	protected abstract SmsSender smsSender();
	/**
	 * 获取：initProperties
	 */
	public Map<String, String> getInitProperties() {
		return initProperties;
	}
	/**
	 * 设置：initProperties
	 */
	public void setInitProperties(Map<String, String> initProperties) {
		this.initProperties = initProperties;
	}
}

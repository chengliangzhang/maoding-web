package com.maoding.core.common.factory;

import java.util.Map;

/**深圳市设计同道技术有限公司
 * 类    名：BaseFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年10月15日-上午11:51:24
 */
public abstract class BaseFactory {
	protected Map<String, String>initProperties;
	/**
	 * 获取：初始化参数
	 */
	public Map<String, String> getInitProperties() {
		return initProperties;
	}
	/**
	 * 设置：初始化参数
	 */
	public void setInitProperties(Map<String, String> initProperties) {
		this.initProperties = initProperties;
	}
}

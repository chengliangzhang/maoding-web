package com.maoding.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.maoding.core.test.BaseTest;

/**深圳市设计同道技术有限公司
 * 类    名：SpringContextUtil
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2016年1月28日-上午10:44:09
 */
public class SpringContextUtil {
	public static <T>T getBean(String name,Class<T>clazz){
		ApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx==null){
			ctx=BaseTest.ctx;
		}
		return ctx.getBean(name, clazz);
	}
	public static <T>T getBean(Class<T>clazz){
		ApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		if(ctx==null){
			ctx=BaseTest.ctx;
		}
		return ctx.getBean(clazz);
	}
}

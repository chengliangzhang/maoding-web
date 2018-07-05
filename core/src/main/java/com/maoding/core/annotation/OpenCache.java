package com.maoding.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**深圳市设计同道技术有限公司
 * 类    名：OpenCache
 * 类描述：开启缓存注解
 * 作    者：Chenxj
 * 日    期：2016年2月26日-上午11:47:05
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenCache {
	/**缓存时长,单位毫秒*/
	public long time() default -1;
}

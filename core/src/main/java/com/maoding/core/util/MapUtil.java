package com.maoding.core.util;

import java.util.HashMap;
import java.util.Map;

/**深圳市设计同道技术有限公司
 * 类    名：MapUtil
 * 类描述：map工具类
 * 作    者：Chenxj
 * 日    期：2016年4月22日-下午5:46:25
 */
public class MapUtil {
	/**
	 * 方法描述：生成value为泛型的map
	 * 作        者：Chenxj
	 * 日        期：2016年4月22日-下午6:10:44
	 * @param tt
	 * @return
	 */
	@SafeVarargs
	public static <T>Map<String, T>map(T...tt){
		Map<String, T>map=new HashMap<String, T>();
		for(int i=0;i<tt.length;i++){
			if(i%2!=0){
				map.put(String.valueOf(tt[i-1]), tt[i]);
			}
		}
		return map;
	}
	/**
	 * 方法描述：生成value为Object的map
	 * 作        者：Chenxj
	 * 日        期：2016年4月22日-下午6:11:13
	 * @param tt
	 * @return
	 */
	@SafeVarargs
	public static <T>Map<String, Object>objectMap(T...tt){
		Map<String, Object>map=new HashMap<String, Object>();
		for(int i=0;i<tt.length;i++){
			if(i%2!=0){
				map.put(String.valueOf(tt[i-1]), tt[i]);
			}
		}
		return map;
	}
	/**
	 * 方法描述：生成value为String的map
	 * 作        者：Chenxj
	 * 日        期：2016年4月22日-下午6:11:39
	 * @param tt
	 * @return
	 */
	@SafeVarargs
	public static <T>Map<String, String>stringMap(T...tt){
		Map<String, String>map=new HashMap<String, String>();
		for(int i=0;i<tt.length;i++){
			if(i%2!=0){
				if(tt[i]==null){
					map.put(String.valueOf(tt[i-1]), null);
				}else{
					map.put(String.valueOf(tt[i-1]), String.valueOf(tt[i]));
				}
			}
		}
		return map;
	}
}

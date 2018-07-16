package com.maoding.core.bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：Response
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年8月7日-下午2:52:52
 */
public class ResponseBean extends BaseBean
{
	private static final long serialVersionUID = 6105746542547778835L;
	/**错误状态0无错误1请求失败*/
	private String error;
	/**消息*/
	private String msg;

	/**数据*/
	private Map<String, Object>data=new HashMap<String, Object>();

	/**
	 * 方法描述：添加操作错误信息
	 * 作        者：Chenxj
	 * 日        期：2015年8月11日-上午10:31:44
	 * @param msg
	 * @return
	 */
	public ResponseBean addErrMsg(String msg){
		return addData("err_msg", msg);
	}
	/**
	 * 方法描述：添加列表到data
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午5:58:10
	 * @param list
	 * @return
	 */
	public ResponseBean addList(Collection<? extends Object>list){
		this.addData("list", list);
		return this;
	}
	/**
	 * 方法描述：从Map中添加
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午5:14:32
	 * @param data
	 * @return
	 */
	public ResponseBean addData(Map<String, ? extends Object>data) {
		this.data.putAll(data);
		return this;
	}
	/**
	 * 方法描述：通过Obj添加数据
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午5:36:18
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResponseBean addDataFromObject(Object obj){
		ObjectMapper om=new ObjectMapper();
		addData(om.convertValue(obj,Map.class));
		return this;
	}
	/**
	 * 方法描述：添加多个数据
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午3:41:07
	 * @param datas
	 * @return
	 */
	public ResponseBean addDatas(Collection<? extends Map<String, ? extends Object>>datas) {
		for(Map<String,? extends Object>m:datas){
			this.data.putAll(m);
		}
		return this;
	}
	/**
	 * 方法描述：添加数据
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午2:56:19
	 * @param key
	 * @param value
	 * @return
	 */
	public ResponseBean addData(String key, Object value){
		this.data.put(key, value);
		return this;
	}
	/**
	 * 获取：error
	 */
	public String getError() {
		return error;
	}
	/**
	 * 设置：error
	 */
	public ResponseBean setError(String error) {
		this.error = error;
		return this;
	}
	/**
	 * 获取：msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * 设置：msg
	 */
	public ResponseBean setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	/**
	 * 获取：data
	 */
	public Map<String, Object> getData() {
		return data;
	}
	/**
	 * 设置：data
	 */
	public ResponseBean setData(Map<String, Object> data) {
		this.data = data;
		return this;
	}


	/**
	 * 方法描述：返回成功
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午3:52:07
	 * @return
	 */
	public static ResponseBean responseSuccess() {
		return new ResponseBean().setError("0");
	}

	/**
	 * 方法描述：返回成功
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午3:52:07
	 * @return
	 */
	public static ResponseBean responseSuccess(String msg) {
		return new ResponseBean().setError("0").setMsg(msg);
	}

	/**
	 * 方法描述：返回失败
	 * 作        者：Chenxj
	 * 日        期：2015年8月7日-下午3:52:34
	 * @param msg
	 * @return
	 */
	public static ResponseBean responseError(String msg) {
		return new ResponseBean().setError("1").setMsg(msg);
	}


}

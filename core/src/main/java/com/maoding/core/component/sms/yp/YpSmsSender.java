package com.maoding.core.component.sms.yp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.SenderResponseBean;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.util.StringUtil;

/**深圳市设计同道技术有限公司
 * 类    名：YpSmsSender
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午11:33:02
 */
public class YpSmsSender implements SmsSender{
	private final Logger log=LoggerFactory.getLogger(getClass());
	/**发送地址*/
	private String url;
	/**用户唯一标识*/
	private String apikey;
	/**扩展号*/
	private String extend;
	/**用户自定义唯一ID*/
	private String uid;
	/**短信报告推送地址*/
	private String callback_url;
	public YpSmsSender(String url,String apikey,String extend,String uid,String callback_url) {
		this.url=url;
		this.apikey=apikey;
		this.extend=extend;
		this.uid=uid;
		this.callback_url=callback_url;
	}
	@SuppressWarnings("unchecked")
	@Override
	public SenderResponseBean send(Sms sms) {
		if(sms.getMobile()==null||sms.getMobile().size()==0)return null;
		SenderResponseBean result = null;
		try{
			CloseableHttpClient client=HttpClients.createDefault();
			HttpPost post=new HttpPost(sms.getMobile().size()==1?(url+"send.json"):(url+"multi_send.json"));
			List<NameValuePair>nvps=new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("apikey", apikey));
			nvps.add(new BasicNameValuePair("mobile", StringUtil.joinString(",", sms.getMobile().toArray())));
			nvps.add(new BasicNameValuePair("text", sms.getMsg()));
			nvps.add(new BasicNameValuePair("extend", extend));
			nvps.add(new BasicNameValuePair("uid", uid));
			nvps.add(new BasicNameValuePair("callback_url", callback_url));
			post.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			CloseableHttpResponse response=client.execute(post);
			log.debug("短信接口调用状态::",response.getStatusLine());
			HttpEntity entity=response.getEntity();
			//处理返回结果
			InputStream in = entity.getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			String s=URLDecoder.decode(baos.toString(), "UTF-8");
			
			Map<String, Object>sMap= new ObjectMapper().readValue(s, Map.class);
			if(Integer.parseInt(sMap.get("code").toString())==0){
				result=new SenderResponseBean(new Date(),String.valueOf(sMap.get("code")),String.valueOf(((Map<String, Object>)sMap.get("result")).get("sid")));
			}else{
				result=new SenderResponseBean(new Date(),String.valueOf(sMap.get("code")));
			}
			
			//释放资源
			EntityUtils.consume(entity);
			client.close();
			response.close();
		}catch(Exception e){
			log.error("短信发送出现异常", e);
		}
		return result;
	}
	/**
	 * 获取：发送地址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：发送地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：用户唯一标识
	 */
	public String getApikey() {
		return apikey;
	}
	/**
	 * 设置：用户唯一标识
	 */
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	/**
	 * 获取：扩展号
	 */
	public String getExtend() {
		return extend;
	}
	/**
	 * 设置：扩展号
	 */
	public void setExtend(String extend) {
		this.extend = extend;
	}
	/**
	 * 获取：用户自定义唯一ID
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * 设置：用户自定义唯一ID
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * 获取：短信报告推送地址
	 */
	public String getCallback_url() {
		return callback_url;
	}
	/**
	 * 设置：短信报告推送地址
	 */
	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
	
}

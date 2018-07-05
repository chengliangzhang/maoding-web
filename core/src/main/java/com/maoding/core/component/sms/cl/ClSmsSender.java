package com.maoding.core.component.sms.cl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.SenderResponseBean;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.util.StringUtil;


/**深圳市设计同道技术有限公司
 * 类    名：ClSmsSender
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午11:32:37
 */
public class ClSmsSender implements SmsSender{
	private final Logger log=LoggerFactory.getLogger(getClass());
	private String url;
	private String account;
	private String pswd;
	private String product;
	private String extno;
	protected ClSmsSender(String url,String account,String pswd,String product,String extno){
		this.url=url;
		this.account=account;
		this.pswd=pswd;
		this.product=product;
		this.extno=extno;
	}
	
	@Override
	public SenderResponseBean send(Sms sms){
		if(sms.getMobile()==null||sms.getMobile().size()==0)return null;
		SenderResponseBean result = null;
		try{
			CloseableHttpClient client=HttpClients.createDefault();
//			HttpPost post=new HttpPost((sms.getMobile().size()==1)?(url+"HttpSendSM"):(url+"HttpBatchSendSM"));
			HttpPost post=new HttpPost(url+"HttpBatchSendSM");
			List<NameValuePair>nvps=new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("account", account));
			nvps.add(new BasicNameValuePair("pswd", pswd));
			nvps.add(new BasicNameValuePair("mobile", StringUtil.joinString(",", sms.getMobile().toArray())));
			nvps.add(new BasicNameValuePair("needstatus", sms.isNeedstatus()?"true":"false"));
			nvps.add(new BasicNameValuePair("msg", sms.getMsg()));
			nvps.add(new BasicNameValuePair("product", product));
			nvps.add(new BasicNameValuePair("extno", extno));
			post.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
//			System.out.println("POST::"+EntityUtils.toString(new UrlEncodedFormEntity(nvps)));
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
			String[]ss=s.split("\n");
			if(ss.length>1){
				String sss[]=ss[0].split(",");
				result=new SenderResponseBean(sss[0], sss[1], ss[1]);
			}else{
				String sss[]=ss[0].split(",");
				result=new SenderResponseBean(sss[0], sss[1]);
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
}

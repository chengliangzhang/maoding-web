package com.maoding.core.component.mail.bean;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**© 2015-2015 CCLooMi.Inc Copyright
 * 类    名：Mail
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2015年7月13日-下午9:31:45
 */
public class Mail {
	private final Logger log=LoggerFactory.getLogger(getClass());
	/**收件人地址*/
	private String to;
	/**抄送人地址*/
	private String cc;
	/**邮件主题*/
	private String subject;
	/**纯文本格式邮件正文*/
	private String body;
	/**超文本格式邮件正文*/
	private String htmlBody;
	/**是否发送超文本格式邮件*/
	private boolean isHtml;
	/**附件列表*/
	private List<String>fileList=new ArrayList<String>();
	/**收件人地址列表*/
	private List<InternetAddress> toAddress=new ArrayList<InternetAddress>();
	/**抄送人地址列表*/
	private List<InternetAddress> ccAddress=new ArrayList<InternetAddress>();
	/**获取 收件人地址*/
	public String getTo() {
		return to;
	}
	/**设置 收件人地址*/
	public void setTo(String to) {
		this.to = to;
		if(to!=null){
			String[]stos=this.to.split(";");
			for(int i=0;i<stos.length;i++){
				try{
					toAddress.add(new InternetAddress(MimeUtility.encodeText(stos[i])));
				}catch(Exception e){
					log.debug("编码发送地址异常",e);
				}
			}
		}
	}
	/**获取 抄送人地址*/
	public String getCc() {
		return cc;
	}
	/**设置 抄送人地址*/
	public void setCc(String cc) {
		this.cc = cc;
		if(cc!=null){
			String[]sccs=this.cc.split(";");
			for(int i=0;i<sccs.length;i++){
				try {
					ccAddress.add(new InternetAddress(MimeUtility.encodeText(sccs[i])));
				} catch (Exception e) {
					log.debug("编码抄送地址异常",e);
				}
			}
		}
	}
	/**获取 邮件主题*/
	public String getSubject() {
		return subject;
	}
	/**设置 邮件主题*/
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**获取 纯文本格式邮件正文*/
	public String getBody() {
		return body;
	}
	/**设置 纯文本格式邮件正文*/
	public void setBody(String body) {
		this.body = body;
		this.isHtml=false;
	}
	/**获取 超文本格式邮件正文*/
	public String getHtmlBody() {
		return htmlBody;
	}
	/**设置 超文本格式邮件正文*/
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
		this.isHtml=true;
	}
	/**获取 是否发送超文本格式邮件*/
	public boolean isHtml() {
		return isHtml;
	}
	/**设置 是否发送超文本格式邮件*/
//	不提供设置方法，此值由邮件内容确定
//	public void setHtml(boolean isHtml) {
//		this.isHtml = isHtml;
//	}
	/**获取 附件列表*/
	public List<String> getFileList() {
		return fileList;
	}
	/**设置 附件列表*/
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	/**获取 收件人地址列表*/
	public InternetAddress[] getToAddress() {
		int size=toAddress.size();
		InternetAddress[]tos=new InternetAddress[size];
		for(int i=0;i<size;i++){
			tos[i]=toAddress.get(i);
		}
		return tos;
	}
	/**设置 收件人地址列表*/
	public void setToAddress(List<InternetAddress> toAddress) {
		this.toAddress = toAddress;
	}
	/**获取 抄送人地址列表*/
	public InternetAddress[] getCcAddress() {
		int size=ccAddress.size();
		InternetAddress[]ccs=new InternetAddress[size];
		for(int i=0;i<size;i++){
			ccs[i]=ccAddress.get(i);
		}
		return ccs;
	}
	/**设置 抄送人地址列表*/
	public void setCcAddress(List<InternetAddress> ccAddress) {
		this.ccAddress = ccAddress;
	}
	public void addToAddress(String to){
		try{
			toAddress.add(new InternetAddress(MimeUtility.encodeText(to)));
		}catch(Exception e){
			log.debug("编码发送地址异常",e);
		}
	}
	public void addCcAddress(String cc){
		try {
			ccAddress.add(new InternetAddress(MimeUtility.encodeText(cc)));
		} catch (Exception e) {
			log.debug("编码抄送地址异常",e);
		}
	}
	public boolean isToAddressEmpty(){
		return toAddress.size()==0?true:false;
	}
	public boolean isCcAddressEmpty(){
		return ccAddress.size()==0?true:false;
	}
	public boolean hasFilesToSend(){
		return fileList.size()==0?false:true;
	}
}

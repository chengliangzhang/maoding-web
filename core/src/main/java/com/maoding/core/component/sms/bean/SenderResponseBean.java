package com.maoding.core.component.sms.bean;

import java.util.Date;

import com.maoding.core.bean.BaseBean;
import com.maoding.core.component.sms.cl.ClSmsCodeUtil;
import com.maoding.core.component.sms.yp.YpSmsCodeUtil;


/**深圳市设计同道技术有限公司
 * 类    名：SenderResponseBean
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月23日-上午11:44:47
 */
public class SenderResponseBean extends BaseBean{
	private static final long serialVersionUID = -4530236832100102529L;
	private Date date;
	private String codeKey;
	private String code;
	private String smsid;
	public SenderResponseBean (){
		super();
	}
	public SenderResponseBean(String timeStr,String code,String smsid){
		this.date=new Date(Long.parseLong(timeStr));
		this.codeKey=code;
		if(this.code==null)this.code=YpSmsCodeUtil.getSmsCodeDesc(code);
		if(this.code==null)this.code=ClSmsCodeUtil.getSmsCodeDesc(code);
		this.smsid=smsid;
	}
	public SenderResponseBean(String timeStr,String code){
		this.date=new Date(Long.parseLong(timeStr));
		this.codeKey=code;
		if(this.code==null)this.code=YpSmsCodeUtil.getSmsCodeDesc(code);
		if(this.code==null)this.code=ClSmsCodeUtil.getSmsCodeDesc(code);
	}
	public SenderResponseBean(Date date,String code,String smsid){
		this.date=date;
		this.codeKey=code;
		if(this.code==null)this.code=YpSmsCodeUtil.getSmsCodeDesc(code);
		if(this.code==null)this.code=ClSmsCodeUtil.getSmsCodeDesc(code);
		this.smsid=smsid;
	}
	public SenderResponseBean(Date date,String code){
		this.date=date;
		this.codeKey=code;
		if(this.code==null)this.code=YpSmsCodeUtil.getSmsCodeDesc(code);
		if(this.code==null)this.code=ClSmsCodeUtil.getSmsCodeDesc(code);
	}
	/**
	 * 获取：date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * 设置：date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * 获取：codeKey
	 */
	public String getCodeKey() {
		return codeKey;
	}
	/**
	 * 设置：codeKey
	 */
	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}
	/**
	 * 获取：code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：smsid
	 */
	public String getSmsid() {
		return smsid;
	}
	/**
	 * 设置：smsid
	 */
	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}
}

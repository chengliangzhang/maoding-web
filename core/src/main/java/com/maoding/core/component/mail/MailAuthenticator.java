package com.maoding.core.component.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 类名：MailAuthenticator
 * 描述：邮箱权限验证
 * 作者： Chenxj
 * 日期：2015年7月13日 - 下午6:20:42
 */
public class MailAuthenticator extends Authenticator{
	/**用户名*/
	private String username;
	/**密码*/
	private String password;
	/**获取 用户名*/
	public String getUsername() {
		return username;
	}
	/**设置 用户名*/
	public void setUsername(String username) {
		this.username = username;
	}
	/**获取 密码*/
	public String getPassword() {
		return password;
	}
	/**设置 密码*/
	public void setPassword(String password) {
		this.password = password;
	}
	
	public MailAuthenticator(String username,String password){
		this.username=username;
		this.password=password;
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}

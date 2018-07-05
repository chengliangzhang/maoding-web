package com.maoding.core.component.sms;

import com.maoding.core.component.sms.bean.SenderResponseBean;
import com.maoding.core.component.sms.bean.Sms;


/**深圳市设计同道技术有限公司
 * 类    名：SmsSender
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年9月28日-上午10:57:33
 */
public interface SmsSender {
	public SenderResponseBean send(Sms sms);
}

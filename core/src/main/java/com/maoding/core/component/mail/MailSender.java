package com.maoding.core.component.mail;

import java.io.File;
import java.net.URLEncoder;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoding.core.component.mail.bean.Mail;


/**
 * 类名：MailSender
 * 描述：邮件发送
 * 作者： Chenxj
 * 日期：2015年7月13日 - 下午6:21:51
 */
public class MailSender {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Session session;

    /**
     * 发件人姓名
     */
    private String fromname;

    private String sendName;

    private String password;

    private Properties properties;
    /**
     * 发送邮箱个数
     */
    private int mCount;

    private String username;

    private static int sendSort = 0;

    private String[] userNames;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public static int getSendSort() {
        return sendSort;
    }

    public static void setSendSort(int sendSort) {
        MailSender.sendSort = sendSort;
    }

    public String[] getUserNames() {
        return userNames;
    }

    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }

    /**
     * 获取 发件人姓名
     */
    public String getFromname() {
        return fromname;
    }

    /**
     * 设置 发件人姓名
     */
    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    /**
     * 获取 session
     */
    public Session getSession() {
        userNames=username.split("[,]");
        sendName=userNames[sendSort];
        if((sendSort+1)==mCount){
            sendSort=0;
        }
        else {
           sendSort= sendSort+1;
        }
        return session=Session.getInstance(properties, new MailAuthenticator(sendName, password));
    }

    /**
     * 设置 session
     */
    public void setSession(Session session) {

        this.session = Session.getInstance(properties, new MailAuthenticator(sendName, password));
    }

    public MailSender(Properties properties, String fromname, String password, int mCount, String username) {
        this.fromname = fromname;
        this.password = password;
        this.mCount = mCount;
        this.username=username;
        this.properties=properties;
    }

    /***
     * 方法描述:发送邮件
     * @param mail
     * @return
     */
    public boolean sendMail(Mail mail) {
        try {
            MimeMessage mmsg = new MimeMessage(getSession());
            //设置收件地址
            if (!mail.isToAddressEmpty()) {
                mmsg.setRecipients(RecipientType.TO, mail.getToAddress());
            } else {
                log.error("没有收件地址");
                return false;
            }
            //设置抄送地址
            if (!mail.isCcAddressEmpty()) {
                mmsg.setRecipients(RecipientType.CC, mail.getCcAddress());
            }
            String companyName = javax.mail.internet.MimeUtility.encodeText(fromname, "utf-8", "B");
            InternetAddress ia = new InternetAddress(companyName + "<" + sendName + ">");
//			//设置发件人名称
            /*InternetAddress ia = new InternetAddress("services@imaoding.com", "深圳市卯丁技术有限公司", "UTF-8");*/
            mmsg.setFrom(ia);
            //设置主题
            mmsg.setSubject(mail.getSubject());
            if (mail.hasFilesToSend()) {//有附件
                Multipart mt = new MimeMultipart();
                BodyPart contentPart = new MimeBodyPart();
                if (mail.isHtml()) {
                    contentPart.setDataHandler(new DataHandler(mail.getHtmlBody(), "text/html;charset=UTF-8"));
                } else {
                    contentPart.setDataHandler(new DataHandler(mail.getBody(), "text/plain;charset=UTF-8"));
                }
                mt.addBodyPart(contentPart);
                //添加附件
                for (String f : mail.getFileList()) {
                    File file = new File(f);
                    BodyPart bp = new MimeBodyPart();
                    DataSource ds = new FileDataSource(file);
                    bp.setDataHandler(new DataHandler(ds));
                    try {
                        bp.setFileName(MimeUtility.encodeText(file.getName()));
                    } catch (Exception e) {
                        log.error("邮件附件名设置异常", e);
                    }
                    mmsg.setContent(mt);
                }
            } else {//无附件
                if (mail.isHtml()) {
                    mmsg.setContent(mail.getHtmlBody(), "text/html;charset=UTF-8");
                } else {
                    mmsg.setContent(mail.getBody(), "text/plain;charset=UTF-8");
                }
            }
            mmsg.saveChanges();
            Transport.send(mmsg);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return false;
        }
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

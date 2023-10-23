package com.jef.util;

import com.jef.entity.MailAuthenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author Jef
 * @date 2020/4/4
 */
public class MailOperationUtil {
    /**
     * 发送邮件
     * @param user 发件人邮箱
     * @param password 授权码（注意不是邮箱登录密码）
     * @param host 发送服务器
     * @param from 发件人
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return success 发送成功 failure 发送失败
     * @throws Exception
     */
    public String sendMail(String user, String password, String host, String from, String to, String subject, String content) {
        if (to != null){
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            MailAuthenticator auth = new MailAuthenticator();
            MailAuthenticator.USERNAME = user;
            MailAuthenticator.PASSWORD = password;
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                if (!"".equals(to.trim())) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.trim()));
                }
                message.setSubject(subject);
                // 正文
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setContent(content, "text/html;charset=utf-8");
                // 整个邮件：正文+附件
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                // mp.addBodyPart(mbp2);
                message.setContent(mp);
                message.setSentDate(new Date());
                message.saveChanges();
                Transport.send(message);
            } catch (Exception e){
                e.printStackTrace();
                return "failure";
            }
            return "success";
        }else{
            return "failure";
        }
    }

}
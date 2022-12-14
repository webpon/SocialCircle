package com.socialCircle.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SentSimpleMail {

    @Value("${com.socialCircle.email.host}")
    private  String MAIL_HOST;
    @Value("${com.socialCircle.email.addr}")
    private String FROM;
    @Value("${com.socialCircle.email.password}")
    private String password;
    /**
     * 用qq邮箱发送一个简单邮件
     *
     * @param subject
     * @param text
     * @param toRecipients 接收邮件，逗号分隔
     * @throws AddressException
     * @throws MessagingException
     */
    public void sentSimpleMail(String subject, String text, String toRecipients)
            throws AddressException, MessagingException {
        /*
         * 初始化JavaMail会话
         */
        Properties props = System.getProperties(); // 获得系统属性配置，用于连接邮件服务器的参数配置
        props.setProperty("mail.smtp.host", MAIL_HOST); // 发送邮件的主机
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props, null);// 获得Session对象
        /*
         * 创建邮件消息，发送邮件
         */
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        // To: 收件人
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toRecipients, false));
        message.setSubject(subject); // 邮件标题
        message.setText(text); // 邮件内容
        Transport.send(message, FROM, password); // 授权码
    }

}

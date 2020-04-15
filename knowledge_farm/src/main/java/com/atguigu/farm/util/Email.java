package com.atguigu.farm.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class Email {
	private static String MAIL_EMAIL = "15133307201@163.com";
	private static String MAIL_PWD = "sjw199833";
	private static String MAIL_PORT = "465";
	private static String MAIL_HOST = "smtp.163.com";
	private static String MAIL_SMTP_AUTH = "true";
	public static String code ="";

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		Email.code = code;
	}

	public static Session getSession() {
		//创建连接对象，链接到邮箱服务器
		Properties properties = new Properties();
		//设置邮件发送协议
		properties.put("mail.transport.protocol","smtp");
		//设置发送邮件服务器
		properties.put("mail.smtp.host",MAIL_HOST);
		//设置授权
		properties.setProperty("mail.smtp.auth",MAIL_SMTP_AUTH);
		//设置端口号
		properties.put("mail.smtp.port",MAIL_PORT);
		properties.put("mail.smtp.starttls.enable",true);
		//2.根据配置创建会话对象，用于和邮件服务器交互
		Session session = Session.getInstance(properties,new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MAIL_EMAIL, MAIL_PWD);
			}
		});
		session.setDebug(true);

		return session;
	}

	/*
	 * 发送绑定邮箱验证码邮件
	 *
	 * @param to 邮件接收方
	 * @param code 邮件的验证码
	 *
	 */
	public static boolean bindingMail(String to){
		final String username = MAIL_EMAIL;
		final String password = MAIL_PWD;
		String subject = "为保证您的账号安全请验证邮箱";
		try {
			//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			//设置邮件会话参数
			Properties props = new Properties();
			//邮箱的发送服务器地址
			props.setProperty("mail.smtp.host", MAIL_HOST);
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			//邮箱发送服务器端口,这里设置为465端口
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			//获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			//通过会话,得到一个邮件,用于发送
			Message msg = new MimeMessage(session);
			//设置发件人
			msg.setFrom(new InternetAddress(MAIL_EMAIL,"知识农场","UTF-8"));
			//设置收件人,to为收件人,cc为抄送,bcc为密送
			//msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			//msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));
			String encodedSubject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset("UTF-8"), null);
			msg.setSubject(encodedSubject);
			//设置邮件消息
			msg.setContent("<h3>【知识农场】</h3>您的验证码是"+randomCode()+
					",您正在进行绑定邮箱验证,2分钟内有效。(请勿向任何人提供您收到的验证码)","text/html;charset=UTF-8");
			//设置发送的日期
			msg.setSentDate(new Date());

			//调用Transport的send方法去发送邮件
			Transport.send(msg);
			if(true){
				System.out.printf("----emial发送成功----"+code);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.printf("----emial发送失败----");
		}
		return false;
	}

	/*
	 * 发送找回密码邮件
	 *
	 * @param to 邮件接收方
	 * @param code 邮件的验证码
	 *
	 */
	public static boolean findPasswordByMail(String to){
		final String username = MAIL_EMAIL;
		final String password = MAIL_PWD;
		String subject = "为保证您的账号安全请验证邮箱";
		try {
			//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			//设置邮件会话参数
			Properties props = new Properties();
			//邮箱的发送服务器地址
			props.setProperty("mail.smtp.host", MAIL_HOST);
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			//邮箱发送服务器端口,这里设置为465端口
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			//获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			//通过会话,得到一个邮件,用于发送
			Message msg = new MimeMessage(session);
			//设置发件人
			msg.setFrom(new InternetAddress(MAIL_EMAIL,"知识农场","UTF-8"));
			//设置收件人,to为收件人,cc为抄送,bcc为密送
			//msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			//msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));
			String encodedSubject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset("UTF-8"), null);
			msg.setSubject(encodedSubject);
			//设置邮件消息
			msg.setContent("<h3>【知识农场】</h3>您的验证码是"+randomCode()+
					",您正在进行邮箱验证找回密码,2分钟内有效。(请勿向任何人提供您收到的验证码)","text/html;charset=UTF-8");
			//设置发送的日期
			msg.setSentDate(new Date());

			//调用Transport的send方法去发送邮件
			Transport.send(msg);
			if(true){
				System.out.printf("----emial发送成功----"+code);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.printf("----emial发送失败----");
		}
		return false;
	}

	public static String randomCode() {
		String c = "";
		for (int i = 0; i < 4; i++) {
			c += String.valueOf((int) (Math.random() * 10));
		}
		setCode(c);
		return c;
	}
}

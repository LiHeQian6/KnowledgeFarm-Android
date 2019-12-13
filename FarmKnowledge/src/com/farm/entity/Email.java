package com.farm.entity;

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
		//�������Ӷ������ӵ����������
	    Properties properties = new Properties();
	    //�����ʼ�����Э��
	    properties.put("mail.transport.protocol","smtp");
	    //���÷����ʼ�������
	    properties.put("mail.smtp.host",MAIL_HOST);
	    //������Ȩ
	    properties.setProperty("mail.smtp.auth",MAIL_SMTP_AUTH);
	    //���ö˿ں�
	    properties.put("mail.smtp.port",MAIL_PORT);
	    properties.put("mail.smtp.starttls.enable",true);
	    //2.�������ô����Ự�������ں��ʼ�����������
	    Session session = Session.getInstance(properties,new Authenticator() {
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    		return new PasswordAuthentication(MAIL_EMAIL, MAIL_PWD);
	      }
	    });
	    session.setDebug(true);
	    
		return session;
	}
	
	/*
	 * ���Ͱ�������֤���ʼ�
	 *
	 * @param to �ʼ����շ�
	 * @param code �ʼ�����֤��
	 * 
	 */
	public static boolean bindingMail(String to){
		final String username = MAIL_EMAIL;
	    final String password = MAIL_PWD;
		String subject = "Ϊ��֤�����˺Ű�ȫ����֤����";
		try {
	        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	        //�����ʼ��Ự����
	        Properties props = new Properties();
	        //����ķ��ͷ�������ַ
	        props.setProperty("mail.smtp.host", MAIL_HOST);
	        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.smtp.socketFactory.fallback", "false");
	        //���䷢�ͷ������˿�,��������Ϊ465�˿�
	        props.setProperty("mail.smtp.port", "465");
	        props.setProperty("mail.smtp.socketFactory.port", "465");
	        props.put("mail.smtp.auth", "true");
	        //��ȡ������Ự,���������ڲ���ķ�ʽ,�������������û�����������Ȩ��jvm
	        Session session = Session.getDefaultInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });
	        //ͨ���Ự,�õ�һ���ʼ�,���ڷ���
	        Message msg = new MimeMessage(session);
	        //���÷�����
	        msg.setFrom(new InternetAddress(MAIL_EMAIL,"֪ʶũ��","UTF-8"));
	        //�����ռ���,toΪ�ռ���,ccΪ����,bccΪ����
            //msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));
            String encodedSubject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset("UTF-8"), null);
            msg.setSubject(encodedSubject);
            //�����ʼ���Ϣ
            msg.setContent("<h3>��֪ʶũ����</h3>������֤����"+randomCode()+
                    ",�����ڽ��а�������֤,2��������Ч��(�������κ����ṩ���յ�����֤��)","text/html;charset=UTF-8");
            //���÷��͵�����
            msg.setSentDate(new Date());
            
            //����Transport��send����ȥ�����ʼ�
            Transport.send(msg);
            if(true){
            	System.out.printf("----emial���ͳɹ�----"+code);
            	return true;	
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("----emial����ʧ��----");
        }
    	return false;
     }
	
     /*
      * �����һ������ʼ�
     *
     * @param to �ʼ����շ�
     * @param code �ʼ�����֤��
     * 
     */
     public static boolean findPasswordByMail(String to){
    	 final String username = MAIL_EMAIL;
        final String password = MAIL_PWD;
		String subject = "Ϊ��֤�����˺Ű�ȫ����֤����";
		try {
            //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            //�����ʼ��Ự����
            Properties props = new Properties();
            //����ķ��ͷ�������ַ
            props.setProperty("mail.smtp.host", MAIL_HOST);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            //���䷢�ͷ������˿�,��������Ϊ465�˿�
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            //��ȡ������Ự,���������ڲ���ķ�ʽ,�������������û�����������Ȩ��jvm
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            //ͨ���Ự,�õ�һ���ʼ�,���ڷ���
            Message msg = new MimeMessage(session);
            //���÷�����
            msg.setFrom(new InternetAddress(MAIL_EMAIL,"֪ʶũ��","UTF-8"));
            //�����ռ���,toΪ�ռ���,ccΪ����,bccΪ����
            //msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));
            String encodedSubject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset("UTF-8"), null);
            msg.setSubject(encodedSubject);
            //�����ʼ���Ϣ
            msg.setContent("<h3>��֪ʶũ����</h3>������֤����"+randomCode()+
                    ",�����ڽ���������֤�һ�����,2��������Ч��(�������κ����ṩ���յ�����֤��)","text/html;charset=UTF-8");
            //���÷��͵�����
            msg.setSentDate(new Date());
            
            //����Transport��send����ȥ�����ʼ�
            Transport.send(msg);
            if(true){
	        	System.out.printf("----emial���ͳɹ�----"+code);
	        	return true;	
	        }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("----emial����ʧ��----");
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

package com.obs.integrator.panacess;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class PanacessThreadedQueueAdapter {
	
	static Logger logger = Logger.getLogger("");
	private static String mailId;
	private static String password;
	private static int portNumber;
	private static PropertiesConfiguration prop;
	
	public static void main(String[] args) {

		try {
			Queue<PanacessProcessRequestData> queue = new ConcurrentLinkedQueue<PanacessProcessRequestData>();
			prop = new PropertiesConfiguration("PanacessIntegrator.ini");
			System.out.println("Adapter Started...");
			
			String logPath=prop.getString("LogFilePath");
			File filelocation = new File(logPath);			
			if(!filelocation.isDirectory()){
				filelocation.mkdirs();
			}	
			
			Logger logger = Logger.getRootLogger();
			FileAppender appender = (FileAppender)logger.getAppender("fileAppender");
			appender.setFile(logPath+"/PanacessIntegrator.log");
			appender.activateOptions();
		
			
			PanacessProducer p = new PanacessProducer(queue,prop);
			PanacessConsumer c= new PanacessConsumer(queue, prop);
            
			
			Thread t1 = new Thread(p);
			Thread t2 = new Thread(c);

			t1.start();
			t2.start();
			
		} catch (ConfigurationException e) {
			System.out.println("(ConfigurationException) Properties file loading error.... : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
		

	}

	public static void sendToUserEmail(String exceptionReason) {
		
		mailId = prop.getString("mailId");
		String hostName = prop.getString("hostName");
		String starttls = prop.getString("starttls");
		password = prop.getString("password1");
		String setContentString = prop.getString("setContentString");
		String port = prop.getString("port");
		String receiptEmailId = prop.getString("receiptEmailId");
		
		if (port.isEmpty()) {
			portNumber = Integer.parseInt("25");
		} else {
			portNumber = Integer.parseInt(port);
		}
		
		String subject = " Beenius Adapter Stopped";
		String header = " Dear User <br/><br/>";
		String body = " Panaacess Adapter is Stopped. <br/>Reason:"+ exceptionReason + "<br/><br/>";
		String footer = "Thank You. <br/>";
		
		 //1) get the session object      
	     Properties properties = System.getProperties();  
	     properties.setProperty("mail.smtp.host", hostName);   
	     properties.put("mail.smtp.ssl.trust",hostName);
	     properties.put("mail.smtp.auth", "true");  
	     properties.put("mail.smtp.starttls.enable", starttls);//put as false
	     properties.put("mail.smtp.starttls.required", starttls);//put as false
	     properties.put("mail.smtp.port", portNumber);


	     Session session = Session.getDefaultInstance(properties,   
	             new javax.mail.Authenticator() {   
	         protected PasswordAuthentication getPasswordAuthentication() {   
	             return new PasswordAuthentication(mailId,password);    }   });       

	     //2) compose message      
		try {
			MimeMessage message = new MimeMessage(session);
			//message.setFrom(new InternetAddress(emailDetail.getMessageFrom()));
			
			message.setFrom(new InternetAddress(mailId));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(receiptEmailId));
			message.setSubject(subject);

			StringBuilder messageBuilder = new StringBuilder()
					.append(header)
					.append(body)
					.append(footer);

			// 3) create MimeBodyPart object and set your message text
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(messageBuilder.toString(),setContentString);
			
			
			// 5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			
			// 6) set the multiplart object to the message object
			message.setContent(multipart);

			// 7) send message
			Transport.send(message);

		} catch (Exception e) {
			logger.error("throwing Exception, Reason:" + stackTrace(e));
		}		
	}
	
	private static String stackTrace(Exception ex){
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}

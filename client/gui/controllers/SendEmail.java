package client.gui.controllers;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail extends Thread {
	private String msg;
	private String to;
	
	 public SendEmail (String to, String msg) {
		 this.to = to;
		 this.msg=msg;
	 }
	
	  public void main(String[] args) {
	    this.start();
	  }
	  public void run() {
	    	String to = "nitzan963@gmail.com";
	    	
	    	final String from = "mebite857@gmail.com";
	    	final String Password = "group@#16";

	 
	        Properties prop = new Properties();
	  		prop.put("mail.smtp.host", "smtp.gmail.com");
	        prop.put("mail.smtp.port", "587");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.starttls.enable", "true"); 

	         
	         Session session = Session.getInstance(prop,
	                 new javax.mail.Authenticator() {
	                     protected PasswordAuthentication getPasswordAuthentication() {
	                         return new PasswordAuthentication(from, Password);
	                     }
	                 });

	         try {

	             Message message = new MimeMessage(session);
	             message.setFrom(new InternetAddress(from));
	             message.setRecipients(Message.RecipientType.TO,  InternetAddress.parse(to));
	            
	             message.setSubject("Your order's status updated!");
	             message.setText(msg);

	             Transport.send(message);

	         } catch (MessagingException e) {
	             e.printStackTrace();
	         }
	  }

	}

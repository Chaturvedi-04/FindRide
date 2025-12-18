package com.alpha.FindRide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(String tomailid, String subject, String message) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(tomailid);
		mail.setSubject(subject);
		mail.setFrom("charan.jeedimatla@gmail.com");
		mail.setText(message);
		
		mailSender.send(mail);
	}
	
}

package com.example.jmaster.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine;

	public void testEmail() {
		String to = "trientran2722002@gmail.com";
		String subject = "Trien ngu";
		String body = "<h1>Trien ngu</h1>";

		sendMail(to, subject, body);
	}

	public void sendBirthdayEmail(String to, String name) {
		String subject = "Happy Birthday !" + name;
		
		Context context = new Context();
		context.setVariable("name", name);
		
		String body = templateEngine.process("email/hpbd.html", context);

		sendMail(to, subject, body);
	}

	public void sendMail(String to, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);// true : ho tro HTML
			helper.setFrom("manh4269@gmail.com");

			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

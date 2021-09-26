package com.sopoong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sopoong.model.entity.User;



@Service
public class EmailService {
	
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMail(User user) throws MailException{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getUserEmail());
	    message.setFrom("Sopoong");
		message.setSubject("Sopoong 가입 인증");
		message.setText("인증번호 : " +user.getAuthNumber());
		
		javaMailSender.send(message);
	}
	
	public void sendFindIdMail(User user) throws MailException{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getUserEmail());
	    message.setFrom("Sopoong");
		message.setSubject("Sopoong 아이디 찾기 인증");
		message.setText("인증번호 : " +user.getAuthNumber().substring(5));
		
		javaMailSender.send(message);
	}
}

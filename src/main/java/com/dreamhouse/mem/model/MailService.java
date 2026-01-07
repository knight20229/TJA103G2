package com.dreamhouse.mem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // 從 application.properties 讀取寄件人
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);      // 寄件人來自 application.properties
        message.setTo(to);          // 收件人
        message.setSubject(subject);// 主旨
        message.setText(text);      // 內容
        mailSender.send(message);
    }
}
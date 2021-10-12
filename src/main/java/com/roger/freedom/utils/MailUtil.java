package com.roger.freedom.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 邮件发送
 * @author kanmeijie
 */
@Component
public class MailUtil {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送纯文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     */
    public void sendMail(String to, String subject, String text) {
        System.out.println("=====sendMail begin");
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println("=====sendMail begin 11111");

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        System.out.println("=====sendMail begin 22222");
        javaMailSender.send(message);
        System.out.println("=====sendMail begin 33333");
    }
}

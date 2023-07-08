package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class EmailService {
    @Value("${spring.mail.username}")
    private String from;
    private  final JavaMailSender javaMailSender;

    private final OrdersService ordersService;
    @Async
    public void sendMessage(String text,String subject,String ...to){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
            messageHelper.setText(text);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom(from);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Async
    public void sendMessage(String text,String subject,String to){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
            messageHelper.setText(text);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom(from);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Sends reminder messages to email that may have ordered games for tomorrow
     */
//    @Scheduled(cron = "0 */2 * * * *")
    public void sendRemindersTheDayBefore(){
        List<String> emails = ordersService.receiveEmailsForReminderMessagesTheDayBefore();
        emails.forEach(email->{
            String text = "This is the reminder for tomorrow's game";
            String subject = "game reminder";
            sendMessage(text,subject,email);
        });

    }


}

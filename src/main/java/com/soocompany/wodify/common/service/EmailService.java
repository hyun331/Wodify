package com.soocompany.wodify.common.service;

import com.soocompany.wodify.common.dto.EmailDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    String senderEmail = "sus03319@gmail.com";
    public MimeMessage createEmail(EmailDto emailDto){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, emailDto.getReceiverEmail());
            message.setSubject(emailDto.getEmailTitle());
            String body = emailDto.getEmailContent();
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void sendEmail(EmailDto emailDto) {
        MimeMessage message = createEmail(emailDto);
        try{
            javaMailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();

        }
    }





}
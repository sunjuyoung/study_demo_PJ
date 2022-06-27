package com.demo.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
            messageHelper.setTo(emailMessage.getTo());
            messageHelper.setSubject(emailMessage.getSubject());
            messageHelper.setText(emailMessage.getMessage(),true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error(e.getMessage());
        }
    }
}

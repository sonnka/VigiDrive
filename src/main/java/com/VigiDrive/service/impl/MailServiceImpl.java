package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.MailException;
import com.VigiDrive.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    private final Context context = new Context(Locale.US);

    @Override
    @Transactional
    public void sendNewAdminMessage(String receiverEmail) throws MailException {
        sendMessage(receiverEmail, "newAdminLetter.html", "Congratulations!");
    }

    @Override
    @Transactional
    public void sendNotApprovedAdminMessage(String receiverEmail) throws MailException {
        sendMessage(receiverEmail, "notApprovedAdminLetter", "Updated your admin status!");
    }

    @Override
    @Transactional
    public void sendApprovedAdminMessage(String receiverEmail, String tempPassword) throws MailException {
        context.setVariable("password", tempPassword);
        sendMessage(receiverEmail, "approvedAdminLetter", "Updated your admin status!");
    }

    private void sendMessage(String email, String templateName, String subject) throws MailException {
        String process = templateEngine.process(templateName, context);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setSubject(subject);
            helper.setText(process, true);
            helper.setTo(email);
        } catch (MessagingException e) {
            throw new MailException(MailException.MailExceptionProfile.FAIL_SEND_MAIL);
        }

        emailSender.send(mimeMessage);
    }
}

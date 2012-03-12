package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 11.3.12
 * Time: 23:57
 */
public class SpringJavaMailService implements MailService {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private SimpleMailMessage mailMessage;
    @Value("${app.domain}")
    private String domain;

    @Override
    public void sendRegistrationConfirmMail(Person user, Locale locale) throws MailException {
        String userName = "<b>" + user.getUsername() + "</b>";
        String password = "<b>" + user.getPassword() + "</b>";

        log.debug("Creating email content");
        String emailBody = "<html><body>";

        emailBody += "<h4>" + messageSource.getMessage("registration.email.body.hello",
                null, locale) + "</h4>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part1",
                null, locale) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part2",
                new String[]{userName}, locale) + "<br/>";

        emailBody += messageSource.getMessage("registration.email.body.text.part3",
                new String[]{password}, locale) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part4",
                null, locale) + "<br/>";


        emailBody +=
                "<a href=\"http://" + domain + "/registration-confirmation.html?activation=" + user.getAuthenticationHash() + "\">"
                        + "http://" + domain + "/registration-confirmation.html?activation=" + user.getAuthenticationHash() + ""
                        + "</a>"
                        + "</p>";

        emailBody += "</body></html>";

        log.debug("email body: " + emailBody);

        log.debug("Composing e-mail message");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        try {
            message.setFrom(mailMessage.getFrom());
            //  message.setContent("text/html");
            message.setTo(user.getEmail());
            //helper.setFrom(messageSource.getMessage("registration.email.from", null, RequestContextUtils.getLocale(request)));
            message.setSubject(messageSource.getMessage("registration.email.subject", null, locale));
            message.setText(emailBody, true);
        } catch (MessagingException e) {//rethrow as MailException to keep only 1 throw cause in method signature
            throw new MailPreparationException(e.getMessage(),e);
        }

        log.debug("Sending e-mail" + message);
        log.debug("mailSender" + mailSender);
        mailSender.send(mimeMessage);
        log.debug("E-mail was sent");
    }
}

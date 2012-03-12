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
        log.debug("Creating email content");
        StringBuilder sb = new StringBuilder();
        String login = "<b>" + user.getUsername() + "</b>";

        sb.append("<html><body>");
        sb.append("<h4>");
        sb.append(messageSource.getMessage("registration.email.welcome",null, locale));
        sb.append("</h4>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.yourLogin", new String[]{login}, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.clickToRegister",null, locale));
        sb.append("<br/>");
        sb.append("<a href=\"http://" + domain + "/registration-confirmation.html?activation=" + user.getAuthenticationHash() + "\">"
                + "http://" + domain + "/registration-confirmation.html?activation=" + user.getAuthenticationHash() + "</a>");
        sb.append("</p>");
        sb.append("</body></html>");

        String emailSubject = messageSource.getMessage("registration.email.subject", null, locale);
        sendEmail(user.getEmail(),emailSubject,sb.toString());
    }

    @Override
    public void sendRequestForJoiningGroupMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale) {
        sendGroupRoleEditMail(toEmail, requestId, userName, researchGroupTitle, locale, true);
    }

    @Override
    public void sendRequestForGroupRoleMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale) {
        sendGroupRoleEditMail(toEmail, requestId, userName, researchGroupTitle, locale, false);
    }

    public void sendGroupRoleEditMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale, boolean newMember) {
        log.debug("Creating email content");
        String userNameElement = "<b>" + userName + "</b>";
        String researchGroupTitleElement = "<b>" + researchGroupTitle + "</b>";

        //Email body is obtained from resource bunde. Url of domain is obtained from
        //configuration property file defined in persistence.xml
        //Locale is from request it ensures that user obtain localized email according to
        //his/her browser setting
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h4>");
        sb.append(messageSource.getMessage("editgrouprole.email.body.hello", null, locale));
        sb.append("</h4>");
        sb.append("<p>");
        sb.append(messageSource.getMessage(newMember ? "editgrouprole.email.body.userWantsToJoinGroup"
                : "editgrouprole.email.body.userWantsToChangeRole",
                new String[]{userNameElement, researchGroupTitleElement}, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("editgrouprole.email.body.clickToConfirm", null, locale));
        sb.append("<br/>");
        sb.append("<a href=\"http://" + domain + "/groups/accept-role-request.html?id=" + requestId + "\">" +
                "http://" + domain + "/groups/accept-role-request.html?id=" + requestId + "</a>");
        sb.append("</p>");
        sb.append("</body></html>");

        String subject = messageSource.getMessage(newMember ? "editgrouprole.email.subject.joinGroup"
                 : "editgrouprole.email.subject.changeRole", null, locale);
        sendEmail(toEmail, subject, sb.toString());
    }

    @Override
    public void sendEmail(String to, String subject, String emailBody) throws MailException {
        sendEmail(mailMessage.getFrom(), to, subject, emailBody);
    }

    protected void sendEmail(String from, String to, String subject, String emailBody) throws MailException {//make public if needed
        log.debug("email body: " + emailBody);
        log.debug("Composing e-mail message");
        MimeMessage mimeMessage = createMimeMessage(from, to,subject,emailBody);
        log.debug("MailSender " + mailSender + " sending email");
        mailSender.send(mimeMessage);
        log.debug("E-mail was sent");
    }

    protected MimeMessage createMimeMessage(String from, String to, String subject, String emailBody) throws MailException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(from);
            //  message.setContent("text/html");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(emailBody, true);
            log.debug("Message " + message);
            return mimeMessage;
        } catch (MessagingException e) {//rethrow as MailException
            throw new MailPreparationException(e.getMessage(),e);
        }
    }
}

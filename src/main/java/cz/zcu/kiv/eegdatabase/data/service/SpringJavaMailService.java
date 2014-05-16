/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SpringJavaMailService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.role.GroupRoleAcceptPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA. User: Jiri Novotny Date: 11.3.12 Time: 23:57
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
    public boolean sendRegistrationConfirmMail(Person user, Locale locale) throws MailException {
        log.debug("Creating email content");
        StringBuilder sb = new StringBuilder();
        String login = "<b>" + user.getUsername() + "</b>";

        sb.append("<html><body>");
        sb.append("<h4>");
        sb.append(messageSource.getMessage("registration.email.welcome", null, locale));
        sb.append("</h4>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.yourLogin", new String[] { login }, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.clickToRegister", null, locale));
        sb.append("<br/>");

        String confirmURL = PageParametersUtils.getUrlForPage(ConfirmPage.class, PageParametersUtils.getPageParameters(ConfirmPage.CONFIRM_ACTIVATION, user.getAuthenticationHash()));
        sb.append("<a href=\"" + confirmURL + "\">" + confirmURL + "</a>");
        sb.append("</p>");
        sb.append("</body></html>");

        String emailSubject = messageSource.getMessage("registration.email.subject", null, locale);
        return sendEmail(user.getUsername(), emailSubject, sb.toString());
    }

    @Override
    public boolean sendRequestForJoiningGroupMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale) {
        return sendGroupRoleEditMail(toEmail, requestId, userName, researchGroupTitle, locale, true);
    }

    @Override
    public boolean sendRequestForGroupRoleMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale) {
        return sendGroupRoleEditMail(toEmail, requestId, userName, researchGroupTitle, locale, false);
    }

    private boolean sendGroupRoleEditMail(String toEmail, int requestId, String userName, String researchGroupTitle, Locale locale, boolean newMember) {
        log.debug("Creating email content");
        String userNameElement = "<b>" + userName + "</b>";
        String researchGroupTitleElement = "<b>" + researchGroupTitle + "</b>";

        // Email body is obtained from resource bunde. Url of domain is obtained
        // from
        // configuration property file defined in persistence.xml
        // Locale is from request it ensures that user obtain localized email
        // according to
        // his/her browser setting
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h4>");
        sb.append(messageSource.getMessage("editgrouprole.email.body.hello", null, locale));
        sb.append("</h4>");
        sb.append("<p>");
        sb.append(messageSource.getMessage(newMember ? "editgrouprole.email.body.userWantsToJoinGroup"
                : "editgrouprole.email.body.userWantsToChangeRole",
                new String[] { userNameElement, researchGroupTitleElement }, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("editgrouprole.email.body.clickToConfirm", null, locale));
        sb.append("<br/>");
        String requestUrl = PageParametersUtils.getUrlForPage(GroupRoleAcceptPage.class, PageParametersUtils.getDefaultPageParameters(requestId));
        sb.append("<a href=\"" + requestUrl + "\">" + requestUrl + "</a>");
        sb.append("</p>");
        sb.append("</body></html>");

        String subject = messageSource.getMessage(newMember ? "editgrouprole.email.subject.joinGroup"
                : "editgrouprole.email.subject.changeRole", null, locale);
        return sendEmail(toEmail, subject, sb.toString());
    }

    @Override
    public boolean sendForgottenPasswordMail(String email, String plainPassword) {
        log.debug("Creating new mail object");
        SimpleMailMessage mail = new SimpleMailMessage(mailMessage);

        log.debug("Composing e-mail - TO: " + email);

        String subject = mail.getSubject() + " - Password reset";
        log.debug("Composing e-mail - SUBJECT: " + subject);
        mail.setSubject(subject);

        String text = "Your password for EEGbase portal was reset. Your new password (within brackets) is [" + plainPassword + "]\n\n" +
                "Please change the password after logging into system.";
        log.debug("Composing e-mail - TEXT: " + text);
        mail.setText(text);

        return sendEmail(email, mail.getSubject(), mail.getText());
    }

    @Override
    public boolean sendEmail(String to, String subject, String emailBody) throws MailException {
        return sendEmail(mailMessage.getFrom(), to, subject, emailBody);
    }

    private boolean sendEmail(String from, String to, String subject, String emailBody) throws MailException {// make
                                                                                                              // public
                                                                                                              // if
                                                                                                              // needed
        try {
            log.debug("email body: " + emailBody);
            log.debug("Composing e-mail message");
            MimeMessage mimeMessage = createMimeMessage(from, to, subject, emailBody);
            log.debug("MailSender " + mailSender + " sending email");
            mailSender.send(mimeMessage);
            log.debug("E-mail was sent");
            return true;
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            throw new MailSendException(e.getMessage(), e);
        }
    }

    protected MimeMessage createMimeMessage(String from, String to, String subject, String emailBody) throws MailException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(from);
            // message.setContent("text/html");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(emailBody, true);
            log.debug("Message " + message);
            return mimeMessage;
        } catch (MessagingException e) {// rethrow as MailException
            throw new MailPreparationException(e.getMessage(), e);
        }
    }

	@Override
	public boolean sendLicenseRequestToApplicantEmail(String toEmail, String licenseDescription) throws MailException {
		Locale locale = EEGDataBaseSession.get().getLocale();
		String subject = messageSource.getMessage("licenserequest.applicant.subject", null, locale);
		String msg = messageSource.getMessage("licenserequest.applicant.body",
				new String[]{ licenseDescription }, locale);
		return this.sendEmail(toEmail, subject, msg);
	}

	@Override
	public boolean sendLicenseRequestToGroupEmail(String toEmail, String applicantName, String applicantEmail, String licenseDescription) throws MailException {
		Locale locale = EEGDataBaseSession.get().getLocale();
		String subject = messageSource.getMessage("licenserequest.group.subject", null, locale);
		String msg = messageSource.getMessage("licenserequest.group.body",
				new String[]{applicantName, applicantEmail, licenseDescription}, locale);
		return this.sendEmail(toEmail, subject, msg);
	}

	@Override
	public boolean sendLicenseRequestConfirmationEmail(String toEmail, String licenseDescription) throws MailException {
		Locale locale = EEGDataBaseSession.get().getLocale();
		String subject = messageSource.getMessage("licenserequest.confirmation.confirmed.subject", null, locale);
		String msg = messageSource.getMessage("licenserequest.confirmation.confirmed.body",
				new String[]{licenseDescription}, locale);
		return this.sendEmail(toEmail, subject, msg);
	}
	
	@Override
	public boolean sendLicenseRequestRejectionEmail(String toEmail, String licenseDescription, String resolution) throws MailException {
		Locale locale = EEGDataBaseSession.get().getLocale();
		String subject = messageSource.getMessage("licenserequest.confirmation.rejected.subject", null, locale);
		String msg = messageSource.getMessage("licenserequest.confirmation.rejected.body",
				new String[]{licenseDescription, resolution}, locale);
		return this.sendEmail(toEmail, subject, msg);
	}
}

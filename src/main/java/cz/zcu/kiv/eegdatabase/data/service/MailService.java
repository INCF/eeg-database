package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.mail.MailException;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 12.3.12
 * Time: 2:42
 */
public interface MailService {

    /**
     * Sends email from the application with the defined content
     *
     * @param toEmail   email recipient address
     * @param subject   email subject
     * @param emailBody body text, can be HTML
     * @throws MailException when mail sending fails
     */
    void sendEmail(String toEmail, String subject, String emailBody) throws MailException;

    /**
     * Sends activation link to confirm user registration
     *
     * @param user newly persisted user entity, MUST have defined userName, authenticationHash
     * and email
     * @param locale browser locale
     * @throws MailException when mail sending fails
     */
    void sendRegistrationConfirmMail(Person user, Locale locale) throws MailException;

    /**
     * Sends request for changing group role
     *
     * @param toEmail            recipient email - group administrator
     * @param requestId          group permission request id
     * @param userName           username of the asking person
     * @param researchGroupTitle research group entity title
     * @param locale             user browser locale
     * @throws MailException when mail sending fails
     */
    void sendRequestForGroupRoleMail(String toEmail, int requestId, String userName,
                                     String researchGroupTitle, Locale locale) throws MailException;

    /**
     * Sends request for joining a group. Same as @link sendRequestForGroupRoleMail, only text is different
     *
     * @param toEmail            recipient email - group administrator
     * @param requestId          group permission request id
     * @param userName           username of the asking person
     * @param researchGroupTitle research group entity title
     * @param locale             user browser locale
     * @throws MailException when mail sending fails
     */
    void sendRequestForJoiningGroupMail(String toEmail, int requestId, String userName,
                                     String researchGroupTitle, Locale locale) throws MailException;
}

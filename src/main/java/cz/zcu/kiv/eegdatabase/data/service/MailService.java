package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.util.Locale;
import org.springframework.mail.MailException;

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
    boolean sendEmail(String toEmail, String subject, String emailBody) throws MailException;
    
	/**
     * Sends License request notification email to the Person who requests the license.
     *
     * @param toEmail User email that is requesting access for a license.
     * @throws MailException when mail sending fails
     */
	boolean sendLicenseRequestToApplicantEmail(String toEmail, String licenseDescription) throws MailException;
	
	/**
     * Sends License request notification email to the Group holding the license.
     *
     * @param toEmail Email the of group's owner that will be notified of the request.
     * @throws MailException when mail sending fails
     */
	boolean sendLicenseRequestToGroupEmail(String toEmail, String applicantName, String applicantEmail, String licenseDescription) throws MailException;
	
	/**
     * Sends notification email upon successful confirmation of a license request.
     *
     * @param toEmail Email the user whose license request has been approved.
     * @throws MailException when mail sending fails
     */
	boolean sendLicenseRequestConfirmationEmail(String toEmail, String licenseDescription) throws MailException;
	
	/**
     * Sends notification email upon denial of a license request.
     *
     * @param toEmail Email the user whose license request has been approved.
     * @throws MailException when mail sending fails
     */
	boolean sendLicenseRequestRejectionEmail(String toEmail, String licenseDescription, String resolution) throws MailException;

    /**
     * Sends activation link to confirm user registration
     *
     * @param user newly persisted user entity, MUST have defined userName, authenticationHash
     * and email
     * @param locale browser locale
     * @throws MailException when mail sending fails
     */
    boolean sendRegistrationConfirmMail(Person user, Locale locale) throws MailException;

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
    boolean sendRequestForGroupRoleMail(String toEmail, int requestId, String userName,
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
    boolean sendRequestForJoiningGroupMail(String toEmail, int requestId, String userName,
                                     String researchGroupTitle, Locale locale) throws MailException;
    
    boolean sendForgottenPasswordMail(String email, String plainPassword);
}

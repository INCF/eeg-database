package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author JiPER
 */
public class WriteRequestsListController extends AbstractController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private MailSender mailSender;
    private SimpleMailMessage mailMessage;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing request");
        ModelAndView mav = new ModelAndView("system/writeRequestsList");

        log.debug("Checking parameter 'grant'");
        String grantParameter = request.getParameter("grant");
        if (grantParameter != null) { // parameter "grant" was set
            log.debug("Parameter 'grant' was set");
            int personId = Integer.parseInt(grantParameter);  // parameter is set programatically, so it should be integer
            grantPermission(personId);
            mav.setViewName("system/permissionGranted");
        }

        log.debug("Checking parameter 'reject'");
        String rejectParameter = request.getParameter("reject");
        if (rejectParameter != null) { // parameter "reject" was set
            log.debug("Parameter 'reject' was set");
            int personId = Integer.parseInt(rejectParameter);  // parameter is set programatically, so it should be integer
            rejectPermission(personId);
            mav.setViewName("system/permissionRejected");
        }

        log.debug("Loading requests list");
        List<Person> requirements = personDao.getPersonsWherePendingRequirement();
        mav.addObject("personList", requirements);

        log.debug("Returning MAV");
        return mav;
    }

    /**
     * Grants permission for the person - sets propriate values to the database
     * and sends e-mail to the registered e-mail address
     * @param personId ID of the person for granting the permission
     */
    private void grantPermission(int personId) {
        log.debug("Loading Person object from database");
        Person user = personDao.read(personId);

        log.debug("Loaded Person - user name = " + user.getUsername());
        log.debug("Setting the authority to ROLE_EXPERIMENTER");
        user.setAuthority("ROLE_EXPERIMENTER");

        log.debug("Updating Person in database");
        personDao.update(user);

        String email = user.getEmail();
        sendEmail(email, true);
    }

    /**
     * Rejects permission for the person - sets propriate values to the database
     * and sends e-mail to the registered e-mail address
     * @param personId ID of the person for rejecting the permission
     */
    private void rejectPermission(int personId) {
        log.debug("Loading Person object from database");
        Person user = personDao.read(personId);

        log.debug("Loaded Person - user name = " + user.getUsername());
        log.debug("Setting the authority to ROLE_READER");
        user.setAuthority("ROLE_READER");

        log.debug("Updating Person in database");
        personDao.update(user);

        String email = user.getEmail();
        sendEmail(email, false);
    }

    /**
     * Sends e-mail with granting message or rejecting message to the recipient
     * @param recipient E-mail address of the recipient
     * @param grant If <code>true</code>, granting message will be sent,
     *              if <code>false</code>, rejecting message will be sent
     */
    private boolean sendEmail(String recipient, boolean grant) {
        log.debug("Sending e-mail");
        SimpleMailMessage mail = new SimpleMailMessage(mailMessage);
        mail.setTo(recipient);

        log.debug("Setting the parameters of the e-mail message");
        String subject = "";
        String text = "";
        if (grant) {
            subject = mail.getSubject() + " - Write permission granted";
            text = "Congratulation, the write permission for the EEGbase portal was granted. You can now submit your data. Thank you for your interest.";
        } else {
            subject = mail.getSubject() + " - Write permission rejected";
            text = "We are sorry, but the write permission for the EEGbase portal was rejected. You can apply again, try to better explain your reasons. Thank you.";
        }
        mail.setSubject(subject);
        mail.setText(text);

        try {
            log.debug("Sending the e-mail");
            mailSender.send(mail);
            log.debug("E-mail was sent");
            return true;
        } catch (MailException e) {
            log.debug("E-mail was NOT sent");
            return false;
        }
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}

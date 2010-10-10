package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ApplyForWritingPermissionCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public class ApplyForWritingPermissionController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private String requestNotNeededView;
    private String failView;
    private MailSender mailSender;
    private SimpleMailMessage mailMessage;

    public ApplyForWritingPermissionController() {
        setCommandClass(ApplyForWritingPermissionCommand.class);
        setCommandName("applyForWritingPermission");
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        log.debug("Processing showForm method");

        log.debug("Loading actual user from database");
        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());

        ModelAndView mav = super.showForm(request, response, errors);
        log.debug("Checking permission level");
        if (user.getAuthority().equals("ROLE_READER")) {
            log.debug("User ha ROLE_READER authority - show form for apply");
            mav.setViewName(getFormView());
        } else {
            log.debug("User has another authority than ROLE_READER - no need of request");
            mav.setViewName(getRequestNotNeededView());
        }

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing the form");
        ApplyForWritingPermissionCommand afwpc = (ApplyForWritingPermissionCommand) command;

        log.debug("Loading Person object of actual logged user from database");
        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());

        log.debug("Composing e-mail message");
        SimpleMailMessage mail = new SimpleMailMessage(mailMessage);
        mail.setFrom(user.getEmail());

        log.debug("Loading list of supervisors");
        List<Person> supervisors = personDao.getSupervisors();
        String[] emails = new String[supervisors.size()];
        int i = 0;
        for (Person supervisor : supervisors) {
            emails[i++] = supervisor.getEmail();
        }
        mail.setTo(emails);

        log.debug("Setting subject to e-mail message");
        mail.setSubject(mail.getSubject() + " - Write permission request from user " + user.getUsername());

        String messageBody = "User " + user.getUsername() + " has requested permission for adding data into EEGbase system.\n" +
                "Reason is: " + afwpc.getReason() + "\n" +
                "Use the address below to grant the write permission.\n";
        String linkAddress = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath() + "/system/grant-permission.html?id=" + user.getPersonId();
        log.debug("Address is: " + linkAddress);
        messageBody += linkAddress ;
        mail.setText(messageBody);

        String mavName = "";
        try {
            log.debug("Sending message");
            mailSender.send(mail);
            log.debug("Mail was sent");
            mavName = getSuccessView();
        } catch (MailException e) {
            log.debug("Mail was not sent");
            mavName = getFailView();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(mavName);
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public String getRequestNotNeededView() {
        return requestNotNeededView;
    }

    public void setRequestNotNeededView(String requestNotNeededView) {
        this.requestNotNeededView = requestNotNeededView;
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

    public String getFailView() {
        return failView;
    }

    public void setFailView(String failView) {
        this.failView = failView;
    }
}

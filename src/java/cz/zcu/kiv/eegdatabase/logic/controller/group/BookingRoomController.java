package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.GroupPermissionRequest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.BookRoomCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.support.RequestContextUtils;

public class BookingRoomController extends SimpleFormController {
  private Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;
  private ResearchGroupDao researchGroupDao;
  private GenericDao<GroupPermissionRequest,Integer> groupPermissionRequestDao;

  /**
   * Source of localized messages defined in persistence.xml
   */
  private HierarchicalMessageSource messageSource;
  /**
   * Domain from configuration file defined in persistence.xml
   */
  private String domain;

  private JavaMailSenderImpl mailSender;

  public BookingRoomController() {
    setCommandClass(BookRoomCommand.class);
    setCommandName("bookRoomCommand");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    BookRoomCommand bookRoomCommand = (BookRoomCommand) command;
/*
    Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
    GroupPermissionRequest gpr = new GroupPermissionRequest();
    gpr.setPerson(user);
    String requestRole = groupRoleCommand.getRole();
    gpr.setRequestedPermission(requestRole);
    gpr.setResearchGroup(researchGroupDao.read(groupRoleCommand.getEditedGroup()));
    groupPermissionRequestDao.create(gpr);
    
    int requestId = gpr.getRequestId();
    String userName ="<b>"+ user.getUsername() +"</b>";
    String researchGroup ="<b>"+ researchGroupDao.read(groupRoleCommand.getEditedGroup()).getTitle()+"</b>";



    String emailAdmin =researchGroupDao.read(groupRoleCommand.getEditedGroup()).getPerson().getEmail();
    
    log.debug("Creating email content");
    //Email body is obtained from resource bunde. Url of domain is obtained from
    //configuration property file defined in persistence.xml
    //Locale is from request it ensures that user obtain localized email according to
    //his/her browser setting
    String emailBody = "<html><body>";
    emailBody += "<h4>" + messageSource.getMessage("editgrouprole.email.body.hello",
            null,
            RequestContextUtils.getLocale(request)) +
            "</h4>";

    emailBody += "<p>" + messageSource.getMessage("editgrouprole.email.body.text.part1",
            new String[]{userName, researchGroup},
            RequestContextUtils.getLocale(request)) + "</p>";

    emailBody += "<p>" + messageSource.getMessage("editgrouprole.email.body.text.part2",
            null, RequestContextUtils.getLocale(request))+"<br/>";

    emailBody +=
            "<a href=\"http://" + domain + "/groups/accept-role-request.html?id=" + requestId + "\">" +
            "http://" + domain + "/groups/accept-role-request.html?id=" + requestId + "" +
            "</a>" +
            "</p>";

    emailBody += "</body></html>";

    log.debug("email body: " + emailBody);

    log.debug("Composing e-mail message");
    MimeMessage message = mailSender.createMimeMessage();
    //message.setContent(confirmLink, "text/html");
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(emailAdmin);
    helper.setFrom(messageSource.getMessage("editgrouprole.email.from",null, RequestContextUtils.getLocale(request)));
    helper.setSubject(messageSource.getMessage("editgrouprole.email.subject",null, RequestContextUtils.getLocale(request)));
    helper.setText(emailBody, true);
   
    try {
      log.debug("Sending e-mail");
      mailSender.send(message);
      log.debug("E-mail was sent");
    } catch (MailException e) {
      log.debug("E-mail was NOT sent");
      e.printStackTrace();
    }



     log.debug("Returning MAV");
    ModelAndView mav = new ModelAndView("groups/requestSent");
    return mav;
 */
    return null;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();
    List<ResearchGroup> researchGroupList =
            researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
    ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
    int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
    map.put("defaultGroupId",defaultGroupId);
    map.put("researchGroupList", researchGroupList);

    return map;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }


  public GenericDao<GroupPermissionRequest, Integer> getGroupPermissionRequestDao() {
    return groupPermissionRequestDao;
  }

  public void setGroupPermissionRequestDao(GenericDao<GroupPermissionRequest, Integer> groupPermissionRequestDao) {
    this.groupPermissionRequestDao = groupPermissionRequestDao;
  }


  public JavaMailSenderImpl getMailSender() {
    return mailSender;
  }

  public void setMailSender(JavaMailSenderImpl mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @param domain the domain to set
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return the messageSource
   */
  public HierarchicalMessageSource getMessageSource() {
    return messageSource;
  }

  /**
   * @param messageSource the messageSource to set
   */
  public void setMessageSource(HierarchicalMessageSource messageSource) {
    this.messageSource = messageSource;
  }


}

package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.GroupPermissionRequest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRoomController extends SimpleFormController {
    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ReservationDao reservationDao;
    private GenericDao<GroupPermissionRequest, Integer> groupPermissionRequestDao;

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
        String status = "fail";
        String comment = "Unknown error";

        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        String[] startTime = request.getParameter("startTime").split(":");
        String[] endTime = request.getParameter("endTime").split(":");
        String[] date = request.getParameter("date").split("/");
        int group = Integer.parseInt(request.getParameter("selectedGroup"));
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        int h = Integer.parseInt(startTime[0]);
        int m = Integer.parseInt(startTime[1]);


        

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + "?status=" + status + "&comment=" + comment);
        return mav;

    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        map.put("defaultGroupId", defaultGroupId);
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


    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }
}

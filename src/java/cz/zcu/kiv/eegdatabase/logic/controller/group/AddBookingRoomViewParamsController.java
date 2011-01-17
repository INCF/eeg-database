package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AddBookingRoomViewParamsController
        extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    /*private AuthorizationManager auth;
    /*private GenericDao<Experiment, Integer> experimentDao;
    private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;
    private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao;*/

    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;


    public AddBookingRoomViewParamsController() {
        setCommandClass(AddBookingRoomViewParamsCommand.class);
        setCommandName("addBookingRoomViewParams");
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        //mav.addObject("userIsExperimenter", auth.userIsExperimenter());
        return mav;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        log.debug("Preparing data for form");

        Map map = new HashMap<String, Object>();

        String[] startTime = request.getParameter("startTime").split(":");
        String[] endTime = request.getParameter("endTime").split(":");
        String[] date = request.getParameter("date").split("/");
        int group = Integer.parseInt(request.getParameter("group"));
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        int h = Integer.parseInt(startTime[0]);
        int m = Integer.parseInt(startTime[1]);

        GregorianCalendar cal = new GregorianCalendar(year, month - 1, day, 0, 0, 0);
        log.info("ORIG= " + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " 00:00:00");

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        GregorianCalendar weekStart = (GregorianCalendar) cal.clone();

        String startStr = weekStart.get(Calendar.DAY_OF_MONTH) + "-" + (weekStart.get(Calendar.MONTH) + 1) + "-" + weekStart.get(Calendar.YEAR) + " 00:00:00";
        log.info("START= " + startStr);

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        GregorianCalendar weekEnd = (GregorianCalendar) cal.clone();

        String endStr = weekEnd.get(Calendar.DAY_OF_MONTH) + "-" + (weekEnd.get(Calendar.MONTH) + 1) + "-" + weekEnd.get(Calendar.YEAR) + " 00:00:00";
        log.info("END= " + endStr);

        map.put("reservations", reservationDao.getReservationsBetween(weekStart, weekEnd));

        //if(repCount)


        map.put("timerange", request.getParameter("date") + "; " + request.getParameter("startTime") + " - " + request.getParameter("endTime"));


        map.put("check", "[group=" + group + " startTime=" + startStr + " endTime=" + endStr + "]");

        log.debug("Returning map object");
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {


        /*
        log.debug("Processing form data.");
        AddBookingRoomViewParamsCommand data = (AddBookingRoomViewParamsCommand) command;

        log.debug("Creating new object");
        ExperimentOptParamVal val = new ExperimentOptParamVal();
        val.setId(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
        val.setParamValue(data.getParamValue());

        log.debug("Saving object to database");
        experimentOptParamValDao.create(val);

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + data.getMeasurationFormId());
        return mav;         */


        AddBookingRoomViewParamsCommand data = (AddBookingRoomViewParamsCommand) command;


        ModelAndView mav = new ModelAndView(getSuccessView() + "?msg=ahojss&" + data.toString());
        return mav;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(AddBookingRoomViewParamsCommand.class);
    }
    /*
 public void validate(Object command, Errors errors) {
     AddBookingRoomViewParamsCommand data = (AddBookingRoomViewParamsCommand) command;

     if (!auth.userIsOwnerOrCoexperimenter(data.getMeasurationFormId())) {
         // First check whether the user has permission to add data
         errors.reject("error.mustBeOwnerOfExperimentOrCoexperimenter");
     } else {

         ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.field");

         if (data.getParamId() < 0) {
             errors.rejectValue("paramId", "required.field");
         }

         ExperimentOptParamVal val = experimentOptParamValDao.read(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
         if (val != null) {  // field already exists
             errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
         }

     }

 }

 public GenericDao<Experiment, Integer> getExperimentDao() {
     return experimentDao;
 }

 public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
     this.experimentDao = experimentDao;
 }

 public GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> getExperimentOptParamValDao() {
     return experimentOptParamValDao;
 }

 public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
     this.experimentOptParamValDao = experimentOptParamValDao;
 }

 public GenericDao<ExperimentOptParamDef, Integer> getExperimentOptParamDefDao() {
     return experimentOptParamDefDao;
 }

 public void setExperimentOptParamDefDao(GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao) {
     this.experimentOptParamDefDao = experimentOptParamDefDao;
 }

 public AuthorizationManager getAuth() {
     return auth;
 }

 public void setAuth(AuthorizationManager auth) {
     this.auth = auth;
 }   */


    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}

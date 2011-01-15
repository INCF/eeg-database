/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.logic.zip.Generator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Petr Jezek
 */
public class DownloadMetadataZipController extends SimpleFormController {

    private Generator zipGenerator;
    private Log log = LogFactory.getLog(getClass());
    private GenericDao<Experiment, Integer> experimentDao;
    private GenericDao<History, Integer> historyDao;
    private PersonDao personDao;

    public DownloadMetadataZipController() {
        setCommandClass(MetadataCommand.class);
        setCommandName("chooseMetadata");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        MetadataCommand mc = (MetadataCommand) super.formBackingObject(request);
        //mc.setChooseAll(true);
        //mc.setPerson(true);
        return mc;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
//      //ModelAndView mav = new ModelAndView("downloadMetadataZipView");
        MetadataCommand mc = (MetadataCommand) command;
        int id = Integer.parseInt(request.getParameter("id"));
        Experiment fromDB = experimentDao.read(id);
        String scenarioName = fromDB.getScenario().getTitle();
        Experiment meas = setChoosenMetadata(mc, experimentDao.read(id));

        Set<DataFile> files = fromDB.getDataFiles();
        //gets a parameters from request
        //contents is in the request if user wants download data file
        String[] contents = request.getParameterValues("content");
        //fileParam represents file metadata params
        String[] fileParam = request.getParameterValues("fileParam");
        FileMetadataParamValId[] params = null;
        if (fileParam != null) {
            params = new FileMetadataParamValId[fileParam.length];
            //go throws parameters and creates FileMetadataParamsValId instances
            //in the request data_file_id and file_metadata_param_def_id is separated
            //by # in the form file_id#file_metadata_param_def_id
            //
            for (int i = 0; i < fileParam.length; i++) {
                String[] tmp = fileParam[i].split("#");
                params[i] = new FileMetadataParamValId(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[0]));
            }
        }
        Set<DataFile> newFiles = null;
        if (fileParam != null || contents != null) {
            newFiles = new HashSet<DataFile>();
            //go over data files selected from db
            for (DataFile item : files) {
                DataFile newItem = null;
                if (contents != null) {
                    if (Arrays.asList(contents).contains(String.valueOf(item.getDataFileId()))) {
                        newItem = new DataFile();
                        newItem.setDataFileId(item.getDataFileId());
                        newItem.setExperiment(item.getExperiment());
                        newItem.setFileContent(item.getFileContent());
                        newItem.setFilename(item.getFilename());
                        newItem.setMimetype(item.getMimetype());
                        newItem.setSamplingRate(item.getSamplingRate());
                    }
                }
                Set<FileMetadataParamVal> newVals = new HashSet<FileMetadataParamVal>();
                if (params != null) {
                    for (FileMetadataParamVal paramVal : item.getFileMetadataParamVals()) {
                        for (FileMetadataParamValId paramId : params) {
                            if (paramVal.getId().getDataFileId() == paramId.getDataFileId() && paramVal.getId().getFileMetadataParamDefId() == paramId.getFileMetadataParamDefId()) {
                                newVals.add(paramVal);
                            }
                        }
                    }
                }
                if (!newVals.isEmpty()) {
                    if (newItem == null) {
                        newItem = new DataFile();
                    }
                    newItem.setFileMetadataParamVals(newVals);
                }
                if (newItem != null) {
                    newFiles.add(newItem);
                }
            }
        }
        meas.setDataFiles(newFiles);
        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        History history = new History();
        log.debug("Setting downloading metadata");
        history.setExperiment(fromDB);
        log.debug("Setting user");
        history.setPerson(user);
        log.debug("Setting time of download");
        history.setDateOfDownload(currentTimestamp);
        log.debug("Saving download history");
        historyDao.create(history);

        List<Experiment> meases = new ArrayList<Experiment>();
        meases.add(meas);
        OutputStream out = getZipGenerator().generate(meases, mc.isTitle());
        ByteArrayOutputStream bout = null;

        response.setHeader("Content-Type", "application/zip");
        if (scenarioName == null) {
            response.setHeader("Content-Disposition", "attachment;filename=Experiment_data.zip");
        } else {
            String[] names = scenarioName.split(" ");
            scenarioName = names[0];
            for (int i = 1; i < names.length; i++) {
                scenarioName += "_" + names[i];

            }
            response.setHeader("Content-Disposition", "attachment;filename=" + scenarioName + ".zip");
        }

        if (out instanceof ByteArrayOutputStream) {
            bout = (ByteArrayOutputStream) out;
            response.getOutputStream().write(bout.toByteArray());
        }
        // mav.addObject("dataObject", meas);
        log.debug(zipGenerator);
        return null;
//
    }

    protected Experiment setChoosenMetadata(MetadataCommand mc, Experiment fromDB) {
        Experiment meas = new Experiment();
        Person subject = new Person();
        meas.setPersonBySubjectPersonId(subject);
        Scenario scen = new Scenario();
        scen.setTitle(fromDB.getScenario().getTitle());
        meas.setScenario(scen);


        if (mc.isDescription()) {
            meas.getScenario().setDescription(fromDB.getScenario().getDescription());
        }
        if (mc.isLength()) {
            meas.getScenario().setScenarioLength(fromDB.getScenario().getScenarioLength());
        } else {
            meas.getScenario().setScenarioLength(Integer.MIN_VALUE);
        }
        if (mc.isScenFile()) {
            meas.getScenario().setScenarioXml(fromDB.getScenario().getScenarioXml());
        }

        Set<Person> persons = new HashSet<Person>();
        for (Person inDB : fromDB.getPersons()) {
            Person person = new Person();
            person.setDateOfBirth(inDB.getDateOfBirth());
            person.setEmail(inDB.getEmail());
            person.setGender(inDB.getGender());
            person.setGivenname(inDB.getGivenname());
            person.setHearingImpairments(inDB.getHearingImpairments());
            person.setNote(inDB.getNote());
            person.setPhoneNumber(inDB.getPhoneNumber());
            person.setSurname(inDB.getSurname());
            person.setVisualImpairments(inDB.getVisualImpairments());
            person.setPersonOptParamVals(inDB.getPersonOptParamVals());
            persons.add(person);
        }
        if (mc.isName()) {
            meas.getPersonBySubjectPersonId().setSurname(fromDB.getPersonBySubjectPersonId().getSurname());
            meas.getPersonBySubjectPersonId().setGivenname(fromDB.getPersonBySubjectPersonId().getGivenname());

        } else {
            for (Person per : persons) {
                per.setSurname(null);
                per.setGivenname(null);
            }
        }
        if (mc.isBirth()) {
            meas.getPersonBySubjectPersonId().setDateOfBirth(fromDB.getPersonBySubjectPersonId().getDateOfBirth());

        } else {
            for (Person per : persons) {
                per.setDateOfBirth(null);
            }
        }
        if (mc.isEmail()) {
            meas.getPersonBySubjectPersonId().setEmail(fromDB.getPersonBySubjectPersonId().getEmail());

        } else {
            for (Person person : persons) {
                person.setEmail(null);
            }
        }
        if (mc.isGender()) {
            meas.getPersonBySubjectPersonId().setGender(fromDB.getPersonBySubjectPersonId().getGender());
        } else {
            meas.getPersonBySubjectPersonId().setGender(' ');
            for (Person person : persons) {
                person.setGender(' ');
            }
        }
        if (mc.isPhoneNumber()) {
            meas.getPersonBySubjectPersonId().setPhoneNumber(fromDB.getPersonBySubjectPersonId().getPhoneNumber());
        } else {
            for (Person person : persons) {
                person.setPhoneNumber(null);
            }
        }
        if (mc.isNote()) {
            meas.getPersonBySubjectPersonId().setNote(fromDB.getPersonBySubjectPersonId().getNote());

        } else {
            for (Person person : persons) {
                person.setNote(null);
            }
        }
        if (mc.isHearingDefects()) {
            meas.getPersonBySubjectPersonId().setHearingImpairments(fromDB.getPersonBySubjectPersonId().getHearingImpairments());

        } else {
            for (Person person : persons) {
                person.setHearingImpairments(null);
            }
        }
        if (mc.isEyesDefects()) {
            meas.getPersonBySubjectPersonId().setVisualImpairments(fromDB.getPersonBySubjectPersonId().getVisualImpairments());

        } else {
            for (Person person : persons) {
                person.setVisualImpairments(null);
            }
        }
        if (mc.isPersonAddParams()) {
            meas.getPersonBySubjectPersonId().setPersonOptParamVals(fromDB.getPersonBySubjectPersonId().getPersonOptParamVals());

        } else {
            for (Person person : persons) {
                person.setPersonOptParamVals(null);
            }
        }
        meas.setPersons(persons);

        if (!mc.isMeasuration()) {
            if (mc.isTimes()) {
                meas.setEndTime(fromDB.getEndTime());
                meas.setStartTime(fromDB.getStartTime());
            }
            if (mc.isTemperature()) {
                meas.setTemperature(meas.getTemperature());
            } else {
                meas.setTemperature(Integer.MIN_VALUE);
            }
            if (mc.isWeather()) {
                meas.setWeather(fromDB.getWeather());
            }
            if (mc.isWeatherNote()) {
                meas.setWeathernote(fromDB.getWeathernote());
            }
            if (mc.isHardware()) {
                meas.setHardwares(fromDB.getHardwares());
            }

            if (mc.isMeasurationAddParams()) {
                meas.setExperimentOptParamVals(fromDB.getExperimentOptParamVals());
            }
        }
        return meas;
    }

    /**
     * @return the zipGenerator
     */
    public Generator getZipGenerator() {
        return zipGenerator;
    }

    /**
     * @param zipGenerator the zipGenerator to set
     */
    public void setZipGenerator(Generator generator) {
        this.zipGenerator = generator;
    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public GenericDao<History, Integer> getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(GenericDao<History, Integer> historyDao) {
        this.historyDao = historyDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}

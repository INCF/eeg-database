/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.xml;

import java.io.ByteArrayOutputStream;

import cz.zcu.kiv.eegdatabase.data.pojo.*;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.ScenarioType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jan Štěbeták
 */
public class XMLTransformer implements DataTransformer {

  private String encoding;
  private final String XSDSchema = "experiment.xsd";
  private String objects;
  private JAXBContext jc;
  private ObjectFactory of;
  private String measured;
  private String experimenter;
  private Log log = LogFactory.getLog(getClass());

  @Override
  public OutputStream transform(Experiment meas, boolean scenarioName) throws JAXBException, IOException {

    if (meas == null) {
      return null;
    }
    jc = JAXBContext.newInstance(objects);
    of = new ObjectFactory();
    log.debug("Creating JAXB context");
    MeasurationType measType = of.createMeasurationType();
    XMLMeasuration mea = new XMLMeasuration(measType);
    if ((meas.getEndTime() != null) && (meas.getStartTime() != null)) {
      mea.writeStartAndEndTime(meas.getEndTime().toString(),
              meas.getStartTime().toString());
      log.debug("Written start and end time: " + measType.getStartTime() + ", "
              + measType.getEndTime());
    }

    Scenario scenario = meas.getScenario();
    if (scenario != null) {
      ScenarioType scType = of.createScenarioType();
      XMLScenario scen = new XMLScenario(scType);
      if (scenarioName) {
        scen.writeTitle(scenario.getTitle());
      }
      scen.writeLength("" + scenario.getScenarioLength());
      scen.writeDescription(scenario.getDescription());
      measType.setScenario(scType);
      log.debug("Written Scenario metadata: " + scType);
    }
    if (meas.getTemperature() != Integer.MIN_VALUE) {
      measType.setTemperature(meas.getTemperature());
    }
    if (meas.getHardwares() != null) {
      List<HardwareType> hwType = measType.getHardware();
      for (Hardware hw : meas.getHardwares()) {
        hwType.add(mea.writeHardware(hw, of));
        log.debug("Written hardware: " + hw);
      }
    }
    if (meas.getExperimentOptParamVals() != null) {
      List<MeasurationAddParam> param = measType.getAddParam();
      for (ExperimentOptParamVal measAddParam : meas.getExperimentOptParamVals()) {
        param.add(mea.writeAdditionalParams(measAddParam, of));
        log.debug("Written measured additional params: " + measAddParam);
      }
    }

    if (meas.getWeather() != null) {
      WeatherType wType = of.createWeatherType();
      wType.setTitle(meas.getWeather().getTitle());
      wType.setDescription(meas.getWeather().getDescription());
      measType.setWeather(wType);
      log.debug("Written weather: " + wType);
    }
    measType.setWeatherNote(meas.getWeathernote());

    List<PersonType> perType = measType.getPerson();
    writePerson(perType, meas.getPersonBySubjectPersonId(), measured);
    for (Person person : meas.getPersons()) {
      writePerson(perType, person, experimenter);
      log.debug("Written Person metadata: " + person);
    }

    List<DataType> dataType = measType.getData();
    if (meas.getDataFiles() != null) {
      for (DataFile data : meas.getDataFiles()) {
        log.debug("creating data into output xml: " + data.getFilename());
        DataType datat = of.createDataType();
        datat.setFileName(data.getFilename());
        if (data.getSamplingRate() > 0) {
          datat.setSamplingRate((float) data.getSamplingRate());
        }
        log.debug("Written sampling rate: " + datat.getSamplingRate());
        if (data.getFileMetadataParamVals() != null) {
          List<FileMetadataType> metType = datat.getFileMetadata();
          for (FileMetadataParamVal fileMetadata : data.getFileMetadataParamVals()) {
            metType.add(mea.writeFileMetadata(fileMetadata, of));
            log.debug("Written sampling rate and file metadata: " + fileMetadata);
          }
        }
        dataType.add(datat);
      }
    }

    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_ENCODING, encoding);
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, XSDSchema);
    JAXBElement<MeasurationType> me = of.createMeasuration(measType);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(me, baos);
    log.debug("Written XML document into a ByteArrayOutputStream ");
    return baos;
  }

  protected void writePerson(List<PersonType> perType, Person per,
          String position) {
    if (per == null) {
      return;
    }
    PersonType pert = of.createPersonType();
    XMLPerson p = new XMLPerson(pert);
    p.writeName(per.getGivenname(), per.getSurname());
    if (per.getDateOfBirth() != null) {
      pert.setDateOfBirth(per.getDateOfBirth().toString());
    }
    pert.setEmail(per.getEmail());
    if (per.getGender() != ' ') {
      pert.setGender("" + per.getGender());
    }
    pert.setNote(per.getNote());
    pert.setPhoneNumber(per.getPhoneNumber());
    pert.setPosition(position);
    log.debug("Written person simple attributes: " + pert);
    if (per.getVisualImpairments() != null) {
      List<EyesDefectType> edefType = pert.getEyesDefect();
      for (VisualImpairment eyesDefect : per.getVisualImpairments()) {
        edefType.add(p.writeEyesDefects(eyesDefect, of));
        log.debug("Written eyes defect: " + eyesDefect);
      }
    }
    if (per.getHearingImpairments() != null) {
      List<HearingDefectType> hdefType = pert.getHearingDefect();
      for (HearingImpairment hearingDefect : per.getHearingImpairments()) {
        hdefType.add(p.writeHearingDefects(hearingDefect, of));
        log.debug("Written hearing defect: " + hearingDefect);
      }
    }
    if (per.getPersonOptParamVals() != null) {
      List<PersonAddParam> param = pert.getAddParam();
      for (PersonOptParamVal personAddParam : per.getPersonOptParamVals()) {
        param.add(p.writeAdditionalParams(personAddParam, of));
        log.debug("Written person additional params: " + personAddParam);
      }
    }

    perType.add(pert);
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getExperimenter() {
    return experimenter;
  }

  public void setExperimenter(String experimenter) {
    this.experimenter = experimenter;
  }

  public String getMeasured() {
    return measured;
  }

  public void setMeasured(String measured) {
    this.measured = measured;
  }

  public String getObjects() {
    return objects;
  }

  public void setObjects(String objects) {
    this.objects = objects;
  }
}

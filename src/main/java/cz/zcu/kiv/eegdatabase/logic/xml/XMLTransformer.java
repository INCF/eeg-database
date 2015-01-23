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
 *   XMLTransformer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.*;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jan Štěbeták
 */
public class XMLTransformer implements DataTransformer {

  private String encoding;
  private final String XSDSchema = "src/main/resources/experiment.xsd";
  private String objects;
  private JAXBContext jc;
  private ObjectFactory of;
  private String measured;
  private String experimenter;
  private Log log = LogFactory.getLog(getClass());

  public OutputStream transform(Experiment meas, MetadataCommand mc, Set<DataFile> datas) throws JAXBException, IOException {

    if (meas == null) {
      return null;
    }
    jc = JAXBContext.newInstance(objects);
    of = new ObjectFactory();
    log.debug("Creating JAXB context");
    MeasurationType measType = of.createMeasurationType();
    XMLMeasuration mea = new XMLMeasuration(measType);
    if (mc.isTimes()) {
      mea.writeStartAndEndTime(meas.getStartTime().toString(),
              meas.getEndTime().toString());
      log.debug("Written start and end time: " + measType.getStartTime() + ", "
              + measType.getEndTime());
    }
    Scenario scenario = meas.getScenario();
      ScenarioType scType = of.createScenarioType();
      XMLScenario scen = new XMLScenario(scType);
      if (mc.isTitle()) {
        scen.writeTitle(scenario.getTitle());
      }
      if (mc.isLength() && scenario.getScenarioLength() >= 0) {
      scen.writeLength("" + scenario.getScenarioLength() + ResourceUtils.getString("valueTable.scenarioLength.minutes"));
      }
      if (mc.isDescription()) {
      scen.writeDescription(scenario.getDescription());
      }
      measType.setScenario(scType);
      log.debug("Written Scenario metadata: " + scType);
    if (mc.isTemperature()) {
      measType.setTemperature(meas.getTemperature());
    }
    if (mc.isHardware()) {
      List<HardwareType> hwType = measType.getHardware();
      for (Hardware hw : meas.getHardwares()) {
        hwType.add(mea.writeHardware(hw, of));
        log.debug("Written hardware: " + hw);
      }
    }
    if (mc.isMeasurationAddParams()) {
      List<MeasurationAddParam> param = measType.getAddParam();
      for (ExperimentOptParamVal measAddParam : meas.getExperimentOptParamVals()) {
        param.add(mea.writeAdditionalParams(measAddParam, of));
        log.debug("Written measured additional params: " + measAddParam);
      }
    }

    if (mc.isWeather()) {
      WeatherType wType = of.createWeatherType();
      wType.setTitle(meas.getWeather().getTitle());
      wType.setDescription(meas.getWeather().getDescription());
      measType.setWeather(wType);
      log.debug("Written weather: " + wType);
    }
    if (mc.isWeatherNote()) {
    measType.setEnvironmentNote(meas.getEnvironmentNote());
    }
    List<PersonType> perType = measType.getPerson();
    writePerson(perType, meas.getPersonBySubjectPersonId(), measured, mc, meas.getStartTime());
    for (Person person : meas.getPersons()) {
      writePerson(perType, person, experimenter, mc, null);
      log.debug("Written Person metadata: " + person);
    }

    List<DataType> dataType = measType.getData();
    if (meas.getDataFiles() != null) {
      for (DataFile data : datas) {
        log.debug("creating data into output xml: " + data.getFilename());
        DataType datat = of.createDataType();
        datat.setFileName(data.getFilename());
        if (mc.isSamplingRate()) {
            datat.setDescription(data.getDescription());
        }
        log.debug("Written file description: " + datat.getDescription());
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
          String position, MetadataCommand mc, Timestamp scenarioStartTime) {
    if (per == null) {
      return;
    }
    PersonType pert = of.createPersonType();
    XMLPerson p = new XMLPerson(pert);
    if (mc.isName()) {
    p.writeName(per.getGivenname(), per.getSurname());
    }
    if (mc.isBirth()) {
        if (per.getDateOfBirth() != null && scenarioStartTime != null) {
            
            Calendar toTime = Calendar.getInstance();
            toTime.setTimeInMillis(scenarioStartTime.getTime());
            
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTimeInMillis(per.getDateOfBirth().getTime());
            
            int years = toTime.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
            int currMonth = toTime.get(Calendar.MONTH) + 1;
            int birthMonth = birthDate.get(Calendar.MONTH) + 1;
            int months = currMonth - birthMonth;
            
            if (months < 0)
               years--;
            
            pert.setAge(Integer.toString(years));
        }

    }
    if (mc.isEmail()) {
    pert.setEmail(per.getUsername());
    }
    if (mc.isGender()) {
      pert.setGender("" + per.getGender());
    }
    if (mc.isNote()) {
    pert.setNote(per.getNote());
    }
    if (mc.isPhoneNumber()) {
    pert.setPhoneNumber(per.getPhoneNumber());
    }
    pert.setPosition(position);
    log.debug("Written person simple attributes: " + pert);
//    if (mc.isEyesDefects()) {
//      List<EyesDefectType> edefType = pert.getEyesDefect();
//      for (VisualImpairment eyesDefect : per.getVisualImpairments()) {
//        edefType.add(p.writeEyesDefects(eyesDefect, of));
//        log.debug("Written eyes defect: " + eyesDefect);
//      }
//    }
//    if (mc.isHearingDefects()) {
//      List<HearingDefectType> hdefType = pert.getHearingDefect();
//      for (HearingImpairment hearingDefect : per.getHearingImpairments()) {
//        hdefType.add(p.writeHearingDefects(hearingDefect, of));
//        log.debug("Written hearing defect: " + hearingDefect);
//      }
//    }
    if (mc.isPersonAddParams()) {
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

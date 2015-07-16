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
 *   ExperimentToODMLMapper.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.sql.Timestamp;

import odml.core.Property;
import odml.core.Section;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ArtifactRemoveMethod;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.components.utils.DateUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;

public class ExperimentToODMLMapper {

    protected static Log log = LogFactory.getLog(ExperimentToODMLMapper.class);

    public static Section convertExperimentToSection(Experiment exp) {

        Section root = new Section();
        Section experiment = null;
        try {

            experiment = new Section();
            experiment.setName("Experiment");
            experiment.setType("experiment");
            root.add(experiment);

            Property prop = new Property("private-experiment", exp.isPrivateExperiment());
            experiment.add(prop);

            Property prop1 = new Property("start-time", exp.getStartTime());
            experiment.add(prop1);

            Property prop4 = new Property("end-time", exp.getEndTime());
            experiment.add(prop4);

            Property prop2 = new Property("temperature", exp.getTemperature());
            experiment.add(prop2);

            Property prop3 = new Property("environment-note", exp.getEnvironmentNote());
            experiment.add(prop3);

            if (exp.getResearchGroup() != null) {
                Property prop5 = new Property("research-group", exp.getResearchGroup().getTitle());
                experiment.add(prop5);
            }

            if (exp.getScenario() != null) {
                Property prop6 = new Property("scenario-title", exp.getScenario().getTitle());
                experiment.add(prop6);
            }

            if (exp.getPersonBySubjectPersonId() != null) {
                root.add(convertPersonToSection(exp.getPersonBySubjectPersonId(), true, exp.getStartTime()));
            }

            if (exp.getWeather() != null) {
                root.add(convertWeatherToSection(exp.getWeather()));
            }

            if (exp.getArtifact() != null) {
                root.add(convertArtifactToSection(exp.getArtifact()));
            }

            if (exp.getDigitization() != null) {
                root.add(convertDigitizationToSection(exp.getDigitization()));
            }

            if (exp.getHardwares().size() != 0) {

                Section hardwares = new Section();
                hardwares.setName("Hardwares");
                hardwares.setType("collection");
                root.add(hardwares);

                for (Hardware hw : exp.getHardwares()) {
                    hardwares.add(convertHardwareToSection(hw));
                }

            }

            if (exp.getSoftwares() != null && exp.getSoftwares().size() != 0) {

                Section softwares = new Section();
                softwares.setName("Softwares");
                softwares.setType("collection");
                root.add(softwares);

                for (Software sw : exp.getSoftwares()) {
                    softwares.add(convertSoftwareToSection(sw));
                }

            }

            if (exp.getPharmaceuticals().size() != 0) {

                Section pharmaceuticals = new Section();
                pharmaceuticals.setName("Pharmaceuticals");
                pharmaceuticals.setType("collection");
                root.add(pharmaceuticals);

                for (Pharmaceutical ph : exp.getPharmaceuticals()) {
                    pharmaceuticals.add(convertPharmaceuticalToSection(ph));
                }

            }

            if (exp.getDiseases().size() != 0) {

                Section diseases = new Section();
                diseases.setName("Diseases");
                diseases.setType("collection");
                root.add(diseases);

                for (Disease tmp : exp.getDiseases()) {
                    diseases.add(convertDiseaseToSection(tmp));
                }

            }

            if (exp.getProjectTypes().size() != 0) {

                Section projectTypes = new Section();
                projectTypes.setName("ProjectTypes");
                projectTypes.setType("collection");
                root.add(projectTypes);

                for (ProjectType tmp : exp.getProjectTypes()) {
                    projectTypes.add(convertProjectTypeToSection(tmp));
                }

            }

            if (exp.getArtifactRemoveMethods().size() != 0) {

                Section artifactRemoveMethod = new Section();
                artifactRemoveMethod.setName("ArtifactRemoveMethods");
                artifactRemoveMethod.setType("collection");
                root.add(artifactRemoveMethod);

                for (ArtifactRemoveMethod tmp : exp.getArtifactRemoveMethods()) {
                    artifactRemoveMethod.add(convertArtifactRemoveMethodToSection(tmp));
                }

            }

            if (exp.getExperimentOptParamVals().size() != 0) {

                Section experimentOptParameters = new Section();
                experimentOptParameters.setName("ExperimentOptParameters");
                experimentOptParameters.setType("collection");
                root.add(experimentOptParameters);

                for (ExperimentOptParamVal tmp : exp.getExperimentOptParamVals()) {
                    experimentOptParameters.add(convertExperimentOptParamValToSection(tmp));
                }

            }

            if (exp.getPersons().size() != 0) {

                Section experimentators = new Section();
                experimentators.setName("Experimentators");
                experimentators.setType("collection");
                root.add(experimentators);

                for (Person tmp : exp.getPersons()) {
                    experimentators.add(convertPersonToSection(tmp, false, exp.getStartTime()));
                }

            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return root;
    }

    public static Section convertHardwareToSection(Hardware hw) {

        Section hardware = new Section();
        hardware.setType("hardware");
        hardware.setName("Hardware");

        try {

            Property prop1 = new Property("title", hw.getTitle());
            hardware.add(prop1);

            Property prop2 = new Property("type", hw.getType());
            hardware.add(prop2);

            Property prop3 = new Property("description", hw.getDescription());
            hardware.add(prop3);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return hardware;
    }

    public static Section convertSoftwareToSection(Software sw) {

        Section software = new Section();
        software.setType("software");
        software.setName("Software");

        try {

            Property prop1 = new Property("title", sw.getTitle());
            software.add(prop1);

            Property prop2 = new Property("description", sw.getDescription());
            software.add(prop2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return software;
    }

    public static Section convertPharmaceuticalToSection(Pharmaceutical ph) {

        Section pharmaceutical = new Section();
        pharmaceutical.setType("pharmaceutical");
        pharmaceutical.setName("Pharmaceutical");

        try {

            Property prop1 = new Property("title", ph.getTitle());
            pharmaceutical.add(prop1);

            Property prop2 = new Property("description", ph.getDescription());
            pharmaceutical.add(prop2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return pharmaceutical;
    }

    public static Section convertDiseaseToSection(Disease ds) {

        Section disease = new Section();
        disease.setType("disease");
        disease.setName("Disease");

        try {

            Property prop1 = new Property("title", ds.getTitle());
            disease.add(prop1);

            Property prop2 = new Property("description", ds.getDescription());
            disease.add(prop2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return disease;
    }

    public static Section convertDigitizationToSection(Digitization dg) {

        Section digitization = new Section();
        digitization.setType("digitization");
        digitization.setName("Digitization");

        try {

            Property prop1 = new Property("filter", dg.getFilter());
            digitization.add(prop1);

            Property prop2 = new Property("gain", dg.getGain());
            digitization.add(prop2);

            Property prop3 = new Property("sampling-rate", dg.getSamplingRate());
            digitization.add(prop3);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return digitization;
    }

    public static Section convertArtifactToSection(Artifact ar) {

        Section artifact = new Section();
        artifact.setType("artifact");
        artifact.setName("Artifact");

        try {

            Property prop1 = new Property("compensation", ar.getCompensation());
            artifact.add(prop1);

            Property prop2 = new Property("reject-condition", ar.getRejectCondition());
            artifact.add(prop2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return artifact;
    }

    public static Section convertArtifactRemoveMethodToSection(ArtifactRemoveMethod arm) {

        Section artifactRemoveMethod = new Section();
        artifactRemoveMethod.setType("artifactRemoveMethod");
        artifactRemoveMethod.setName("ArtifactRemoveMethod");

        try {

            Property prop1 = new Property("title", arm.getTitle());
            artifactRemoveMethod.add(prop1);

            Property prop2 = new Property("description", arm.getDescription());
            artifactRemoveMethod.add(prop2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return artifactRemoveMethod;
    }

    public static Section convertWeatherToSection(Weather wh) {

        Section weather = new Section();
        weather.setType("weather");
        weather.setName("Weather");

        try {

            Property prop1 = new Property("title", wh.getTitle());
            weather.add(prop1);

            Property prop3 = new Property("description", wh.getDescription());
            weather.add(prop3);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return weather;
    }

    public static Section convertProjectTypeToSection(ProjectType pt) {

        Section projectType = new Section();
        projectType.setType("projectType");
        projectType.setName("ProjectType");

        try {

            Property prop1 = new Property("title", pt.getTitle());
            projectType.add(prop1);

            Property prop3 = new Property("description", pt.getDescription());
            projectType.add(prop3);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return projectType;
    }

    public static Section convertExperimentOptParamValToSection(ExperimentOptParamVal eopv) {

        Section experimentOptParam = new Section();
        experimentOptParam.setType("experimentOptParam");
        experimentOptParam.setName("ExperimentOptParam");

        try {

            Property prop1 = new Property("param-name", eopv.getExperimentOptParamDef().getParamName());
            experimentOptParam.add(prop1);

            Property prop2 = new Property("param-type", eopv.getExperimentOptParamDef().getParamDataType());
            experimentOptParam.add(prop2);

            Property prop3 = new Property("param-value", eopv.getParamValue());
            experimentOptParam.add(prop3);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return experimentOptParam;
    }

    public static Section convertPersonToSection(Person pr, boolean subject, Timestamp scenarioTime) {

        Section person = new Section();
        person.setType("person");

        if (subject) {
            person.setName("Subject");

        } else {
            person.setName("Person");
        }

        try {

            if (subject) {
                
                Property prop2 = new Property("gender", pr.getGender());
                person.add(prop2);
                
                Property prop5 = new Property("age", DateUtils.getPersonAge(pr, scenarioTime));
                person.add(prop5);
            
            } else {

                Property prop1 = new Property("username", pr.getUsername());
                person.add(prop1);

                Property prop3 = new Property("surname", pr.getSurname());
                person.add(prop3);
                
                Property prop4 = new Property("givenname", pr.getGivenname());
                person.add(prop4);

                Property prop2 = new Property("gender", pr.getGender());
                person.add(prop2);

                Property prop6 = new Property("role", UserRole.valueOf(pr.getAuthority()).name());
                person.add(prop6);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return person;
    }

}

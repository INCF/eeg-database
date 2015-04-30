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
 *   ExperimentMetadata.java, 2014/02/04 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.datadownload.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

import java.util.List;

/**
 * Created by Honza on 2.4.15.
 */
public class ExperimentMetadata {

    private ExperimentInfo experimentInfo;
    private PersonInfo subjectPerson;
    private SubjectGroupInfo subjectGroupInfo;
    private ArtifactInfo artifactInfo;
    private ResearchGroupInfo researchGroupInfo;

    private List<PersonInfo> coexperimenters;
    private WeatherInfo weatherInfo;
    private DigitizationInfo digitizationInfo;
    private ScenarioInfo scenarioInfo;
    private List<HardwareInfo> hardwareInfos;
    private List<DiseaseInfo> diseaseInfos;
    private List<PharmaceuticalInfo> pharmaceuticalInfos;
    private List<SoftwareInfo> softwareInfos;
    private List<ProjectTypeInfo> projectTypeInfos;
    private List<ArtifactRemoveMethodInfo> artifactRemoveMethodInfos;
    private List<DataFileInfo> dataFileInfos;

    public ExperimentInfo getExperimentInfo() {
        return experimentInfo;
    }

    public void setExperimentInfo(ExperimentInfo info) {
        this.experimentInfo = info;
    }

    public List<HardwareInfo> getHardwareInfos() {
        return hardwareInfos;
    }

    public void setHardwareInfos(List<HardwareInfo> hardwareInfos) {
        this.hardwareInfos = hardwareInfos;
    }

    public PersonInfo getSubjectPerson() {
        return subjectPerson;
    }

    public void setSubjectPerson(PersonInfo subjectPerson) {
        this.subjectPerson = subjectPerson;
    }

    public SubjectGroupInfo getSubjectGroupInfo() {
        return subjectGroupInfo;
    }

    public void setSubjectGroupInfo(SubjectGroupInfo subjectGroupInfo) {
        this.subjectGroupInfo = subjectGroupInfo;
    }

    public ArtifactInfo getArtifactInfo() {
        return artifactInfo;
    }

    public void setArtifactInfo(ArtifactInfo artifactInfo) {
        this.artifactInfo = artifactInfo;
    }

    public ResearchGroupInfo getResearchGroupInfo() {
        return researchGroupInfo;
    }

    public void setResearchGroupInfo(ResearchGroupInfo researchGroupInfo) {
        this.researchGroupInfo = researchGroupInfo;
    }

    public List<PersonInfo> getCoexperimenters() {
        return coexperimenters;
    }

    public void setCoexperimenters(List<PersonInfo> coexperimenters) {
        this.coexperimenters = coexperimenters;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public DigitizationInfo getDigitizationInfo() {
        return digitizationInfo;
    }

    public void setDigitizationInfo(DigitizationInfo digitizationInfo) {
        this.digitizationInfo = digitizationInfo;
    }

    public ScenarioInfo getScenarioInfo() {
        return scenarioInfo;
    }

    public void setScenarioInfo(ScenarioInfo scenarioInfo) {
        this.scenarioInfo = scenarioInfo;
    }

    public List<DiseaseInfo> getDiseaseInfos() {
        return diseaseInfos;
    }

    public void setDiseaseInfos(List<DiseaseInfo> diseaseInfos) {
        this.diseaseInfos = diseaseInfos;
    }

    public List<PharmaceuticalInfo> getPharmaceuticalInfos() {
        return pharmaceuticalInfos;
    }

    public void setPharmaceuticalInfos(List<PharmaceuticalInfo> pharmaceuticalInfos) {
        this.pharmaceuticalInfos = pharmaceuticalInfos;
    }

    public List<SoftwareInfo> getSoftwareInfos() {
        return softwareInfos;
    }

    public void setSoftwareInfos(List<SoftwareInfo> softwareInfos) {
        this.softwareInfos = softwareInfos;
    }

    public List<ProjectTypeInfo> getProjectTypeInfos() {
        return projectTypeInfos;
    }

    public void setProjectTypeInfos(List<ProjectTypeInfo> projectTypeInfos) {
        this.projectTypeInfos = projectTypeInfos;
    }

    public List<ArtifactRemoveMethodInfo> getArtifactRemoveMethodInfos() {
        return artifactRemoveMethodInfos;
    }

    public void setArtifactRemoveMethodInfos(List<ArtifactRemoveMethodInfo> artifactRemoveMethodInfos) {
        this.artifactRemoveMethodInfos = artifactRemoveMethodInfos;
    }

    public List<DataFileInfo> getDataFileInfos() {
        return dataFileInfos;
    }

    public void setDataFileInfos(List<DataFileInfo> dataFileInfos) {
        this.dataFileInfos = dataFileInfos;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

/**
 * @author Jan Štěbeták
 */
public class MetadataCommand {

    private boolean chooseAll;
    private boolean person;
    private boolean scenario;
    private boolean measuration;


    private boolean name;
    private boolean birth;
    private boolean gender;
    private boolean phoneNumber;
    private boolean email;
    private boolean note;
    private boolean eyesDefects;
    private boolean hearingDefects;
    private boolean personAddParams;

    private boolean title;
    private boolean length;
    private boolean description;
    private boolean scenFile;


    private boolean times;
    private boolean temperature;
    private boolean weather;
    private boolean weatherNote;
    private boolean hardware;
    private boolean measurationAddParams;
    private boolean samplingRate;
    private boolean fileName;
    private boolean fileMetadata;

    public boolean isFileMetadata() {
        return fileMetadata;
    }

    public boolean isScenFile() {
        return scenFile;
    }

    public void setScenFile(boolean scenFile) {
        this.scenFile = scenFile;
    }

    public boolean isMeasurationAddParams() {
        return measurationAddParams;
    }

    public boolean isBirth() {
        return birth;
    }

    public boolean isChooseAll() {
        return chooseAll;
    }

    public boolean isDescription() {
        return description;
    }

    public boolean isEmail() {
        return email;
    }

    public boolean isEyesDefects() {
        return eyesDefects;
    }

    public boolean isGender() {
        return gender;
    }

    public boolean isHardware() {
        return hardware;
    }

    public boolean isHearingDefects() {
        return hearingDefects;
    }

    public boolean isLength() {
        return length;
    }

    public boolean isName() {
        return name;
    }

    public boolean isNote() {
        return note;
    }

    public boolean isPerson() {
        return person;
    }

    public boolean isPersonAddParams() {
        return personAddParams;
    }

    public boolean isPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSamplingRate() {
        return samplingRate;
    }

    public boolean isScenario() {
        return scenario;
    }

    public boolean isTemperature() {
        return temperature;
    }

    public boolean isTimes() {
        return times;
    }

    public boolean isTitle() {
        return title;
    }

    public boolean isWeather() {
        return weather;
    }

    public boolean isWeatherNote() {
        return weatherNote;
    }

    public boolean isMeasuration() {
        return measuration;
    }

    public boolean isFileName() {
        return fileName;
    }

    public void setFileName(boolean fileName) {
        this.fileName = fileName;
    }

    public void setFileMetadata(boolean fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public void setMeasuration(boolean measuration) {
        this.measuration = measuration;
    }

    public void setMeasurationAddParams(boolean measurationAddParams) {
        this.measurationAddParams = measurationAddParams;
    }

    public void setBirth(boolean birth) {
        this.birth = birth;
    }

    public void setChooseAll(boolean chooseAll) {
        this.chooseAll = chooseAll;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public void setEyesDefects(boolean eyesDefects) {
        this.eyesDefects = eyesDefects;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setHardware(boolean hardware) {
        this.hardware = hardware;
    }

    public void setHearingDefects(boolean hearingDefects) {
        this.hearingDefects = hearingDefects;
    }

    public void setLength(boolean length) {
        this.length = length;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public void setPerson(boolean person) {
        this.person = person;
    }

    public void setPersonAddParams(boolean personAddParams) {
        this.personAddParams = personAddParams;
    }

    public void setPhoneNumber(boolean phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSamplingRate(boolean samplingRate) {
        this.samplingRate = samplingRate;
    }

    public void setScenario(boolean scenario) {
        this.scenario = scenario;
    }

    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    public void setTimes(boolean times) {
        this.times = times;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public void setWeather(boolean weather) {
        this.weather = weather;
    }

    public void setWeatherNote(boolean weatherNote) {
        this.weatherNote = weatherNote;
    }


}

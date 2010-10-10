/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author pbruha
 */
public class ExperimentsSearcherCommand {
  
  private String startDate;
  private String startTime;
  private String endDate;
  private String endTime;
  private int scenario;
  private int[] hardware;
  private String gender;
  private int dateOption;
  private int genderOption;
  private int scenarioOption;
  private int hardwareOption;
  private int ageOption;
  private String fromDate;
  private String toDate;
  private int scenarioLenghtFrom;
  private int scenarioLenghtTo;


  
  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int[] getHardware() {
    return hardware;
  }

  public void setHardware(int[] hardware) {
    this.hardware = hardware;
  }

  public int getScenario() {
    return scenario;
  }

  public void setScenario(int scenario) {
    this.scenario = scenario;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

   public int getDateOption() {
    return dateOption;
  }

  public void setDateOption(int dateOption) {
    this.dateOption = dateOption;
  }

  public int getGenderOption() {
    return genderOption;
  }

  public void setGenderOption(int genderOption) {
    this.genderOption = genderOption;
  }

   public int getScenarioOption() {
    return scenarioOption;
  }

  public void setScenarioOption(int scenarioOption) {
    this.scenarioOption = scenarioOption;
  }

   public int getHardwareOption() {
    return hardwareOption;
  }

  public void setHardwareOption(int hardwareOption) {
    this.hardwareOption = hardwareOption;
  }

   public int getAgeOption() {
    return ageOption;
  }

  public void setAgeOption(int ageOption) {
    this.ageOption = ageOption;
  }

   public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public int getScenarioLenghtFrom() {
    return scenarioLenghtFrom;
  }

  public void setScenarioLenghtFrom(int scenarioLenghtFrom) {
    this.scenarioLenghtFrom = scenarioLenghtFrom;
  }

  public int getScenarioLenghtTo() {
    return scenarioLenghtTo;
  }

  public void setScenarioLenghtTo(int scenarioLenghtTo) {
    this.scenarioLenghtTo = scenarioLenghtTo;
  }
  
}

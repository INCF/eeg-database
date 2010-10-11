/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.history;

/**
 *
 * @author pbruha
 */
public class DownloadStatistic implements Comparable<DownloadStatistic>{
 private int scenarioId;
 private int experimentId;
 private String fileType;


 private String title;
 private long count;

 public DownloadStatistic(int scenarioId,String title, long count) {
   this.scenarioId = scenarioId;
   this.title = title;
   this.count = count;
   this.fileType = "Scenario-"+scenarioId+"(*.xml)";
 }

  public DownloadStatistic(int scenarioId,String title, String fileName, long count) {
   this.scenarioId = scenarioId;
   this.title = title;
   this.count = count;
   this.fileType = "Data file" + "- " +fileName;
 }

   public DownloadStatistic(int scenarioId, int experimentId, String title, long count) {
   this.scenarioId = scenarioId;
   this.title = title;
   this.count = count;
   this.fileType = "Experimet data(*.zip)";
 }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }


  public int getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(int scenarioId) {
    this.scenarioId = scenarioId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int compareTo(DownloadStatistic o) {
    if (this.count < o.count) return +1;
        if (this.count > o.count) return -1;
        return 0;
  }

  public int getExperimentId() {
    return experimentId;
  }

  public void setExperimentId(int experimentId) {
    this.experimentId = experimentId;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

}

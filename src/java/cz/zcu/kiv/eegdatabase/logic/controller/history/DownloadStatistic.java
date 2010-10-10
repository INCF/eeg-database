/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.history;

/**
 *
 * @author pbruha
 */
public class DownloadStatistic {
 private int scenarioId;
 private String title;
 private long count;

 public DownloadStatistic(int scenarioId,String title, long count) {
   this.scenarioId = scenarioId;
   this.title = title;
   this.count = count;
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
}

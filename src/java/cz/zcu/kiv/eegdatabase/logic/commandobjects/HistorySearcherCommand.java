/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.commandobjects;

import java.sql.Timestamp;

/**
 *
 * @author pbruha
 */
public class HistorySearcherCommand {
private String title;
private Timestamp fromDateOfDownload;
private Timestamp toDateOfDownload;

  public Timestamp getFromDateOfDownload() {
    return fromDateOfDownload;
  }

  public void setFromDateOfDownload(Timestamp fromDateOfDownload) {
    this.fromDateOfDownload = fromDateOfDownload;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Timestamp getToDateOfDownload() {
    return toDateOfDownload;
  }

  public void setToDateOfDownload(Timestamp toDateOfDownload) {
    this.toDateOfDownload = toDateOfDownload;
  }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 *
 * @author Honza
 */
public class SearchRequest {

  private String condition;
  private String source;
  private String choice;

  public SearchRequest() {
  }

  public SearchRequest(String condition, String value, String choice) {
    this.condition = condition;
    this.source = value;
    this.choice = choice;
  }



  public String getChoice() {
    return choice;
  }

  public void setChoice(String choice) {
    this.choice = choice;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}

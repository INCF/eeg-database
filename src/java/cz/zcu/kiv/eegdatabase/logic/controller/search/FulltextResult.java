/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 *
 * @author pbruha
 */
public class FulltextResult {

  private int id;
  private String foundString;
  private String section;

  public FulltextResult(int id, String foundString) {
    this.id = id;
    this.foundString = foundString;
  }

  public FulltextResult(int id, String foundString, String section) {
    this.id = id;
    this.foundString = foundString;
    this.section = section;
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    this.section = section;
  }

  public String getFoundString() {
    return foundString;
  }

  public void setFoundString(String foundString) {
    this.foundString = foundString;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}

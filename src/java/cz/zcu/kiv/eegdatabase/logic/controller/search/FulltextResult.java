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
  private String className;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
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

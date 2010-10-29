/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;


/**
 *
 * @author Honza
 */
public class FulltextResult {

  private Object obj;
  private String foundString;
  private String section;
  private String path;


  public FulltextResult(Object obj, String foundString) {
    this.obj = obj;
    this.foundString = foundString;
  }

  public FulltextResult(Object obj, String foundString, String section) {
    this.obj = obj;
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

  public Object getObj() {
    return obj;
  }

  public void setObj(Object obj) {
    this.obj = obj;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}

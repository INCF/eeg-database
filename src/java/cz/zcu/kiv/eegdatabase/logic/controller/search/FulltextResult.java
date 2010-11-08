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

  private int id;
  private String foundString;
  private String section;
  private String path;
  private String title;


  public FulltextResult(int id, String foundString) {
    this.id = id;
    this.foundString = foundString;
  }

  public FulltextResult(int id, String foundString, String section, String path, String title) {
    this.id = id;
    this.foundString = foundString;
    this.section = section;
    this.path = path;
    this.title = title;
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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
    public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof FulltextResult)) return false;
    FulltextResult fr = (FulltextResult) obj;
    if ((this.section.equals(fr.section))&&(this.id == fr.id)) return true;
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + this.id;
    hash = 97 * hash + (this.section != null ? this.section.hashCode() : 0);
    return hash;
  }


}

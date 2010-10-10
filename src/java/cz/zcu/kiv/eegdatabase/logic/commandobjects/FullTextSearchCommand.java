/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author Petr Ježek
 */
public class FullTextSearchCommand {

  private String searchTI;
  private String weathernote;

  public String getWeathernote() {
    return weathernote;
  }

  public void setWeathernote(String weathernote) {
    this.weathernote = weathernote;
  }

  /**
   * @return the searchTI
   */
  public String getSearchTI() {
    return searchTI;
  }

  /**
   * @param searchTI the searchTI to set
   */
  public void setSearchTI(String searchTI) {
    this.searchTI = searchTI;
  }

}

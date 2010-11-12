/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

/**
 *
 * @author Honza
 */
public interface IWrapper {

  public void setObject(Object o);

  public String getTitle();

  public String className();

  public String getSetName();

  public String getPath();
}

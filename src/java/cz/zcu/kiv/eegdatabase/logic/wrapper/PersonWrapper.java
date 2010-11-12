/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 *
 * @author Honza
 */
public class PersonWrapper implements IWrapper {

  private Person p;

  @Override
  public void setObject(Object o) {
    this.p = (Person) o;
  }

  @Override
  public String getTitle() {
    return p.getNote();
  }

  @Override
  public String className() {
    return "Person";
  }

  @Override
  public String getSetName() {
    return "persons";
  }

  @Override
  public String getPath() {
    return "/people/detail.html?personId=";
  }
}

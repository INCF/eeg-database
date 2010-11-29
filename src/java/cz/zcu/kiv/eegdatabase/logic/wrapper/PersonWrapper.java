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
public class PersonWrapper implements Wrapper {

  private Person p;

  public PersonWrapper(Object p) {
    this.p = (Person) p;
  }


  @Override
  public String getTitle() {
    return p.getNote();
  }


  @Override
  public String getPath() {
    return "/people/detail.html?personId=";
  }

  public String className() {
    return "Person";
  }
}

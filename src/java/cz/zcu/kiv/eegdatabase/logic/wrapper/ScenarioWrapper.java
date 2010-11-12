/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;

/**
 *
 * @author Honza
 */
public class ScenarioWrapper implements IWrapper {

  private Scenario s;

  @Override
  public void setObject(Object o) {
    this.s = (Scenario) o;
  }

  @Override
  public String getTitle() {
    return s.getTitle();
  }

  @Override
  public String className() {
    return "Scenario";
  }

  @Override
  public String getSetName() {
    return "scenarios";
  }

  @Override
  public String getPath() {
    return "/scenarios/detail.html?scenarioId=";
  }
}

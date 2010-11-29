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
public class ScenarioWrapper implements Wrapper {

  private Scenario s;

  public ScenarioWrapper(Object s) {
    this.s = (Scenario) s;
  }


  @Override
  public String getTitle() {
    return s.getTitle();
  }


  @Override
  public String getPath() {
    return "/scenarios/detail.html?scenarioId=";
  }

  public String className() {
    return "Scenario";
  }
}

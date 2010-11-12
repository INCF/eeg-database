/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

/**
 *
 * @author Honza
 */
public class ExperimentWrapper implements IWrapper {

  private Experiment e;

  @Override
  public void setObject(Object o) {
    this.e = (Experiment) o;
  }

  @Override
  public String getTitle() {
    return e.getScenario().getTitle();
  }

  @Override
  public String className() {
    return "Experiment";
  }

  @Override
  public String getSetName() {
    return "experiments";
  }

  @Override
  public String getPath() {
    return "/experiments/detail.html?experimentId=";
  }
}

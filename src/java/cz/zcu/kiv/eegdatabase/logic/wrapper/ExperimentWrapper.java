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
public class ExperimentWrapper implements Wrapper {

  private Experiment e;

  public ExperimentWrapper(Object e) {
    this.e = (Experiment) e;
  }

  @Override
  public String getTitle() {
    return e.getScenario().getTitle();
  }

  public String className() {
    return "Experiment";
  }


  @Override
  public String getPath() {
    return "/experiments/detail.html?experimentId=";
  }
}

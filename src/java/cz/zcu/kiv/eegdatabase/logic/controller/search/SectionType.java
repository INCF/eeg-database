/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.logic.wrapper.ArticleWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.ExperimentWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.PersonWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.ScenarioWrapper;

/**
 *
 * @author Honza
 */
public enum SectionType {

  ARTICLE("article", "articles", ArticleWrapper.class),
  EXPERIMENT("experiment", "experiments", ExperimentWrapper.class),
  PERSON("person", "persons", PersonWrapper.class),
  SCENARIO("scenario", "scenario", ScenarioWrapper.class);
  private String simpleRelName;
  private String setName;
  private Class wrapperClass;

  private SectionType(String simpleRelName, String setName, Class wrapperClass) {
    this.simpleRelName = simpleRelName;
    this.setName = setName;
    this.wrapperClass = wrapperClass;
  }

  public String getSetName() {
    return setName;
  }

  public String getSimpleRelName() {
    return simpleRelName;
  }

  public Class getWrapperClass() {
    return wrapperClass;
  }
}

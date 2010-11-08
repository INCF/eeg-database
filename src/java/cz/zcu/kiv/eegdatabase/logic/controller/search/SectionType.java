/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 *
 * @author Honza
 */
public enum SectionType {

  SCENARIO {

    public String className() {
      return "Scenario";
    }

    public String getSetName() {
      return "scenarios";
    }

    public String getPath() {
      return "/scenarios/detail.html?scenarioId=";
    }
  },
  EXPERIMENT {

    public String className() {
      return "Experiment";
    }

    public String getSetName() {
      return "experiments";
    }

    public String getPath() {
      return "/experiments/detail.html?experimentId=";
    }
  },
  PERSON {

    public String className() {
      return "Person";
    }

    public String getSetName() {
      return "persons";
    }

    public String getPath() {
      return "/people/detail.html?personId=";
    }
  },
  ARTICLE {

    public String className() {
      return "Article";
    }

    public String getSetName() {
      return "articles";
    }

    public String getPath() {
      return "/articles/detail.html?articleId=";
    }
  };

  public abstract String className();

  public abstract String getSetName();

  public abstract String getPath();
}

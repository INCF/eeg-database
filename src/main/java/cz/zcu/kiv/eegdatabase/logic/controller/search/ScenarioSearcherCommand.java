/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 * @author pbruha
 */
public class ScenarioSearcherCommand {

    private int scenarioOption;
    private int scenario;
    private int lenghtOption;
    private int scenarioLenghtFrom;
    private int scenarioLenghtTo;
    private int authorScenarioOption;
    private int authorsScenario;

    public int getAuthorScenarioOption() {
        return authorScenarioOption;
    }

    public void setAuthorScenarioOption(int authorScenarioOption) {
        this.authorScenarioOption = authorScenarioOption;
    }

    public int getAuthorsScenario() {
        return authorsScenario;
    }

    public void setAuthorsScenario(int authorsScenario) {
        this.authorsScenario = authorsScenario;
    }

    public int getLenghtOption() {
        return lenghtOption;
    }

    public void setLenghtOption(int lenghtOption) {
        this.lenghtOption = lenghtOption;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public int getScenarioLenghtFrom() {
        return scenarioLenghtFrom;
    }

    public void setScenarioLenghtFrom(int scenarioLenghtFrom) {
        this.scenarioLenghtFrom = scenarioLenghtFrom;
    }

    public int getScenarioLenghtTo() {
        return scenarioLenghtTo;
    }

    public void setScenarioLenghtTo(int scenarioLenghtTo) {
        this.scenarioLenghtTo = scenarioLenghtTo;
    }

    public int getScenarioOption() {
        return scenarioOption;
    }

    public void setScenarioOption(int scenarioOption) {
        this.scenarioOption = scenarioOption;
    }
}

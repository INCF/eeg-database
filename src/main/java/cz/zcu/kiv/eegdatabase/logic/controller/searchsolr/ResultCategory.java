package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

/**
 * Enum for the categories of full text search results.
 * User: Jan Koren
 * Date: 8.4.13
 */
public enum ResultCategory {

    ARTICLE("Article"),
    EXPERIMENT("Experiment"),
    PERSON("Person"),
    RESEARCH_GROUP("Research group"),
    SCENARIO("Scenario");

    private String value;

    private ResultCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

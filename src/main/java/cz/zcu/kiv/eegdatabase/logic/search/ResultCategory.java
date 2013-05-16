package cz.zcu.kiv.eegdatabase.logic.search;

/**
 * Enumeration type for the categories of full text search results.
 * User: Jan Koren
 * Date: 8.4.13
 */
public enum ResultCategory {

    ARTICLE("Article"),
    EXPERIMENT("Experiment"),
    PERSON("Person"),
    RESEARCH_GROUP("Research group"),
    SCENARIO("Scenario"),
    ALL("All");

    private String value;

    private ResultCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ResultCategory getCategory(String value) {
        for(ResultCategory category : values()) {
            if(category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }
}

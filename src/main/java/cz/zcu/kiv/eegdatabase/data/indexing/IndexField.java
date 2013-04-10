package cz.zcu.kiv.eegdatabase.data.indexing;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public enum IndexField {

    CLASS("class"),
    FILE_MIMETYPE("file_mimetype"),
    ID("id"),
    NAME("name"),
    PARAM_DATATYPE("param_datatype"),
    SOURCE("source"), // Database/LinkedIn
    TEMPERATURE("temperature"),
    TEXT("text"), // for long text, notes, description etc.
    CHILD_TEXT("child_text"),
    TIMESTAMP("timestamp"),
    TITLE("title"),
    CHILD_TITLE("child_title"),
    UUID("uuid"),
    AUTOCOMPLETE("autocomplete");

    private String value;

    private IndexField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package cz.zcu.kiv.eegdatabase.data.indexing;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public enum IndexField {

    // properties are sorted alhpabetically
    CHILD_CLASS("child_class"), // e.g. ArtifactRemoveMethod is the child class of Artifact
    CLASS("class"),
    DESCRIPTION("description"),
    FILE_MIMETYPE("file_mimetype"),
    ID("id"),
    NAME("name"),
    NOTE("note"),
    PARAM_DATATYPE("param_datatype"),
    PARENT_CLASS("parent_class"), // e.g. Article is the parent class of ArticleComment
    TEMPERATURE("temperature"),
    TEXT("text"),
    TITLE("title"),
    UUID("uuid"),
    SOURCE("source");

    private String value;

    private IndexField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

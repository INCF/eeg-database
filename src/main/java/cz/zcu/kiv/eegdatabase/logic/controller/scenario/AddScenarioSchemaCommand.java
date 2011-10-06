package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 8.6.11
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class AddScenarioSchemaCommand {

    private int id;
    private String schemaDescription;
    private MultipartFile schemaFile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchemaDescription() {
        return schemaDescription;
    }

    public void setSchemaDescription(String schemaDescription) {
        this.schemaDescription = schemaDescription;
    }

    public MultipartFile getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(MultipartFile schemaFile) {
        this.schemaFile = schemaFile;
    }

}

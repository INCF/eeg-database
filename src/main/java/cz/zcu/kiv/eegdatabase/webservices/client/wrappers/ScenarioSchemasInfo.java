package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * @author František Liška
 */
public class ScenarioSchemasInfo {
  private int schemaId;
  private String schemaName;
  //private Clob sql;
  //private Clob hbmXml;
  //private Clob pojo;
  //private String bean;
  private String description;
  private char approved;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getApproved() {
        return approved;
    }

    public void setApproved(char approved) {
        this.approved = approved;
    }
}

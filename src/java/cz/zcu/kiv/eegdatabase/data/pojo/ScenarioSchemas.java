/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 *
 * @author Jan Koreň
 */
public class ScenarioSchemas implements java.io.Serializable {

  private int schemaId;
  private String schemaName;

  public ScenarioSchemas() {
  }

  public ScenarioSchemas(int schemaId, String schemaName) {
    this.schemaId = schemaId;
    this.schemaName = schemaName;
  }

  public int getSchemaId() {
    return schemaId;
  }

  public void setSchemaId(int schemaId) {
    this.schemaId = schemaId;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }
}

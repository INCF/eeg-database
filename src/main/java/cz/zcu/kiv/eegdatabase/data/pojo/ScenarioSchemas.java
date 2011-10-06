package cz.zcu.kiv.eegdatabase.data.pojo;

import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.XSSchemaSet;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.xml.sax.SAXException;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jan Koren
 */
public class ScenarioSchemas implements java.io.Serializable {

  private int schemaId;
  private String schemaName;
  private Clob sql;
  private Clob hbmXml;
  private Clob pojo;
  private String bean;
  private String description;
  private char approved;

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

  public Clob getSql() {
    return sql;
  }

  public void setSql(Clob sql) {
    this.sql = sql;
  }

  public Clob getHbmXml() {
    return hbmXml;
  }

  public void setHbmXml(Clob hbmXml) {
    this.hbmXml = hbmXml;
  }

  public Clob getPojo() {
    return pojo;
  }

  public void setPojo(Clob pojo) {
    this.pojo = pojo;
  }

  public String getBean() {
    return bean;
  }

  public void setBean(String bean) {
    this.bean = bean;
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

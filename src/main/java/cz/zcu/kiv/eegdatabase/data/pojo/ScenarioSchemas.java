package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.sql.Clob;

/**
 * @author Jan Koren
 */
@Entity
@javax.persistence.Table(name = "SCENARIO_SCHEMA")
public class ScenarioSchemas implements java.io.Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SCHEMA_ID")
    private int schemaId;
    @SolrField(name = IndexField.NAME)
    @Column(name = "SCHEMA_NAME")
    private String schemaName;
    @Column(name = "SQL")
    @Lob
    private Clob sql;
    @Lob
    @Column(name = "HBM_XML")
    private Clob hbmXml;
    @Lob
    @Column(name = "POJO")
    private Clob pojo;
    @Column(name = "BEAN")
    private String bean;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "APPROVED")
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

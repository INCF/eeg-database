package cz.zcu.kiv.eegdatabase.logic.schemagen;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.parser.XSOMParser;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 6.6.11
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioSchemaGenerator {

    private int schemaId;
    private String schemaName;
    private byte[] content;

    public ScenarioSchemaGenerator(int schemaId, String schemaName, byte[] content) {
        this.schemaId = schemaId;
        this.content = content;
        this.schemaName = schemaName;
    }

    public String generateSql() {

        String xsd = new String(content);

        String registerSchema = "dbms_xmlschema.registerschema(\n" +
              "    SCHEMAURL => "+ schemaName +",\n" +
              "    SCHEMADOC => " + xsd +",\n" +
              "    LOCAL       => TRUE,\n" +
              "    GENTYPES    => TRUE,\n" +
              "    GENBEAN     => FALSE,\n" +
              "    GENTABLES   => TRUE,\n" +
              "    FORCE       => FALSE,\n" +
              "    OWNER       => USER\n" +
              "  );";

        String createTable = "create table SCENARIO_TYPE_SCHEMA_" + schemaId + " (\n" +
              "  SCENARIO_ID number not null,\n" +
              "  SCENARIO_XML xmltype,\n" +
              "  CONSTRAINT SCENARIO_TYPE_SCHEMA_" + schemaId + "_PK\n" +
              "    PRIMARY KEY(SCENARIO_ID),\n" +
              "  CONSTRAINT SCENARIO_TYPE_" + schemaId + "_SCENARIO_FK1\n" +
              "    FOREIGN KEY(SCENARIO_ID)\n" +
              "    REFERENCES SCENARIO_TYPE_PARENT(SCENARIO_ID)\n" +
              "    ON DELETE CASCADE\n" +
              "  )\n" +
              "  xmltype SCENARIO_XML store as object relational\n" +
              "  xmlschema \"" + schemaName + "\"\n" +
              "  ELEMENT \"" + this.getElement() + "\";";

        String s = registerSchema + createTable;

        /*
        try {
            clob.setCharacterStream();
                    setString(1, s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        return s;
    }

    public String generateHbmXml() {
        String hbm = "<joined-subclass name=\"cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeSchema"+ schemaId +"\"\n" +
              "                     table=\"SCENARIO_TYPE_SCHEMA_" + schemaId + "\">\n" +
              "        <key column=\"SCENARIO_ID\"/>\n" +
              "        <property name=\"scenarioXml\" type=\"cz.zcu.kiv.eegdatabase.data.datatypes.OracleXMLType\">\n" +
              "            <column name=\"SCENARIO_XML\"/>\n" +
              "        </property>\n" +
              "    </joined-subclass>";

        /*
        Clob clob = null;
        try {
            clob.setString(0, hbm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        return hbm;
    }

    public String generatePojo() {
        String pojo = "package cz.zcu.kiv.eegdatabase.data.pojo;\n" +
              "\n" +
              "import org.w3c.dom.Document;\n" +
              "\n" +
              "public class ScenarioTypeSchema"+ schemaId +"  extends ScenarioType<Document> implements java.io.Serializable {\n" +
              "\n" +
              "    private Document scenarioXml;\n" +
              "\n" +
              "    public ScenarioTypeSchema"+ schemaId + "() {\n" +
              "    }\n" +
              "\n" +
              "    public Document getScenarioXml() {\n" +
              "      return scenarioXml;\n" +
              "    }\n" +
              "\n" +
              "    public void setScenarioXml(Document scenarioXml) {\n" +
              "      this.scenarioXml = scenarioXml;\n" +
              "    }\n" +
              "}";

        return pojo;

    }

    public String generateBean() {

        String bean = "<bean id=\"scenarioTypeSchema" + schemaId + "\" " +
                "class=\"cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeSchema" + schemaId + "\"\n" +
                "    parent=\"scenarioSchemaDefault\">\n" +
                "</bean>";

        return bean;
    }

    private String getElement() {

        InputStream xsd = new ByteArrayInputStream(content);

        XSOMParser parser = new XSOMParser();
        String myString = content.toString();

        try {
            parser.parse(xsd);
        } catch (SAXException e) {
            if(e.getException() != null)
                e.getException().printStackTrace();
        }

        Set<SchemaDocument> docSet = parser.getDocuments();
        Map<String, XSElementDecl> elementDeclMap = null;
        for(SchemaDocument doc : docSet) {
            elementDeclMap = doc.getSchema().getElementDecls();
            if(!elementDeclMap.isEmpty()) break;
        }

        TreeSet<String> elementNameSet = new TreeSet(elementDeclMap.keySet());
        String elementName = elementNameSet.first();

        System.out.println(elementName);

        return elementName;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

}

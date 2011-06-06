package cz.zcu.kiv.eegdatabase.logic.schemagen;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.parser.XSOMParser;
import org.xml.sax.SAXException;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 6.6.11
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioSchemaGenerator {

    private String schemaId;
    private String schemaName;
    private Clob content;

    public ScenarioSchemaGenerator(String schemaId, String schemaName, Clob content) {
        this.schemaId = schemaId;
        this.content = content;
        this.schemaName = schemaName;
    }

    public Clob generateSql() {

        String registerSchema = "dbms_xmlschema.registerschema(\n" +
              "    SCHEMAURL => "+ schemaName +",\n" +
              "    SCHEMADOC => " + content +",\n" +
              "    LOCAL       => TRUE,\n" +
              "    GENTYPES    => TRUE,\n" +
              "    GENBEAN     => FALSE,\n" +
              "    GENTABLES   => TRUE,\n" +
              "    FORCE       => FALSE,\n" +
              "    OWNER       => USER\n" +
              "  );";

        String createTable = "create table SCENARIO_TYPE_SCHEMA_" + schemaId + " (\n" +
              "  SCENARIO_ID number not nulL,\n" +
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
              "  ELEMENT \"" + getElement() + "\";";

        String s = registerSchema + createTable;
        Clob clob = null;
        try {
            clob.setString(0, s);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clob;
    }

    public Clob generateHbmXml() {
        String hbm = "<joined-subclass name=\"cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeSchema"+ schemaId +"\"\n" +
              "                     table=\"SCENARIO_TYPE_SCHEMA_" + schemaId + "\">\n" +
              "        <key column=\"SCENARIO_ID\"/>\n" +
              "        <property name=\"scenarioXml\" type=\"cz.zcu.kiv.eegdatabase.data.datatypes.OracleXMLType\">\n" +
              "            <column name=\"SCENARIO_XML\"/>\n" +
              "        </property>\n" +
              "    </joined-subclass>";

        Clob clob = null;
        try {
            clob.setString(0, hbm);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clob;
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

        return null;
    }

    private String getElement() {
        XSOMParser parser = new XSOMParser();

        try {
            parser.parse(this.getContent().getCharacterStream());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Set<SchemaDocument> docSet = parser.getDocuments();

        Map<String, XSElementDecl> elementDeclMap = null;
        for(SchemaDocument doc : docSet) {
            elementDeclMap = doc.getSchema().getElementDecls();
        }

        String s = elementDeclMap.toString();

        String elementName = elementDeclMap.get(elementDeclMap.get("element")).getName();

        System.out.println(s);
        System.out.println(elementName);

        //XSSchemaSet schemaSet = parser.getResult();
        //Iterator iter = schemaSet.iterateSchema();

        return elementName;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public Clob getContent() {
        return content;
    }

    public void setContent(Clob content) {
        this.content = content;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

}

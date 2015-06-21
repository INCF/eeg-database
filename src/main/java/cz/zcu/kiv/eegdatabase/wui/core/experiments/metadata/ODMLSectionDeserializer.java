package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import odml.core.Reader;
import odml.core.Section;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.json.XML;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class ODMLSectionDeserializer extends JsonDeserializer<Section> {

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public Section deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        long start = System.currentTimeMillis();

        try {

            String jsonString = jp.getCodec().readTree(jp).toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            String xmlString = XML.toString(jsonObject);
            Reader reader = new Reader();
            
            ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes("UTF-8")); // encoding is necessary
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dbf.newDocumentBuilder();
            InputSource source = new InputSource(stream);
            source.setEncoding("UTF-8"); // encoding is necessary
            Document dom = dbuilder.parse(source);
            Element rootElement = dom.getDocumentElement();
            rootElement.setAttribute("version", "1.0");

            stream = new ByteArrayInputStream(getStringFromDocument(dom).getBytes());
            return reader.load(stream);
        } catch (JSONException e) {
            log.warn(e.getMessage(), e);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        } finally {
            long end = System.currentTimeMillis();
            log.warn("Deserialize time - " + (end - start) + " ms.");
        }
        return null;
    }

    public String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException e) {
            log.warn(e.getMessage(), e);
            return "";
        }
    }
}

package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import odml.core.Section;
import odml.core.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.json.XML;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ODMLSectionSerializer extends JsonSerializer<Section> {

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public void serialize(Section value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        long start = System.currentTimeMillis();
        try {
            
            Writer wr = new Writer(value, true, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            wr.write(stream, false, false);

            String xmlString = stream.toString("UTF-8"); // encoding is necessary
            
            IOUtils.write(xmlString, new FileOutputStream(new File("D:\\tmp\\test.xml")));
            
            JSONObject jsonObject = XML.toJSONObject(xmlString);
//            String jsonString = jsonObject.toString(); /// XXX remove this - UTF8 encoding problem. 
            String jsonString = new String(jsonObject.toString().getBytes("UTF-8")); // encoding is necessary
            IOUtils.write(jsonString, new FileOutputStream(new File("D:\\tmp\\test.json")));
            
            jgen.writeRawValue(jsonString);

        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        } finally {
            long end = System.currentTimeMillis();
            log.warn("Serialize time - " + (end - start) + " ms.");
        }

    }

}

/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ODMLSectionSerializer.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import odml.core.Section;
import odml.core.Writer;

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
            JSONObject jsonObject = XML.toJSONObject(xmlString);
            String jsonString = new String(jsonObject.toString()); // encoding is necessary
            jgen.writeRawValue(jsonString);

        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        } finally {
            long end = System.currentTimeMillis();
            log.trace("Serialize time - " + (end - start) + " ms.");
        }

    }

}

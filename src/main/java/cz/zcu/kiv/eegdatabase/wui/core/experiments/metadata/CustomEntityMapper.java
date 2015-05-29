package cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata;

import java.io.IOException;

import odml.core.Section;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.data.elasticsearch.core.EntityMapper;

public class CustomEntityMapper implements EntityMapper {

    protected Log log = LogFactory.getLog(getClass());

    private ObjectMapper objectMapper;

    public CustomEntityMapper() {

        objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); need it ??? don't know now.

        SimpleModule module = new SimpleModule("section", Version.unknownVersion());
        module.addSerializer(Section.class, new ODMLSectionSerializer());
        module.addDeserializer(Section.class, new ODMLSectionDeserializer());

        objectMapper.registerModule(module);
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }
}

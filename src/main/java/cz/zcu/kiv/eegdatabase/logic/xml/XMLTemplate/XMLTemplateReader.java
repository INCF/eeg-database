package cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.XMLTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * XMLTemplateReader, 2014/07/07 14:24 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class XMLTemplateReader {

    public XMLTemplateReader() {
    }

    public static XMLTemplate read(byte[] xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLTemplate.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputStream is = new ByteArrayInputStream(xml);
        XMLTemplate template = (XMLTemplate)unmarshaller.unmarshal(is);

        return  template;
    }
}

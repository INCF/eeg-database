package cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.XMLTags;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private static XMLEventReader eventReader;

    public XMLTemplateReader() {
    }

    public static List<SectionType> readTemplate(byte[] template) throws XMLStreamException {
        List<SectionType> sections = new ArrayList<SectionType>();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream is = new ByteArrayInputStream(template);
        eventReader = inputFactory.createXMLEventReader(is);

        SectionType section;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                String name = event.asStartElement().getName().toString();
                if (name.equalsIgnoreCase(XMLTags.SECTION)) {
                    section = readSection();
                    sections.add(section);
                }
            }
        }
        return sections;
    }

    private static SectionType readSection() throws XMLStreamException {
        SectionType section = null;
        SectionType subsection;
        //------section attributes-------
        String name = "";
        Boolean required = false;
        int maxCount = 1;
        List<SectionType> subsections = new ArrayList<SectionType>();
        int minCount = 1;
        int selectedCount = 1;
        boolean selected = false;
        //-------------------------------

        boolean readNext = true;
        while (readNext) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                //section inside section => subsection
                if (startElement.getName().getLocalPart().equalsIgnoreCase(XMLTags.SECTION)) {
                    subsection = readSection();
                    subsections.add(subsection);

                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.NAME)) {
                    event = eventReader.nextEvent();
                    name = event.asCharacters().getData();
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.REQUIRED)) {
                    event = eventReader.nextEvent();
                    required = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.MAX_COUNT)) {
                    event = eventReader.nextEvent();
                    maxCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.MIN_COUNT)) {
                    event = eventReader.nextEvent();
                    minCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.SELECTED_COUNT)) {
                    event = eventReader.nextEvent();
                    selectedCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart()
                        .equalsIgnoreCase(XMLTags.SELECTED)) {
                    event = eventReader.nextEvent();
                    selected = Boolean.parseBoolean(event.asCharacters().getData());
                }
            }
            // If we reach the end of an section element, we end loop and return new section
            else if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equalsIgnoreCase(XMLTags.SECTION)) {
                    section = new SectionType(name, required, maxCount, subsections,
                            minCount, selectedCount, selected);
                    readNext = false;
                }
            }
        }

        return section;
    }
}

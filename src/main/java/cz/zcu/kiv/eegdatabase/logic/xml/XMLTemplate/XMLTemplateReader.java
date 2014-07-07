package cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
    private final String SECTION = "section";
    private final String NAME = "name";
    private final String REQUIRED = "required";
    private final String MAX_COUNT = "maxCount";
    private final String MIN_COUNT = "minCount";
    private final String SELECTED_COUNT = "selectedCount";
    private final String SELECTED = "selected";

    private XMLInputFactory inputFactory;
    private XMLEventReader eventReader;

    public XMLTemplateReader() {
    }

    public List<SectionType> readTemplate(byte[] template) throws XMLStreamException {
        List<SectionType> sections = new ArrayList<SectionType>();
        inputFactory = XMLInputFactory.newInstance();
        InputStream is = new ByteArrayInputStream(template);
        eventReader = inputFactory.createXMLEventReader(is);

        SectionType section = null;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                String name = event.asStartElement().getName().toString();
                if (name.equals(SECTION)) {
                    section = readSection();
                    sections.add(section);
                }
            }
        }
        return sections;
    }

    private SectionType readSection() throws XMLStreamException {
        SectionType section = null;
        SectionType subsection = null;
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
                if (startElement.getName().getLocalPart().equals(SECTION)) {
                    subsection = readSection();
                    subsections.add(subsection);

                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equals(NAME)) {
                    event = eventReader.nextEvent();
                    name = event.asCharacters().getData();
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equals(REQUIRED)) {
                    event = eventReader.nextEvent();
                    required = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equals(MAX_COUNT)) {
                    event = eventReader.nextEvent();
                    maxCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart()
                        .equals(MIN_COUNT)) {
                    event = eventReader.nextEvent();
                    minCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart()
                        .equals(SELECTED_COUNT)) {
                    event = eventReader.nextEvent();
                    selectedCount = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart()
                        .equals(SELECTED)) {
                    event = eventReader.nextEvent();
                    selected = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
            }
            // If we reach the end of an section element, we end loop and return new section
            else if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart() == (SECTION)) {
                    section = new SectionType(name, required, maxCount, subsections,
                            minCount, selectedCount, selected);
                    readNext = false;
                }
            }
        }

        return section;
    }
}

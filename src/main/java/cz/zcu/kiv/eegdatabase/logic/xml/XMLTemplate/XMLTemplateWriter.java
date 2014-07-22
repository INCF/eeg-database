package cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.XMLTags;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.ByteArrayOutputStream;
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
 * XMLTemplateWriter, 2014/07/08 10:16 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class XMLTemplateWriter {

    private static XMLEventFactory eventFactory;
    private static XMLEventWriter eventWriter;

    public XMLTemplateWriter() {
    }

    /**
     * Writes list of sections into XML and returns it as array of bytes
     * @param sections list of sections
     * @return sections written in XML
     * @throws XMLStreamException Error during XML writing
     */
    public static byte[] writeTemplate(List<SectionType> sections) throws XMLStreamException {
        // create an XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        // create XMLEventWriter
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        eventWriter = outputFactory.createXMLEventWriter(byteOut);
        // create an EventFactory
        eventFactory = XMLEventFactory.newInstance();
        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        addLineBreak();
        //create odML tag with one attribute
        StartElement startElement = eventFactory.createStartElement("", "", XMLTags.ODML);
        Attribute versionAttribute = eventFactory.createAttribute(XMLTags.ODML_VERSION_ATTRIBUTE,
                XMLTags.ODML_VERSION_ATTRIBUTE_VALUE);
        eventWriter.add(startElement);
        eventWriter.add(versionAttribute);
        addLineBreak();

        //write all sections
        for (SectionType section : sections) {
            writeSection(section, 1);
        }

        //write last end tag and end document
        eventWriter.add(eventFactory.createEndElement("", "", XMLTags.ODML));
        addLineBreak();
        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.flush();
        eventWriter.close();

        return byteOut.toByteArray();
    }

    /**
     * Writes one section into xml
     * @param section section
     * @param level level of this section - 1 for sections, 2 for subsections etc. Necessary only for formatting reasons.
     * @throws XMLStreamException Error during XML writing a section
     */
    private static void writeSection(SectionType section, int level) throws XMLStreamException {
        int nextLevel = level + 1;
        addTab(level);
        eventWriter.add(eventFactory.createStartElement("", "", XMLTags.SECTION));
        addLineBreak();

        //write all section tags
        writeNode(XMLTags.NAME, section.getName(), nextLevel);
        writeNode(XMLTags.REQUIRED, "" + section.isRequired(), nextLevel);
        writeNode(XMLTags.MAX_COUNT, "" + section.getMaxCount(), nextLevel);
        writeNode(XMLTags.MIN_COUNT, "" + section.getMinCount(), nextLevel);
        writeNode(XMLTags.SELECTED_COUNT, "" + section.getSelectedCount(), nextLevel);
        writeNode(XMLTags.SELECTED, "" + section.isSelected(), nextLevel);

        List<SectionType> subsections = section.getSubsections();

        if (!section.hasEmptyOrNullSubsections()) {
            for (SectionType subsection : subsections) {
                writeSection(subsection, nextLevel);
            }
        }

        addTab(level);
        eventWriter.add(eventFactory.createEndElement("", "", XMLTags.SECTION));
        addLineBreak();
    }

    /**
     * Writes one node into XML
     * @param name node name
     * @param value node value
     * @param level node level
     * @throws XMLStreamException Error during XML writing a node
     */
    private static void writeNode(String name, String value, int level) throws XMLStreamException {
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        addTab(level);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        addLineBreak();
    }

    /**
     * Padding with tab
     * @param level number of inserted "tabs"
     * @throws XMLStreamException Error during XML writing
     */
    private static void addTab(int level) throws XMLStreamException {
        XMLEvent tab = eventFactory.createCharacters("\t");
        for (int i = 0; i < level; i++) {
            eventWriter.add(tab);
        }
    }

    /**
     * Adds new line char into XML
     * @throws XMLStreamException Error during XML writing
     */
    private static void addLineBreak() throws XMLStreamException {
        eventWriter.add(eventFactory.createCharacters("\n"));
    }
}

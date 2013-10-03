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
 *   RSSFeedWriter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.article;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RSSFeedWriter {

    private String outputFile;
    private Feed rssfeed;

    public RSSFeedWriter(Feed rssfeed, String outputFile) {
        this.rssfeed = rssfeed;
        this.outputFile = outputFile;
    }

    public String write() throws Exception {

        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            public String toString() {
                return this.string.toString();
            }
        };
        // Create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        // Create XMLEventWriter
        XMLEventWriter eventWriter = outputFactory
                .createXMLEventWriter(new FileOutputStream(outputFile));


        // Create a EventFactory

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("");

        // Create and write Start Tag

        StartDocument startDocument = eventFactory.createStartDocument();

        eventWriter.add(startDocument);

        // Create open tag
        //eventWriter.add(end);

        StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        eventWriter.add(rssStart);
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        //eventWriter.add(end);

        eventWriter.add(eventFactory.createStartElement("", "", "channel"));
        //eventWriter.add(end);

        // Write the different nodes

        createNode(eventWriter, "title", rssfeed.getTitle());

        createNode(eventWriter, "link", rssfeed.getLink());

        createNode(eventWriter, "description", rssfeed.getDescription());

        createNode(eventWriter, "language", rssfeed.getLanguage());

        createNode(eventWriter, "copyright", rssfeed.getCopyright());

        createNode(eventWriter, "pubdate", rssfeed.getPubDate());

        for (FeedMessage entry : rssfeed.getMessages()) {
            eventWriter.add(eventFactory.createStartElement("", "", "item"));
            //eventWriter.add(end);
            createNode(eventWriter, "title", entry.getTitle());
            createNode(eventWriter, "description", entry.getDescription());
            createNode(eventWriter, "link", entry.getLink());
            createNode(eventWriter, "author", entry.getAuthor());
            createNode(eventWriter, "guid", entry.getGuid());
            //eventWriter.add(end);
            eventWriter.add(eventFactory.createEndElement("", "", "item"));
            //eventWriter.add(end);

        }

        //eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "channel"));
        //eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));

        //eventWriter.add(end);

        eventWriter.add(eventFactory.createEndDocument());
        System.out.println(output);

        eventWriter.close();
        return output.toString();
    }

    private void createNode(XMLEventWriter eventWriter, String name,

                            String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // Create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        //eventWriter.add(tab);
        eventWriter.add(sElement);
        // Create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        //eventWriter.add(end);
    }

}

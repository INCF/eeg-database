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
 *   BookingRoomXmlUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * User: Jenda
 * Date: 6.4.11
 * Time: 13:06
 */
public class BookingRoomXmlUtils
{
    public static final String RESERVATION_COLOR = "red";
    public static final String MY_RESERVATION_COLOR = "green";

    public static String getXmlAsString(Document doc) throws TransformerException
    {
        doc.normalizeDocument();
        StringWriter stw = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.transform(new DOMSource(doc), new StreamResult(stw));
        return stw.toString();
    }

    public static String formatReservationsList(List<Reservation> reservations, Person loggedUser) throws ParserConfigurationException, TransformerException
    {
        Document doc = getNewDocument();
        Node root = doc.getFirstChild();

        for (Reservation reservation : reservations)
        {
            root.appendChild(formatReservation(doc, reservation, loggedUser));
        }

        return getXmlAsString(doc);
    }

    private static Document getNewDocument() throws ParserConfigurationException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = documentBuilderFactory.newDocumentBuilder().newDocument();
        doc.appendChild(doc.createElement("data"));
        return doc;
    }

    private static Node formatReservation(Document doc, Reservation reservation, Person loggedUser)
    {
        String title = "Reservation by " + BookingRoomUtils.formatPersonName(reservation.getPerson());

        String text = "for <span style='font-weight: bold; font-style: italic;'>" + reservation.getResearchGroup().getTitle() + "</span>";
        text += "<br>to <b>" + BookingRoomUtils.getDate(reservation.getStartTime()) + "<b>";
        text += ", " + BookingRoomUtils.getHoursAndMinutes(reservation.getStartTime()) + " - " + BookingRoomUtils.getHoursAndMinutes(reservation.getEndTime());
        String mail = reservation.getPerson().getEmail();
        String date = BookingRoomUtils.getDate(reservation.getStartTime()) + ", " + BookingRoomUtils.getHoursAndMinutes(reservation.getStartTime()) + " - " + BookingRoomUtils.getHoursAndMinutes(reservation.getEndTime());
        text += "<br><a href='mailto:" + mail + "?subject=Reservation to " + date + "'>" + mail + "</a>";


        Node event = doc.createElement("event");
        NamedNodeMap attributes = event.getAttributes();

        Attr attribute;

        attribute = doc.createAttribute("start");
        attribute.setValue(formatCalendar(reservation.getStartTime()));
        attributes.setNamedItem(attribute);

        attribute = doc.createAttribute("end");
        attribute.setValue(formatCalendar(reservation.getEndTime()));
        attributes.setNamedItem(attribute);

        attribute = doc.createAttribute("title");
        attribute.setValue(title);
        attributes.setNamedItem(attribute);

        attribute = doc.createAttribute("isDuration");
        attribute.setValue("true");
        attributes.setNamedItem(attribute);

        attribute = doc.createAttribute("color");
        attribute.setValue((loggedUser.getUsername().compareTo(reservation.getPerson().getUsername()) == 0) ? MY_RESERVATION_COLOR : RESERVATION_COLOR);
        attributes.setNamedItem(attribute);

        attribute = doc.createAttribute("textColor");
        attribute.setValue("black");
        attributes.setNamedItem(attribute);

        event.setTextContent(text);

        return event;
    }

    private static String formatCalendar(Timestamp stamp)
    {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", new Locale("en", "US"));
        return format.format(stamp) + " GMT";
    }
}

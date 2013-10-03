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
 *   ReservationPDF.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class ReservationPDF extends SimpleFormController
{
    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private HierarchicalMessageSource messageSource;
    private Log log = LogFactory.getLog(getClass());

    public ReservationPDF()
    {
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            if (request.getParameter("type").compareTo("single") == 0)
            {
                int id = Integer.parseInt(request.getParameter("reservationId"));
                Reservation reservation = reservationDao.getReservationById(id);

                response.setHeader("Content-Type", "application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=reservation-" + id + ".pdf");

                Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                PDFUtils utils = new PDFUtils(getServletContext().getRealPath("/"));
                document.add(utils.setHeader(document, "Reservation listing"));

                /*Paragraph paragraph = new Paragraph("Reservation #" + id, FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, BaseColor.BLACK));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                paragraph.setSpacingBefore(10);
                paragraph.setSpacingAfter(10);
                document.add(paragraph);*/

                document.add(formatReservation(reservation));

                document.close();
                response.flushBuffer();

                return null;
            }

            if (request.getParameter("type").compareTo("range") == 0)
            {

                String date = request.getParameter("date") + " 00:00:00";
                GregorianCalendar rangeStart = BookingRoomUtils.getCalendar(date);
                int length = Integer.parseInt(request.getParameter("length"));

                GregorianCalendar rangeEnd = (GregorianCalendar) rangeStart.clone();
                rangeEnd.add(Calendar.DAY_OF_YEAR, length);

                response.setHeader("Content-Type", "application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=reservations.pdf");

                Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                PDFUtils utils = new PDFUtils(getServletContext().getRealPath("/"));
                document.add(utils.setHeader(document, "Reservations listing"));

                java.util.List<Reservation> reservations = reservationDao.getReservationsBetween(rangeStart, rangeEnd, "", 0);

                int count = 0;
                for (Reservation reservation : reservations)
                {
                    document.add(formatReservation(reservation));
                    if (++count % 6 == 0)
                    {
                        document.newPage();
                        document.add(utils.setHeader(document, "Reservations listing"));
                    }
                }
                document.close();
                response.flushBuffer();

                return null;
            }

            return null;
        } catch (Exception e)
        {
            ModelAndView mav = new ModelAndView(getFormView());
            Map data = new HashMap<String, Object>();
            data.put("error", e.getMessage());
            mav.addAllObjects(data);
            return mav;
        }
    }

    public static PdfPTable formatReservation(Reservation reservation)
    {
        int padding = 5;

        GregorianCalendar created = new GregorianCalendar();
        created.setTime(reservation.getCreationTime());
        GregorianCalendar startTime = new GregorianCalendar();
        startTime.setTime(reservation.getStartTime());
        GregorianCalendar endTime = new GregorianCalendar();
        endTime.setTime(reservation.getEndTime());

        String personName = BookingRoomUtils.formatPersonName(reservation.getPerson());

        Font title = FontFactory.getFont("Trebuchet MS", "utf-8", 15, Font.BOLD, new BaseColor(59, 70, 00));
        Font header = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, Font.BOLD, BaseColor.BLACK);
        Font value = FontFactory.getFont(FontFactory.TIMES, 13);

        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        Phrase phrase;

        phrase = new Phrase("Reservation by " + personName);
        phrase.setFont(title);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthLeft(1);
        cell.setBorderWidthRight(1);
        cell.setPadding(padding);
        cell.setPaddingBottom(5);
        table.addCell(cell);

        phrase = new Phrase("Date: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthLeft(1);
        cell.setPadding(padding);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getDate(startTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthRight(1);
        cell.setPadding(padding);
        table.addCell(cell);

        phrase = new Phrase("Start: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthLeft(1);
        cell.setPadding(padding);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getHoursAndMinutes(startTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthRight(1);
        cell.setPadding(padding);
        table.addCell(cell);

        phrase = new Phrase("End: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthLeft(1);
        cell.setBorderWidthBottom(1);
        cell.setPadding(padding);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getHoursAndMinutes(endTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBorderWidthRight(1);
        cell.setBorderWidthBottom(1);
        cell.setPadding(padding);
        table.addCell(cell);

        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        return table;
    }

    public ReservationDao getReservationDao()
    {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao)
    {
        this.reservationDao = reservationDao;
    }

    public ResearchGroupDao getResearchGroupDao()
    {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao)
    {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonDao getPersonDao()
    {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao)
    {
        this.personDao = personDao;
    }

    public HierarchicalMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}

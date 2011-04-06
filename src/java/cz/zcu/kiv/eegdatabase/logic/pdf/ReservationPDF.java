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
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.GregorianCalendar;


public class ReservationPDF extends SimpleFormController
{
    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private HierarchicalMessageSource messageSource;

    public ReservationPDF()
    {
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            int id = Integer.parseInt(request.getParameter("reservationId"));
            Reservation reservation = reservationDao.getReservationById(id);


            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=reservation-" + id + ".pdf");

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            /*PDFUtils utils = new PDFUtils(request.getContextPath());
            document.add(utils.getHeader());*/

            Paragraph paragraph = new Paragraph("Reservation #" + id, FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, BaseColor.BLACK));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(10);
            document.add(paragraph);

            document.add(formatReservation(reservation));

            document.close();
            response.flushBuffer();
            return null;
        } catch (Exception e)
        {
            ModelAndView mav = new ModelAndView(getFormView());
            return mav;
        }
    }

    public static PdfPTable formatReservation(Reservation reservation)
    {
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

        //BookingRoomUtils.getDate(created) + ", " + BookingRoomUtils.getTime(created)
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        Phrase phrase;

        phrase = new Phrase("Reservation by " + personName);
        phrase.setFont(title);
        cell = new PdfPCell(phrase);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(5);
        table.addCell(cell);

        phrase = new Phrase("Date: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getDate(startTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase("Start: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getHoursAndMinutes(startTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase("End: ");
        phrase.setFont(header);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

        phrase = new Phrase(BookingRoomUtils.getHoursAndMinutes(endTime));
        phrase.setFont(value);
        cell = new PdfPCell(phrase);
        cell.setBorder(0);
        table.addCell(cell);

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

package cz.zcu.kiv.eegdatabase.logic.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;

import java.io.IOException;

/**
 * User: Jenda
 * Date: 5.4.11
 * Time: 20:02
 */
public class PDFUtils
{
    String path;

    public PDFUtils(String path)
    {
        this.path = path;
    }

    public Paragraph getHeader()
    {
        return getHeader(null);
    }

    public Paragraph getHeader(String title)
    {
        Paragraph paragraph = new Paragraph("EEG Database");

        try
        {


            Image img = Image.getInstance(this.getClass().getResource("/files/images/header-no-login.gif"));
            img.setAbsolutePosition((PageSize.A4.getWidth() - img.getScaledWidth()) / 2, (PageSize.A4.getHeight() - img.getScaledHeight()));
            paragraph.add(img);

            return paragraph;
        } catch (BadElementException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }


}

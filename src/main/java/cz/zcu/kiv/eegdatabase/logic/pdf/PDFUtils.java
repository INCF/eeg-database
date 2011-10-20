package cz.zcu.kiv.eegdatabase.logic.pdf;

import com.itextpdf.text.*;

import java.io.File;
import java.io.IOException;

/**
 * User: Jenda
 * Date: 5.4.11
 * Time: 20:02
 */
public class PDFUtils
{
    public static final String HEADERIMG = "logo.png";
    public static final String FOOTERIMG = "pdf_footer.png";
    public static final int[] PADDING = {10, 10, 10, 10};

    private String path;


    public PDFUtils(String path)
    {
        this.path = path;
    }

    public Paragraph setHeader(Document document) throws IOException, BadElementException
    {
        return setHeader(document, "EEG Database");
    }

    public Paragraph setHeader(Document document, String title) throws IOException, BadElementException
    {
        Paragraph paragraph = new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK));
        paragraph.setAlignment(Element.ALIGN_CENTER);

        Image img = GetResizedAndCenteredImage(path + convertPath("/files/images/" + HEADERIMG));
        float topMargin = img.getHeight() + 2 * PADDING[0];
        img.setAbsolutePosition(PADDING[3], (PageSize.A4.getHeight() - img.getHeight()) - PADDING[0]);
        paragraph.add(img);
        img = GetResizedAndCenteredImage(path + convertPath("/files/images/" + FOOTERIMG));
        img.setAbsolutePosition((PageSize.A4.getWidth() - img.getWidth()) / 2, PADDING[2]);
        paragraph.add(img);
        float bottomMargin = img.getHeight() + 2 * PADDING[2];

        document.setMargins(topMargin, PADDING[1], bottomMargin, PADDING[3]);

        paragraph.setSpacingAfter(1.5f * PADDING[0]);

        return paragraph;
    }

    private Image GetResizedAndCenteredImage(String filename) throws IOException, BadElementException
    {
        Image img = Image.getInstance(filename);

        if (img.getWidth() <= PageSize.A4.getWidth() - PADDING[1] - PADDING[3])
        {
            return img;
        }

        float newWidth = PageSize.A4.getWidth() - PADDING[1] - PADDING[3];
        float ratio = newWidth / img.getWidth();
        float newHeight = img.getHeight() * ratio;
        img.scaleAbsolute(newWidth, newHeight);

        return img;
    }

    private String convertPath(String origPath)
    {
        return origPath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
    }
}

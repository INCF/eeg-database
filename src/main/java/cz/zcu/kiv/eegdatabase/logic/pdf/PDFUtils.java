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
 *   PDFUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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

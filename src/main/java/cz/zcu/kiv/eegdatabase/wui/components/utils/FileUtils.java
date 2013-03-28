package cz.zcu.kiv.eegdatabase.wui.components.utils;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;

/**
 * Utilities class for files.
 * 
 * @author Jakub Rinkes
 *
 */
public class FileUtils {
    
    /**
     * Prepared request handler for file download.
     * 
     * @param file
     * @return
     */
    public static ResourceStreamRequestHandler prepareDownloadFile(final DataFileDTO file) {

        if (file == null || file.getFileContent() == null)
            return null;

        AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter()
        {
            private static final long serialVersionUID = 1L;

            public void write(OutputStream output) throws IOException
            {
                output.write(file.getFileContent());
            }
        };

        return new ResourceStreamRequestHandler(stream).setFileName(file.getFileName());
    }
}

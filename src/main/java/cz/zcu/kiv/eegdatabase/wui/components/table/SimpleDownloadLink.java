package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

public class SimpleDownloadLink extends Link<Void> {

    private static final long serialVersionUID = -2130280618030423577L;

    @SpringBean
    FileFacade facade;

    private int fileId;

    public SimpleDownloadLink(String id, int fileId) {
        super(id);
        this.fileId = fileId;
        Injector.get().inject(this);
    }

    @Override
    public void onClick() {

        final DataFileDTO file = getFile();

        if (file == null)
            return;

        AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter()
        {
            private static final long serialVersionUID = 1L;

            public void write(OutputStream output) throws IOException
            {
                output.write(file.getFileContent());
            }
        };

        getRequestCycle().scheduleRequestHandlerAfterCurrent(new
                ResourceStreamRequestHandler(stream).setFileName(file.getFileName()));

    }

    private DataFileDTO getFile() {
        return facade.getFile(fileId);
    }

}

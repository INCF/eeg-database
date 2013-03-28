package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

/**
 * Simple enhancement link for download file. In constructor is added file id and link get file after click.
 * 
 * @author Jakub Rinkes
 * 
 */
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

        getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(file));
    }

    private DataFileDTO getFile() {
        return facade.getFile(fileId);
    }

}

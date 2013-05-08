package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import java.io.File;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 20:23
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentResultsDTO extends IdentifiDTO implements Serializable {
    private FileUpload resultFile;

    public FileUpload getResultFile() {
        return resultFile;
    }

    public void setResultFile(FileUpload resultFile) {
        this.resultFile = resultFile;
    }

}
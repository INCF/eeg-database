package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.logic.zip.ZipGenerator;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileService;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonService;

public class ExperimentDownloadProvider {

    protected Log log = LogFactory.getLog(getClass());

    ExperimentsService service;

    PersonService personService;

    FileService fileService;

    ZipGenerator zipGenerator;

    @Required
    public void setService(ExperimentsService service) {
        this.service = service;
    }

    @Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Required
    public void setZipGenerator(ZipGenerator zipGenerator) {
        this.zipGenerator = zipGenerator;
    }

    @Required
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Transactional
    public DataFileDTO generate(Experiment exp, MetadataCommand mc, Collection<DataFile> files, Map<Integer, Set<FileMetadataParamVal>> params) {
        try {

            Experiment experiment = service.getExperimentForDetail(exp.getExperimentId());
            String scenarioName = experiment.getScenario().getTitle();

            Set<DataFile> newFiles = new HashSet<DataFile>();

            if (files != null || params != null) {
                // list selected files and prepare new files which we use for generated zip file.
                for (DataFile item : files) {
                    // fill new file from selected file
                    DataFile newItem = new DataFile();
                    newItem.setDataFileId(item.getDataFileId());
                    newItem.setExperiment(item.getExperiment());
                    newItem.setFileContent(fileService.read(item.getDataFileId()).getFileContent());
                    newItem.setFilename(item.getFilename());
                    newItem.setMimetype(item.getMimetype());
                    newItem.setDescription(item.getDescription());

                    // create list of parameters which we use for generated zip file
                    Set<FileMetadataParamVal> newVals = new HashSet<FileMetadataParamVal>();
                    // get from map of selected parameters collection for actual file
                    Set<FileMetadataParamVal> list = params.get(item.getDataFileId());
                    for (FileMetadataParamVal paramVal : list) {
                        newVals.add(paramVal);
                    }

                    newItem.setFileMetadataParamVals(newVals);
                    newFiles.add(newItem);
                }
            }

            Person user = personService.getLoggedPerson();
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            History history = new History();
            log.debug("Setting downloading metadata");
            history.setExperiment(experiment);
            log.debug("Setting user");
            history.setPerson(user);
            log.debug("Setting time of download");
            history.setDateOfDownload(currentTimestamp);
            log.debug("Saving download history");
            // TODO add missing history creation
            // historyDao.create(history);

            log.error("files count " + newFiles.size());
            ByteArrayOutputStream stream = (ByteArrayOutputStream) zipGenerator.generate(experiment, mc, newFiles);

            DataFileDTO dto = new DataFileDTO();
            dto.setFileContent(stream.toByteArray());
            if (scenarioName != null)
                dto.setFileName(scenarioName.replaceAll("\\s", "_") + ".zip");
            else
                dto.setFileName("Experiment_data_" + exp.getExperimentId() + ".zip");

            return dto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }
}

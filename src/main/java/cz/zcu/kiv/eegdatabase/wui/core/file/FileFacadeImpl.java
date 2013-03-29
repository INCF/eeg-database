package cz.zcu.kiv.eegdatabase.wui.core.file;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

public class FileFacadeImpl implements FileFacade {
    
    protected Log log = LogFactory.getLog(getClass());

    FileService fileService;
    
    @Required
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    
    @Override
    public Integer create(DataFile newInstance) {
        return fileService.create(newInstance);
    }

    @Override
    public DataFile read(Integer id) {
        return fileService.read(id);
    }

    @Override
    public List<DataFile> readByParameter(String parameterName, int parameterValue) {
        return fileService.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<DataFile> readByParameter(String parameterName, String parameterValue) {
        return fileService.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(DataFile transientObject) {
        fileService.update(transientObject);
    }

    @Override
    public void delete(DataFile persistentObject) {
        fileService.delete(persistentObject);
    }

    @Override
    public List<DataFile> getAllRecords() {
        return fileService.getAllRecords();
    }

    @Override
    public List<DataFile> getRecordsAtSides(int first, int max) {
        return fileService.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return fileService.getCountRecords();
    }

    @Override
    public Blob createBlob(byte[] input) {
        return fileService.createBlob(input);
    }

    @Override
    public Blob createBlob(InputStream input, int length) {
        return fileService.createBlob(input, length);
    }
    
    /**
     * Method prepared file with file Id. File data is copied from database into byte array inside DataFileDTO.
     */
    @Override
    public DataFileDTO getFile(int fileId) {
        return fileService.getFile(fileId);
    }
}

package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.DataTransformer;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.MethodParameters;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.ProcessService;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.SupportedFormat;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingServiceImpl
        implements SignalProcessingService {

    protected Log log = LogFactory.getLog(getClass());
    @Autowired
    private ExperimentDao experimentDao;
    @Autowired
    private DataTransformer transformer;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ServiceResultDao resultDao;
    private ProcessService eegService;

    @Override
    public List<String> getAvailableMethods() {

        return eegService.getAvailableMethods();
    }

    @Transactional
    @Override
    public List<String> getSuitableHeaders(int experimentId) {
        Experiment e = experimentDao.read(experimentId);
        return SignalProcessingUtils.getHeaders(e);
    }

    @Override
    public List<MethodParameters> getMethodParameters(String methodName) {
        return eegService.getMethodParameters(SupportedFormat.KIV_FORMAT, methodName);
    }

    @Override
    @Transactional
    public List<ChannelInfo> getChannelInfo(int experimentId, String header) throws SQLException {
        readHeader(experimentId, header);
        return transformer.getChannelInfo();
    }

    @Override
    @Transactional
    public List<cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile>
    getDataFiles(int experimentId, String header) {
        List<cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile> dataFiles =
                new ArrayList<cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile>();
        readHeader(experimentId, header);
        String dataFileName = transformer.getProperties().get("CI").get("DataFile");
        dataFileName = dataFileName.replace(" ", "_");
        cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile headerFile = new
                cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile();
        cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile dataFile = new
                cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile();
        Experiment e = experimentDao.read(experimentId);
        for (DataFile file : e.getDataFiles()) {
            if (file.getFilename().equals(header + ".vhdr")) {
                headerFile.setFileName(header + ".vhdr");
                try {
                    headerFile.setFile(file.getFileContent().getBytes(1, (int)file.getFileContent().length()));
                }catch (SQLException ex)
                {
                    Logger.getLogger(SignalProcessingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);

                }
                dataFiles.add(headerFile);
            }
            if (file.getFilename().equals(dataFileName)) {
                dataFile.setFileName(dataFileName);
                try {
                    dataFile.setFile(file.getFileContent().getBytes(1, (int)file.getFileContent().length()));
                }catch (SQLException ex)
                {
                    Logger.getLogger(SignalProcessingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);

                }
                dataFiles.add(dataFile);
            }
        }

        return dataFiles;
    }

    @Override
    public byte[] processService(List<cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile> files,
                                 SupportedFormat format, String methodName, List<String> params) {
        return eegService.processData(files, format, methodName, params);
    }

    @Override
    @Transactional
    public Person getLoggedPerson() {
        return personDao.getLoggedPerson();
    }

    @Override
    @Transactional
    public void update(ServiceResult result) {
        resultDao.update(result);
    }

    @Override
    @Transactional
    public void delete(ServiceResult persistentObject) {
        resultDao.delete(persistentObject);
    }

    @Override
    @Transactional
    public List<ServiceResult> getAllRecords() {
        return resultDao.getAllRecords();
    }

    @Override
    @Transactional
    public List<ServiceResult> getRecordsAtSides(int first, int max) {
        return resultDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional
    public int getCountRecords() {
        return resultDao.getCountRecords();
    }

    @Override
    @Transactional
    public List<ServiceResult> getUnique(ServiceResult example) {
        return resultDao.findByExample(example);
    }

    @Override
    @Transactional
    public Integer create(ServiceResult result) {
        return resultDao.create(result);
    }



    @Override
    @Transactional
    public List<ServiceResult> getResults(Person person) {
        return resultDao.getResultByPerson(person.getPersonId());
    }

    @Override
    @Transactional
    public ServiceResult read(Integer resultId) {
        return resultDao.read(resultId);
    }

    @Override
    @Transactional
    public List<ServiceResult> readByParameter(String parameterName, Object parameterValue) {
        return resultDao.readByParameter(parameterName, parameterValue);
    }

    @Transactional
    private void readHeader(int experimentId, String header) {
        if (transformer.isHeaderRed()) {
            return;
        }
        Experiment e = experimentDao.read(experimentId);
        for (DataFile file : e.getDataFiles()) {
            if (file.getFilename().equals(header + ".vhdr")) {
                try {
                    transformer.readVhdr(file.getFileContent().getBytes(1, (int)file.getFileContent().length()));
                } catch (SQLException ex) {
                    Logger.getLogger(SignalProcessingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                return;
            }
        }

    }

    @Override
    @Transactional(readOnly = true)
    public FileDTO getResultFile(int resultId) {
        ServiceResult result = resultDao.read(resultId);
        try {
            FileDTO dto = new FileDTO();

            File tmpFile;
            tmpFile = File.createTempFile("result", ".xml");
            tmpFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tmpFile);
            IOUtils.copy(result.getContent().getBinaryStream(), out);
            out.close();

            dto.setFileName(result.getFilename());
            dto.setFile(tmpFile);

            return dto;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    @Override
    public Blob createBlob(byte[] content) {
        return resultDao.createBlob(content);
    }

    public ProcessService getEegService() {
        return eegService;
    }

    public void setEegService(ProcessService eegService) {
        this.eegService = eegService;
    }

}
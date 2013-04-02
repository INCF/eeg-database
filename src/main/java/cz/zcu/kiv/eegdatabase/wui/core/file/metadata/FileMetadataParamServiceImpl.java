package cz.zcu.kiv.eegdatabase.wui.core.file.metadata;

import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class FileMetadataParamServiceImpl implements FileMetadataParamService {

    protected Log log = LogFactory.getLog(getClass());
    
    FileMetadataParamDefDao dao;
    
    GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataParamValDao;

    @Required
    public void setDao(FileMetadataParamDefDao dao) {
        this.dao = dao;
    }
    
    public void setFileMetadataParamValDao(GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataParamValDao) {
        this.fileMetadataParamValDao = fileMetadataParamValDao;
    }
    
    @Override
    @Transactional
    public Integer create(FileMetadataParamDef newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public FileMetadataParamDef read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(FileMetadataParamDef transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(FileMetadataParamDef persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> getRecordsAtSides(int first, int max) {
        return getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    public List<FileMetadataParamDef> getUnique(FileMetadataParamDef example) {
        return dao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> getItemsForList() {
        return dao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return dao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> getRecordsByGroup(int groupId) {
        return dao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional
    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef) {
        dao.createDefaultRecord(fileMetadataParamDef);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamDef> getDefaultRecords() {
        return dao.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return dao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        dao.deleteGroupRel(fileMetadataParamDefGroupRel);
    }

    @Override
    @Transactional(readOnly = true)
    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId) {
        return dao.getGroupRel(fileMetadataParamDefId, researchGroupId);
    }

    @Override
    @Transactional
    public void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        dao.createGroupRel(fileMetadataParamDefGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup) {
        dao.createGroupRel(fileMetadataParamDef, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return dao.isDefault(id);
    }

    @Override
    @Transactional
    public FileMetadataParamValId create(FileMetadataParamVal newInstance) {
        return fileMetadataParamValDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public FileMetadataParamVal read(FileMetadataParamValId id) {
        return fileMetadataParamValDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamVal> readValueByParameter(String parameterName, int parameterValue) {
        return fileMetadataParamValDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamVal> readValueByParameter(String parameterName, String parameterValue) {
        return fileMetadataParamValDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(FileMetadataParamVal transientObject) {
        fileMetadataParamValDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(FileMetadataParamVal persistentObject) {
        fileMetadataParamValDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamVal> getAllValueRecords() {
        return fileMetadataParamValDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataParamVal> getValueRecordsAtSides(int first, int max) {
        return fileMetadataParamValDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getValueCountRecords() {
        return fileMetadataParamValDao.getCountRecords();
    }

}

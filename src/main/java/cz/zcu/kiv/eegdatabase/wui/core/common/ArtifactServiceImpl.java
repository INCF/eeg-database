package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ArtifactServiceImpl implements ArtifactService {

    protected Log log = LogFactory.getLog(getClass());

    GenericListDao<Artifact> artifactDao;

    public void setArtifactDao(GenericListDao<Artifact> artifactDao) {
        this.artifactDao = artifactDao;
    }

    @Override
    @Transactional
    public Integer create(Artifact newInstance) {
        return artifactDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Artifact read(Integer id) {
        return artifactDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artifact> readByParameter(String parameterName, Object parameterValue) {
        return artifactDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Artifact transientObject) {
        artifactDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Artifact persistentObject) {
        artifactDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artifact> getAllRecords() {
        return artifactDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artifact> getRecordsAtSides(int first, int max) {
        return artifactDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return artifactDao.getCountRecords();
    }

    @Override
    public List<Artifact> getUnique(Artifact example) {
        return artifactDao.findByExample(example);
    }

    @Override
    @Transactional
    public void createGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        artifactDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artifact> getItemsForList() {
        return artifactDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artifact> getRecordsByGroup(int groupId) {
        return artifactDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return artifactDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return artifactDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        artifactDao.deleteGroupRel(persistent, researchGroup);
    }

}

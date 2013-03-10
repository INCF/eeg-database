package cz.zcu.kiv.eegdatabase.wui.core.experiments.param;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ExperimentsOptParamServiceImpl implements ExperimentsOptParamService {

    protected Log log = LogFactory.getLog(getClass());

    ExperimentOptParamDefDao dao;

    @Required
    public void setDao(ExperimentOptParamDefDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Integer create(ExperimentOptParamDef newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentOptParamDef read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> readByParameter(String parameterName, int parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> readByParameter(String parameterName, String parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(ExperimentOptParamDef transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ExperimentOptParamDef persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getItemsForList() {
        return dao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return dao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getRecordsByGroup(int groupId) {
        return dao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional
    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef) {
        dao.createDefaultRecord(experimentOptParamDef);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getDefaultRecords() {
        return dao.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return dao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        dao.deleteGroupRel(experimentOptParamDefGroupRel);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId) {
        return dao.getGroupRel(experimentOptParamDefId, researchGroupId);
    }

    @Override
    @Transactional
    public void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        dao.createGroupRel(experimentOptParamDefGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup) {
        dao.createGroupRel(experimentOptParamDef, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return dao.isDefault(id);
    }

}

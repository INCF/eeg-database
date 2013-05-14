package cz.zcu.kiv.eegdatabase.wui.core.person.param;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PersonOptParamServiceImpl implements PersonOptParamService {
    
    protected Log log = LogFactory.getLog(getClass());
    
    PersonOptParamDefDao defDAO;
    
    GenericDao<PersonOptParamVal, PersonOptParamValId> valDAO;
    
    @Required
    public void setDefDAO(PersonOptParamDefDao defDAO) {
        this.defDAO = defDAO;
    }
    
    public void setValDAO(GenericDao<PersonOptParamVal, PersonOptParamValId> valDAO) {
        this.valDAO = valDAO;
    }
    
    @Override
    @Transactional
    public Integer create(PersonOptParamDef newInstance) {
        return defDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly=true)
    public PersonOptParamDef read(Integer id) {
        return defDAO.read(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> readByParameter(String parameterName, int parameterValue) {
        return defDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> readByParameter(String parameterName, String parameterValue) {
        return defDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(PersonOptParamDef transientObject) {
        defDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(PersonOptParamDef persistentObject) {
        defDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> getAllRecords() {
        return defDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> getRecordsAtSides(int first, int max) {
        return defDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly=true)
    public int getCountRecords() {
        return defDAO.getCountRecords();
    }

    @Override
    public List<PersonOptParamDef> getUnique(PersonOptParamDef example) {
        return defDAO.findByExample(example);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> getItemsForList() {
        return defDAO.getItemsForList();
    }

    @Override
    @Transactional(readOnly=true)
    public boolean canDelete(int id) {
        return defDAO.canDelete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> getRecordsByGroup(int groupId) {
        return defDAO.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional
    public void createDefaultRecord(PersonOptParamDef personOptParamDef) {
        defDAO.createDefaultRecord(personOptParamDef);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamDef> getDefaultRecords() {
        return defDAO.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly=true)
    public boolean hasGroupRel(int id) {
        return defDAO.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        defDAO.deleteGroupRel(personOptParamDefGroupRel);
    }

    @Override
    @Transactional(readOnly=true)
    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId) {
        return defDAO.getGroupRel(personOptParamDefId, researchGroupId);
    }

    @Override
    @Transactional
    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        defDAO.createGroupRel(personOptParamDefGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup) {
        defDAO.createGroupRel(personOptParamDef, researchGroup);
    }

    @Override
    @Transactional(readOnly=true)
    public boolean isDefault(int id) {
        return defDAO.isDefault(id);
    }

    @Override
    @Transactional
    public PersonOptParamValId create(PersonOptParamVal newInstance) {
        return valDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly=true)
    public PersonOptParamVal read(PersonOptParamValId id) {
        return valDAO.read(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamVal> readValueByParameter(String parameterName, int parameterValue) {
        return valDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamVal> readValueByParameter(String parameterName, String parameterValue) {
        return valDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(PersonOptParamVal transientObject) {
        valDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(PersonOptParamVal persistentObject) {
        valDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamVal> getAllValueRecords() {
        return valDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly=true)
    public List<PersonOptParamVal> getValueRecordsAtSides(int first, int max) {
        return valDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly=true)
    public int getValueCountRecords() {
        return valDAO.getCountRecords();
    }
}

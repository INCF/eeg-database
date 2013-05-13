package cz.zcu.kiv.eegdatabase.wui.core.person.param;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PersonOptParamFacadeImpl implements PersonOptParamFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    PersonOptParamService service;
    
    @Required
    public void setService(PersonOptParamService service) {
        this.service = service;
    }

    @Override
    public Integer create(PersonOptParamDef newInstance) {
        return service.create(newInstance);
    }

    @Override
    public PersonOptParamDef read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<PersonOptParamDef> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<PersonOptParamDef> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(PersonOptParamDef transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(PersonOptParamDef persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<PersonOptParamDef> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<PersonOptParamDef> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<PersonOptParamDef> getUnique(PersonOptParamDef example) {
        return service.getUnique(example);
    }

    @Override
    public List<PersonOptParamDef> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public List<PersonOptParamDef> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public void createDefaultRecord(PersonOptParamDef personOptParamDef) {
        service.createDefaultRecord(personOptParamDef);
    }

    @Override
    public List<PersonOptParamDef> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        service.deleteGroupRel(personOptParamDefGroupRel);
    }

    @Override
    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId) {
        return service.getGroupRel(personOptParamDefId, researchGroupId);
    }

    @Override
    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        service.createGroupRel(personOptParamDefGroupRel);
    }

    @Override
    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup) {
        service.createGroupRel(personOptParamDef, researchGroup);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public PersonOptParamValId create(PersonOptParamVal newInstance) {
        return service.create(newInstance);
    }

    @Override
    public PersonOptParamVal read(PersonOptParamValId id) {
        return service.read(id);
    }

    @Override
    public List<PersonOptParamVal> readValueByParameter(String parameterName, int parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public List<PersonOptParamVal> readValueByParameter(String parameterName, String parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(PersonOptParamVal transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(PersonOptParamVal persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<PersonOptParamVal> getAllValueRecords() {
        return service.getAllValueRecords();
    }

    @Override
    public List<PersonOptParamVal> getValueRecordsAtSides(int first, int max) {
        return service.getValueRecordsAtSides(first, max);
    }

    @Override
    public int getValueCountRecords() {
        return service.getValueCountRecords();
    }

}

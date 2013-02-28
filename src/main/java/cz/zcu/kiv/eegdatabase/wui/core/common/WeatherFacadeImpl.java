package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;

public class WeatherFacadeImpl implements WeatherFacade {

    protected Log log = LogFactory.getLog(getClass());

    WeatherService service;

    @Required
    public void setService(WeatherService service) {
        this.service = service;
    }

    @Override
    public Integer create(Weather newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Weather read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Weather> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Weather> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Weather transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Weather persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Weather> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Weather> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Weather> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Weather> getRecordsNewerThan(long oracleScn) {
        return service.getRecordsNewerThan(oracleScn);
    }

    @Override
    public boolean canSaveDescription(String description, int groupId, int weatherId) {
        return service.canSaveDescription(description, groupId, weatherId);
    }

    @Override
    public boolean canSaveDefaultDescription(String description, int weatherId) {
        return service.canSaveDefaultDescription(description, weatherId);
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public boolean canSaveNewDescription(String description, int groupId) {
        return service.canSaveNewDescription(description, groupId);
    }

    @Override
    public void createDefaultRecord(Weather weather) {
        service.createDefaultRecord(weather);
    }

    @Override
    public void createGroupRel(WeatherGroupRel weatherGroupRel) {
        service.createGroupRel(weatherGroupRel);
    }

    @Override
    public void createGroupRel(Weather weather, ResearchGroup researchGroup) {
        service.createGroupRel(weather, researchGroup);
    }

    @Override
    public List<Weather> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public List<Weather> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public void deleteGroupRel(WeatherGroupRel weatherGroupRel) {
        service.deleteGroupRel(weatherGroupRel);
    }

    @Override
    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId) {
        return service.getGroupRel(weatherId, researchGroupId);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int weatherId) {
        return service.canSaveTitle(title, groupId, weatherId);
    }

    @Override
    public boolean canSaveNewTitle(String title, int groupId) {
        return service.canSaveNewTitle(title, groupId);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int weatherId) {
        return service.canSaveDefaultTitle(title, weatherId);
    }
}

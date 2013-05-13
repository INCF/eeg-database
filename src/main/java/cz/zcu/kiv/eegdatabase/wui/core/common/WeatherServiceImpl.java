package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class WeatherServiceImpl implements WeatherService {

    protected Log log = LogFactory.getLog(getClass());

    WeatherDao weatherDAO;

    @Required
    public void setWeatherDAO(WeatherDao weatherDAO) {
        this.weatherDAO = weatherDAO;
    }

    @Override
    @Transactional
    public Integer create(Weather newInstance) {
        return weatherDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Weather read(Integer id) {
        return weatherDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> readByParameter(String parameterName, int parameterValue) {
        return weatherDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> readByParameter(String parameterName, String parameterValue) {
        return weatherDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Weather transientObject) {
        weatherDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Weather persistentObject) {
        weatherDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getAllRecords() {
        return weatherDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getRecordsAtSides(int first, int max) {
        return weatherDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return weatherDAO.getCountRecords();
    }

    @Override
    public List<Weather> getUnique(Weather example) {
        return weatherDAO.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getItemsForList() {
        return weatherDAO.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getRecordsNewerThan(long oracleScn) {
        return weatherDAO.getRecordsNewerThan(oracleScn);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDescription(String description, int groupId, int weatherId) {
        return weatherDAO.canSaveDescription(description, groupId, weatherId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultDescription(String description, int weatherId) {
        return weatherDAO.canSaveDefaultDescription(description, weatherId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return weatherDAO.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveNewDescription(String description, int groupId) {
        return weatherDAO.canSaveNewDescription(description, groupId);
    }

    @Override
    @Transactional
    public void createDefaultRecord(Weather weather) {
        weatherDAO.createDefaultRecord(weather);
    }

    @Override
    @Transactional
    public void createGroupRel(WeatherGroupRel weatherGroupRel) {
        weatherDAO.createGroupRel(weatherGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(Weather weather, ResearchGroup researchGroup) {
        weatherDAO.createGroupRel(weather, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getRecordsByGroup(int groupId) {
        return weatherDAO.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weather> getDefaultRecords() {
        return weatherDAO.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return weatherDAO.isDefault(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(WeatherGroupRel weatherGroupRel) {
        weatherDAO.deleteGroupRel(weatherGroupRel);
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId) {
        return weatherDAO.getGroupRel(weatherId, researchGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return weatherDAO.hasGroupRel(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int groupId, int weatherId) {
        return weatherDAO.canSaveTitle(title, groupId, weatherId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveNewTitle(String title, int groupId) {
        return weatherDAO.canSaveNewTitle(title, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultTitle(String title, int weatherId) {
        return weatherDAO.canSaveDefaultTitle(title, weatherId);
    }

}

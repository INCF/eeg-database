package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class HardwareServiceImpl implements HardwareService {

    protected Log log = LogFactory.getLog(getClass());

    HardwareDao hardwareDao;

    @Required
    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }

    @Override
    @Transactional
    public Integer create(Hardware newInstance) {
        return hardwareDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Hardware read(Integer id) {
        return hardwareDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> readByParameter(String parameterName, int parameterValue) {
        return hardwareDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> readByParameter(String parameterName, String parameterValue) {
        return hardwareDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Hardware transientObject) {
        hardwareDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Hardware persistentObject) {
        hardwareDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getAllRecords() {
        return hardwareDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getRecordsAtSides(int first, int max) {
        return hardwareDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return hardwareDao.getCountRecords();
    }

    @Override
    @Transactional
    public void createDefaultRecord(Hardware hardware) {
        hardwareDao.createDefaultRecord(hardware);
    }

    @Override
    @Transactional
    public void createGroupRel(HardwareGroupRel hardwareGroupRel) {
        hardwareDao.createGroupRel(hardwareGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup) {
        hardwareDao.createGroupRel(hardware, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getItemsForList() {
        return hardwareDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getRecordsNewerThan(long oracleScn) {
        return hardwareDao.getRecordsNewerThan(oracleScn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getRecordsByGroup(int groupId) {
        return hardwareDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hardware> getDefaultRecords() {
        return hardwareDao.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return hardwareDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return hardwareDao.isDefault(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel) {
        hardwareDao.deleteGroupRel(hardwareGroupRel);
    }

    @Override
    @Transactional(readOnly = true)
    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId) {
        return hardwareDao.getGroupRel(hardwareId, researchGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return hardwareDao.hasGroupRel(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int groupId, int hwId) {
        return hardwareDao.canSaveTitle(title, groupId, hwId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveNewTitle(String title, int groupId) {
        return hardwareDao.canSaveNewTitle(title, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultTitle(String title, int hwId) {
        return hardwareDao.canSaveDefaultTitle(title, hwId);
    }
}

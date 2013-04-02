package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class HardwareFacadeImpl implements HardwareFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    HardwareService service;
    
    @Required
    public void setService(HardwareService service) {
        this.service = service;
    }

    @Override
    public Integer create(Hardware newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Hardware read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Hardware> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Hardware transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Hardware persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Hardware> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Hardware> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Hardware> getUnique(Hardware example) {
        return service.getUnique(example);
    }

    @Override
    public void createDefaultRecord(Hardware hardware) {
        service.createDefaultRecord(hardware);
    }

    @Override
    public void createGroupRel(HardwareGroupRel hardwareGroupRel) {
        service.createGroupRel(hardwareGroupRel);
    }

    @Override
    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup) {
        service.createGroupRel(hardware, researchGroup);
    }

    @Override
    public List<Hardware> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Hardware> getRecordsNewerThan(long oracleScn) {
        return service.getRecordsNewerThan(oracleScn);
    }

    @Override
    public List<Hardware> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public List<Hardware> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel) {
        service.deleteGroupRel(hardwareGroupRel);
    }

    @Override
    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId) {
        return service.getGroupRel(hardwareId, researchGroupId);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int hwId) {
        return service.canSaveTitle(title, groupId, hwId);
    }

    @Override
    public boolean canSaveNewTitle(String title, int groupId) {
        return service.canSaveNewTitle(title, groupId);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int hwId) {
        return service.canSaveDefaultTitle(title, hwId);
    }
}

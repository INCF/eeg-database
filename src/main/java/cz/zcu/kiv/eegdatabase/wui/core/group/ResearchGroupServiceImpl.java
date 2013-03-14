package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.Util;

public class ResearchGroupServiceImpl implements ResearchGroupService {

    protected Log log = LogFactory.getLog(getClass());

    ResearchGroupDao researchGroupDAO;
    PersonDao personDAO;
    GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao;
    
    HardwareDao hardwareDao;
    GenericListDao<Artifact> artifactDao;
    WeatherDao weatherDao;
    ExperimentOptParamDefDao experiemntParamDefDao;
    FileMetadataParamDefDao fileMetadataParamDefDao;
    PersonOptParamDefDao personOptParamDefDao;
    
    @Required
    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }
    
    @Required
    public void setFileMetadataParamDefDao(FileMetadataParamDefDao fileMetadataParamDefDao) {
        this.fileMetadataParamDefDao = fileMetadataParamDefDao;
    }
    
    @Required
    public void setExperiemntParamDefDao(ExperimentOptParamDefDao experiemntParamDefDao) {
        this.experiemntParamDefDao = experiemntParamDefDao;
    }

    @Required
    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }
    
    @Required
    public void setArtifactDao(GenericListDao<Artifact> artifactDao) {
        this.artifactDao = artifactDao;
    }

    @Required
    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }

    @Required
    public void setResearchGroupDAO(ResearchGroupDao researchGroupDAO) {
        this.researchGroupDAO = researchGroupDAO;
    }

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    public void setMembershipDao(GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao) {
        this.membershipDao = membershipDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {

        return researchGroupDAO.getResearchGroupsWhereMember(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {

        return researchGroupDAO.getResearchGroupsWhereMember(person, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person) {

        return researchGroupDAO.getResearchGroupsWhereOwner(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person) {
        return researchGroupDAO.getResearchGroupsWhereAbleToWriteInto(person);
    }

    @Override
    @Transactional(readOnly = true)
    public String getResearchGroupTitle(int groupId) {
        return researchGroupDAO.getResearchGroupTitle(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person) {
        return researchGroupDAO.getGroupDataForAccountOverview(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person) {
        return researchGroupDAO.getResearchGroupsWhereUserIsGroupAdmin(person);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int id) {
        return researchGroupDAO.canSaveTitle(title, id);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForList() {
        return researchGroupDAO.getCountForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getGroupsForList(int start, int limit) {
        return researchGroupDAO.getGroupsForList(start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List getListOfGroupMembers(int groupId) {
        return researchGroupDAO.getListOfGroupMembers(groupId);
    }

    @Override
    @Transactional
    public Integer create(ResearchGroup group) {

        log.debug("Saving new ResearchGroup object.");
        Integer groupId = researchGroupDAO.create(group);

        log.debug("Assigning user to the group as member with level GROUP_ADMIN.");
        ResearchGroupMembership membership = new ResearchGroupMembership();
        membership.setAuthority(Util.GROUP_ADMIN);
        membership.setId(new ResearchGroupMembershipId(group.getPerson().getPersonId(), groupId));
        log.debug("Saving membership relation.");
        membershipDao.create(membership);

        log.debug("Creating of research group done.");
        
        preparedDefaultListsForNewGroup(group);

        return groupId;
    }

    @Override
    @Transactional
    public void delete(ResearchGroup group) {
        researchGroupDAO.delete(researchGroupDAO.read(group.getResearchGroupId()));
    }

    @Override
    @Transactional
    public void update(ResearchGroup group) {
        researchGroupDAO.update(group);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroup getResearchGroupById(int id) {
        return researchGroupDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroup read(Integer id) {
        return researchGroupDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> readByParameter(String parameterName, int parameterValue) {
        return researchGroupDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> readByParameter(String parameterName, String parameterValue) {
        return researchGroupDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getAllRecords() {
        return researchGroupDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getRecordsAtSides(int first, int max) {
        return researchGroupDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return researchGroupDAO.getCountRecords();
    }

    @Override
    @Transactional
    public ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance) {
        return membershipDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id) {
        return membershipDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue) {
        return membershipDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue) {
        return membershipDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void updateMemberhip(ResearchGroupMembership transientObject) {
        membershipDao.update(transientObject);
    }

    @Override
    @Transactional
    public void deleteMemberhip(ResearchGroupMembership persistentObject) {
        membershipDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> getAllMemberhipRecords() {
        return membershipDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max) {
        return membershipDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountMemberhipRecords() {
        return membershipDao.getCountRecords();
    }
    
    private void preparedDefaultListsForNewGroup(ResearchGroup researchGroup){
        List<Hardware> hardwareList = hardwareDao.getDefaultRecords();
        Hardware hardware;
        Weather weather;
        PersonOptParamDef personOptParamDef;
        FileMetadataParamDef fileMetadataParamDef;
        ExperimentOptParamDef experimentOptParamDef;

        for(int i = 0;i< hardwareList.size();i++){
            hardware = new Hardware();
            hardware.setDefaultNumber(0);
            hardware.setDescription(hardwareList.get(i).getDescription());
            hardware.setTitle(hardwareList.get(i).getTitle());
            hardware.setType(hardwareList.get(i).getType());
            hardwareDao.create(hardware);
            hardwareDao.createGroupRel(hardware,researchGroup);
        }
        List<Weather> weatherList = weatherDao.getDefaultRecords();
        for(int i = 0;i< weatherList.size();i++){
            weather = new Weather();
            weather.setDefaultNumber(0);
            weather.setDescription(weatherList.get(i).getDescription());
            weather.setTitle(weatherList.get(i).getTitle());
            weatherDao.create(weather);
            weatherDao.createGroupRel(weather,researchGroup);
        }

        List<PersonOptParamDef> personOptParamDefList = personOptParamDefDao.getDefaultRecords();
        for(int i = 0;i< personOptParamDefList.size();i++){
            personOptParamDef = new PersonOptParamDef();
            personOptParamDef.setDefaultNumber(0);
            personOptParamDef.setParamDataType(personOptParamDefList.get(i).getParamDataType());
            personOptParamDef.setParamName(personOptParamDefList.get(i).getParamName());
            personOptParamDefDao.create(personOptParamDef);
            personOptParamDefDao.createGroupRel(personOptParamDef,researchGroup);
        }
        List<FileMetadataParamDef> fileMetadataParamDefList = fileMetadataParamDefDao.getDefaultRecords();
        for(int i = 0;i< fileMetadataParamDefList.size();i++){
            fileMetadataParamDef = new FileMetadataParamDef();
            fileMetadataParamDef.setDefaultNumber(0);
            fileMetadataParamDef.setParamDataType(fileMetadataParamDefList.get(i).getParamDataType());
            fileMetadataParamDef.setParamName(fileMetadataParamDefList.get(i).getParamName());
            fileMetadataParamDefDao.create(fileMetadataParamDef);
            fileMetadataParamDefDao.createGroupRel(fileMetadataParamDef,researchGroup);
        }
        List<ExperimentOptParamDef> experimentOptParamDefList = experiemntParamDefDao.getDefaultRecords();
        for(int i = 0;i< experimentOptParamDefList.size();i++){
            experimentOptParamDef = new ExperimentOptParamDef();
            experimentOptParamDef.setDefaultNumber(0);
            experimentOptParamDef.setParamDataType(experimentOptParamDefList.get(i).getParamDataType());
            experimentOptParamDef.setParamName(experimentOptParamDefList.get(i).getParamName());
            experiemntParamDefDao.create(experimentOptParamDef);
            experiemntParamDefDao.createGroupRel(experimentOptParamDef,researchGroup);
        }
    }

}

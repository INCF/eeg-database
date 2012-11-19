package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonMapper;

public class ResearchGroupServiceImpl implements ResearchGroupService {

    ResearchGroupDao researchGroupDAO;
    PersonDao personDAO;
    EducationLevelDao educationLevelDao;

    ResearchGroupMapper researchMapper = new ResearchGroupMapper();
    PersonMapper personMapper = new PersonMapper();

    @Required
    public void setResearchGroupDAO(ResearchGroupDao researchGroupDAO) {
        this.researchGroupDAO = researchGroupDAO;
    }

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    @Required
    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person) {

        List<ResearchGroup> entityList = researchGroupDAO.getResearchGroupsWhereMember(personDAO.getPerson(person.getEmail()));
        List<ResearchGroupDTO> list = new ArrayList<ResearchGroupDTO>(entityList.size());

        return convertEntityListToDTOs(entityList, list);
    }

    // TODO this isnt nice and refactor will be nice.
    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person, int limit) {
        List<ResearchGroupDTO> list = getResearchGroupsWhereMember(person);

        return list.subList(0, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupDTO> getResearchGroupsWhereOwner(FullPersonDTO person) {

        List<ResearchGroup> entityList = researchGroupDAO.getResearchGroupsWhereOwner(personDAO.getPerson(person.getEmail()));
        List<ResearchGroupDTO> list = new ArrayList<ResearchGroupDTO>(entityList.size());

        return convertEntityListToDTOs(entityList, list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupDTO> getResearchGroupsWhereAbleToWriteInto(FullPersonDTO person) {
        List<ResearchGroup> entityList = researchGroupDAO.getResearchGroupsWhereAbleToWriteInto(personDAO.getPerson(person.getEmail()));
        List<ResearchGroupDTO> list = new ArrayList<ResearchGroupDTO>(entityList.size());

        return convertEntityListToDTOs(entityList, list);
    }

    @Override
    @Transactional(readOnly = true)
    public String getResearchGroupTitle(int groupId) {
        return researchGroupDAO.getResearchGroupTitle(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(FullPersonDTO person) {
        return researchGroupDAO.getGroupDataForAccountOverview(personDAO.getPerson(person.getEmail()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupDTO> getResearchGroupsWhereUserIsGroupAdmin(FullPersonDTO person) {
        List<ResearchGroup> entityList = researchGroupDAO.getResearchGroupsWhereUserIsGroupAdmin(personDAO.getPerson(person.getEmail()));
        List<ResearchGroupDTO> list = new ArrayList<ResearchGroupDTO>(entityList.size());

        return convertEntityListToDTOs(entityList, list);
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
    public List getGroupsForList(int start, int limit) {
        return researchGroupDAO.getGroupsForList(start, limit);
    }

    @Override
    public List getListOfGroupMembers(int groupId) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<ResearchGroupDTO> convertEntityListToDTOs(List<ResearchGroup> entityList, List<ResearchGroupDTO> list) {
        for (ResearchGroup tmp : entityList) {
            ResearchGroupDTO dto = researchMapper.convertToDTO(tmp);
            dto.setOwner(personMapper.convertToDTO(tmp.getPerson(), educationLevelDao));

            list.add(dto);

        }

        return list;
    }
}

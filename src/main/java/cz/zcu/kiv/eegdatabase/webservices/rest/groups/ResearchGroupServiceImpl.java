package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.*;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Service
public class ResearchGroupServiceImpl implements ResearchGroupService {

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao groupDao;

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupData> getAllGroups() {
        List<ResearchGroup> groups = groupDao.getAllRecords();
        List<ResearchGroupData> list = new ArrayList<ResearchGroupData>();

        for (ResearchGroup g : groups) {
            ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
            list.add(d);
        }

        Collections.sort(list, Collator.getInstance());
        return list;
    }

    @Override

    @Transactional(readOnly = true)
    public List<ResearchGroupData> getMyGroups() {

        Set<ResearchGroup> groups = personDao.getLoggedPerson().getResearchGroups();
        List<ResearchGroupData> list = new ArrayList<ResearchGroupData>();

        for (ResearchGroup g : groups) {
            ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
            list.add(d);
        }
        Collections.sort(list, Collator.getInstance());
        return list;
    }
}

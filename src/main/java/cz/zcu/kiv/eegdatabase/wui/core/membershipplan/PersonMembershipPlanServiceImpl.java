package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

/**
 * Created by Lichous on 6.4.15.
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonMembershipPlanDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PersonMembershipPlanServiceImpl extends GenericServiceImpl<PersonMembershipPlan, Integer> implements PersonMembershipPlanService{

    protected Log log = LogFactory.getLog(getClass());
    private PersonMembershipPlanDao dao;

    public PersonMembershipPlanServiceImpl(PersonMembershipPlanDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(PersonMembershipPlanDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonMembershipPlan> getPersonMembershipPlans(Person person) {
        return dao.getPersonMembershipPlans(person);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPlanUsed(int membershipPlanId) {
        return dao.isPlanUsed(membershipPlanId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveMembershipPlan(Person person) {
        return dao.hasActiveMembershipPlan(person);
    }

}

package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public class PersonMembershipPlanFacadeImpl extends GenericFacadeImpl<PersonMembershipPlan, Integer> implements PersonMembershipPlanFacade{

    protected Log log = LogFactory.getLog(getClass());
    private PersonMembershipPlanService service;

    public PersonMembershipPlanFacadeImpl(PersonMembershipPlanService membershipPlanService) {
        super(membershipPlanService);
        setService(membershipPlanService);
    }

    @Required
    public void setService(PersonMembershipPlanService service) {
        this.service = service;
    }

    @Override
    public List<PersonMembershipPlan> getPersonMembershipPlans(Person person) {
        return service.getPersonMembershipPlans(person);
    }

    @Override
    public boolean isPlanUsed(int membershipPlanId) {
        return service.isPlanUsed(membershipPlanId);
    }

    @Override
    public boolean hasActiveMembershipPlan(Person person) {
        return service.hasActiveMembershipPlan(person);
    }

}

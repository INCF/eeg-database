package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public class MembershipPlanFacadeImpl extends GenericFacadeImpl<MembershipPlan, Integer> implements MembershipPlanFacade{

    protected Log log = LogFactory.getLog(getClass());
    private MembershipPlanService service;

    public MembershipPlanFacadeImpl(MembershipPlanService membershipPlanService) {
        super(membershipPlanService);
        setService(membershipPlanService);
    }

    @Required
    public void setService(MembershipPlanService service) {
        this.service = service;
    }

    @Override
    public List<MembershipPlan> getPersonMembershipPlans(Person person) {
        return service.getPersonMembershipPlans(person);
    }

    @Override
    public List<MembershipPlan> getGroupMembershipPlans(ResearchGroup researchGroup) {
        return service.getGroupMembershipPlans(researchGroup);
    }

    @Override
    public List<MembershipPlan> getAvailablePersonMembershipPlans() {
        return service.getAvailablePersonMembershipPlans();
    }

    @Override
    public List<MembershipPlan> getAvailableGroupMembershipPlans() {
        return service.getAvailableGroupMembershipPlans();
    }
}

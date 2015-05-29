package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
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
    public List<MembershipPlan> getAvailablePersonMembershipPlans() {
        return service.getAvailablePersonMembershipPlans();
    }

    @Override
    public List<MembershipPlan> getAvailableGroupMembershipPlans() {
        return service.getAvailableGroupMembershipPlans();
    }

    @Override
    public MembershipPlan getMembershipPlanById(Integer id) {
        return service.getMembershipPlanById(id);
    }

    @Override
    public MembershipPlan read(Integer id) {
        return service.read(id);
    }

}

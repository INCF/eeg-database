package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public class ResearchGroupMembershipPlanFacadeImpl extends GenericFacadeImpl<ResearchGroupMembershipPlan, Integer> implements ResearchGroupMembershipPlanFacade{

    protected Log log = LogFactory.getLog(getClass());
    private ResearchGroupMembershipPlanService service;

    public ResearchGroupMembershipPlanFacadeImpl(ResearchGroupMembershipPlanService membershipPlanService) {
        super(membershipPlanService);
        setService(membershipPlanService);
    }

    @Required
    public void setService(ResearchGroupMembershipPlanService service) {
        this.service = service;
    }

    @Override
    public List<ResearchGroupMembershipPlan> getGroupMembershipPlans(ResearchGroup researchGroup) {
        return service.getGroupMembershipPlans(researchGroup);
    }

    @Override
    public boolean isPlanUsed(int membershipPlanId) {
        return service.isPlanUsed(membershipPlanId);
    }

}

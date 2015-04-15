package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

/**
 * Created by Lichous on 6.4.15.
 */

import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupMembershipPlanDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ResearchGroupMembershipPlanServiceImpl extends GenericServiceImpl<ResearchGroupMembershipPlan, Integer> implements ResearchGroupMembershipPlanService{

    protected Log log = LogFactory.getLog(getClass());
    private ResearchGroupMembershipPlanDao dao;

    public ResearchGroupMembershipPlanServiceImpl(ResearchGroupMembershipPlanDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(ResearchGroupMembershipPlanDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembershipPlan> getGroupMembershipPlans(ResearchGroup researchGroup) {
        return dao.getGroupMembershipPlans(researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPlanUsed(int membershipPlanId) {
        return dao.isPlanUsed(membershipPlanId);
    }
}

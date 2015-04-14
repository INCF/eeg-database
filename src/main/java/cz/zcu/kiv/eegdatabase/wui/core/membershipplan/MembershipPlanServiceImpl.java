package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

/**
 * Created by Lichous on 6.4.15.
 */

import cz.zcu.kiv.eegdatabase.data.dao.MembershipPlanDao;
import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MembershipPlanServiceImpl extends GenericServiceImpl<MembershipPlan, Integer> implements MembershipPlanService{

    protected Log log = LogFactory.getLog(getClass());
    private MembershipPlanDao dao;

    public MembershipPlanServiceImpl(MembershipPlanDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(MembershipPlanDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlan> getAvailablePersonMembershipPlans() {
        return dao.getAvailablePersonMembershipPlans();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlan> getAvailableGroupMembershipPlans() {
        return dao.getAvailableGroupMembershipPlans();
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipPlan read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipPlan getMembershipPlanById(Integer id) {
        return dao.getMembershipPlanById(id);
    }

}

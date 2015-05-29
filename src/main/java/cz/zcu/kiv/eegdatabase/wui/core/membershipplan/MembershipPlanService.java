package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public interface MembershipPlanService extends GenericService<MembershipPlan, Integer> {

    List<MembershipPlan> getAvailablePersonMembershipPlans();

    List<MembershipPlan> getAvailableGroupMembershipPlans();

    public MembershipPlan getMembershipPlanById(Integer id);

}

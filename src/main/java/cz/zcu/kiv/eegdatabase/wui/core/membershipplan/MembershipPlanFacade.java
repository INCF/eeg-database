package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public interface MembershipPlanFacade extends GenericFacade<MembershipPlan, Integer> {

    List<MembershipPlan> getPersonMembershipPlans(Person person);

    List<MembershipPlan> getGroupMembershipPlans(ResearchGroup researchGroup);

    List<MembershipPlan> getAvailablePersonMembershipPlans();

    List<MembershipPlan> getAvailableGroupMembershipPlans();
}

package cz.zcu.kiv.eegdatabase.wui.core.membershipplan;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */
public interface PersonMembershipPlanFacade extends GenericFacade<PersonMembershipPlan, Integer> {

    List<PersonMembershipPlan> getPersonMembershipPlans(Person person);

    public boolean isPlanUsed(int membershipPlanId);

}

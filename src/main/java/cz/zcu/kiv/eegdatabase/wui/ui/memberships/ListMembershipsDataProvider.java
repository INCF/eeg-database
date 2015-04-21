package cz.zcu.kiv.eegdatabase.wui.ui.memberships;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 20.4.15.
 */
public class ListMembershipsDataProvider extends BasicDataProvider<MembershipPlan> {

    public ListMembershipsDataProvider (MembershipPlanFacade facade, MembershipPlanType type) {

        super("name", SortOrder.ASCENDING);
        List<MembershipPlan> plans;

        if(type == MembershipPlanType.GROUP) {
            plans = facade.getAvailableGroupMembershipPlans();
        }
        else if (type == MembershipPlanType.PERSON){
            plans = facade.getAvailablePersonMembershipPlans();
        }
        else {
            plans = new ArrayList<MembershipPlan>();
        }

        super.listModel.setObject(plans);
    }
}

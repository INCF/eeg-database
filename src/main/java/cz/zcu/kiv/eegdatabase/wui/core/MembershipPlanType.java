package cz.zcu.kiv.eegdatabase.wui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 12.4.15.
 */
public enum MembershipPlanType {

    PERSON(0),
    GROUP(1);

    public static final String ENUM_PROPERTY_PREFIX = "general.membershipplan.${name}";

    private int type;

    private MembershipPlanType(int type) {
        this.type=type;
    }

    public int getType() {
        return this.type;
    }

    public static MembershipPlanType getMembershipPlanByType(int type) {
        for(MembershipPlanType tmp : values()) {
            if(tmp.getType() == type) return tmp;
        }
        return null;
    }
    public static List<Integer> getMembershipPlanTypes() {
        List<Integer> ret = new ArrayList<Integer>();
        for(MembershipPlanType tmp : values()) {
            ret.add(tmp.getType());
        }
        return ret;
    }
}

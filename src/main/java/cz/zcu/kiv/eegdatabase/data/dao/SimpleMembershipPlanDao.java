package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;

import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * SimpleMembershipPlanDao, 2015/03/23 21:52 administrator
 * <p/>
 * ********************************************************************************************************************
 */

public class SimpleMembershipPlanDao extends SimpleGenericDao<MembershipPlan,Integer> implements MembershipPlanDao {

    public SimpleMembershipPlanDao() {
        super(MembershipPlan.class);
    }

    @Override
    public List<MembershipPlan> getAvailableGroupMembershipPlans() {
        String query = "select m from MembershipPlan m where m.type = :membershipPlanType and m.valid = 'TRUE'";

        List<MembershipPlan> ret = this.getSession().createQuery(query).setParameter("membershipPlanType", MembershipPlanType.GROUP.getType()).list(); //set parameters
        return ret;
    }

    @Override
    public List<MembershipPlan> getAvailablePersonMembershipPlans() {
        String query = "select m from MembershipPlan m where m.type = :membershipPlanType and m.valid = 'TRUE'";

        List<MembershipPlan> ret = this.getSession().createQuery(query).setParameter("membershipPlanType",MembershipPlanType.PERSON.getType()).list(); //set parameters
        return ret;
    }

    @Override
    public MembershipPlan getMembershipPlanById(Integer id) {
        String query = "select m from MembershipPlan m where m.membershipId = :id";
        MembershipPlan ret = (MembershipPlan) this.getSession().createQuery(query).setParameter("id",id).uniqueResult();
        return ret;
    }

}

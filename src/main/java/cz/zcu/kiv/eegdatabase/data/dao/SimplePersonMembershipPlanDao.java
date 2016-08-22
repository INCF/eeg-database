package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;

import java.sql.Timestamp;
import java.util.Date;
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
 * SimplePersonMembershipPlanDao, 2015/03/23 23:00 administrator
 * <p/>
 * ********************************************************************************************************************
 */
public class SimplePersonMembershipPlanDao extends SimpleGenericDao<PersonMembershipPlan,Integer> implements PersonMembershipPlanDao {

    public SimplePersonMembershipPlanDao() {
        super(PersonMembershipPlan.class);
    }

    @Override
    public List<PersonMembershipPlan> getPersonMembershipPlans(Person person) {
        String query = "select m from PersonMembershipPlan m where m.person = :person";

        List<PersonMembershipPlan> ret = this.getSession().createQuery(query).setParameter("person",person).list(); //set parameters
        return ret;
    }

    @Override
    public boolean isPlanUsed(int membershipPlanId) {

        String query = "select membershipPlan from PersonMembershipPlan m where m.membershipPlan.membershipId = :plan";
        return (!this.getSession().createQuery(query).setParameter("plan",membershipPlanId).list().isEmpty());
    }

    @Override
    public boolean hasActiveMembershipPlan(Person person) {

        Timestamp time = new Timestamp(new Date().getTime());

        String query = "select m from PersonMembershipPlan m where m.person = :person and m.to > :time";

        return (!this.getSession().createQuery(query).setParameter("person",person).setParameter("time",time).list().isEmpty()); //set parameters

    }



}

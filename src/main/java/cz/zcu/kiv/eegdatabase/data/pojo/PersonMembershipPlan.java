package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;

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
 * PersonMembershipPlan, 2015/03/23 09:40 administrator
 * <p/>
 * ********************************************************************************************************************
 */

@Entity
@Table(name = "PERSON_MEMBERSHIP_PLAN")
public class PersonMembershipPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERSON_MEMBERSHIP_PLAN_ID")
    private int personMembershipPlanId;

    @ManyToOne
    @JoinColumn(name = "PERSON")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "MEMBERSHIP_PLAN")
    private MembershipPlan membershipPlan;

    public void setPersonMembershipPlanId(int personMembershipPlanId) {
        this.personMembershipPlanId = personMembershipPlanId;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

    public int getPersonMembershipPlanId() {
        return personMembershipPlanId;
    }

    public Person getPerson() {
        return person;
    }

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }
}

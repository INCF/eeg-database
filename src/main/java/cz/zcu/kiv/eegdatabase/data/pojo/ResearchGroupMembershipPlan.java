package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

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
 * ResearchGroupMembershipPlan, 2015/03/23 18:06 administrator
 * <p/>
 * ********************************************************************************************************************
 */

@Entity
@Table(name = "RESEARCH_GROUP_MEMBERSHIP_PLAN")
public class ResearchGroupMembershipPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESEARCH_GROUP_MEMBERSHIP_PLAN_ID")
    private int researchGroupMembershipPlanId;

    @Column(name="DATE_FROM")
    Timestamp from;

    @Column(name="DATE_TO")
    Timestamp to;

    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP")
    private ResearchGroup researchGroup;

    @ManyToOne
    @JoinColumn(name = "MEMBERSHIP_PLAN")
    private MembershipPlan membershipPlan;

    public int getResearchGroupMembershipPlanId() {
        return researchGroupMembershipPlanId;
    }

    public Timestamp getFrom() {
        return from;
    }

    public Timestamp getTo() {
        return to;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public void setResearchGroupMembershipPlanId(int researchGroupMembershipPlanId) {
        this.researchGroupMembershipPlanId = researchGroupMembershipPlanId;
    }

    public void setFrom(Timestamp from) {
        this.from = from;
    }

    public void setTo(Timestamp to) {
        this.to = to;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public void setMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

}

package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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
 * MembershipPlan, 2015/03/22 18:38 administrator
 * <p/>
 * ********************************************************************************************************************
 */

@Entity
@Table(name="MEMBERSHIP_PLAN")
public class MembershipPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBERSHIP_PLAN_ID")
    private int membershipId;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name="LENGTH")
    private long length;

    @Column(name="TYPE")
    private int type;

    @Column(name="VALID")
    private boolean valid;

    @OneToMany(mappedBy = "membershipPlan")
    private Set<PersonMembershipPlan> personMembershipPlans;

    @OneToMany(mappedBy = "membershipPlan")
    private Set<ResearchGroupMembershipPlan> researchGroupMembershipPlans;

    public int getMembershipId() {
        return membershipId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getLength() {
        return length;
    }

    public int getType() {
        return type;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Set<PersonMembershipPlan> getPersonMembershipPlans() {
        return personMembershipPlans;
    }

    public Set<ResearchGroupMembershipPlan> getResearchGroupMembershipPlans() {
        return researchGroupMembershipPlans;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPersonMembershipPlans(Set<PersonMembershipPlan> personMembershipPlans) {
        this.personMembershipPlans = personMembershipPlans;
    }

    public void setResearchGroupMembershipPlans(Set<ResearchGroupMembershipPlan> researchGroupMembershipPlans) {
        this.researchGroupMembershipPlans = researchGroupMembershipPlans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MembershipPlan that = (MembershipPlan) o;

        if (length != that.length) return false;
        if (membershipId != that.membershipId) return false;
        if (type != that.type) return false;
        if (valid != that.valid) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!name.equals(that.name)) return false;
        if (!price.equals(that.price)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = membershipId;
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + price.hashCode();
        result = 31 * result + (int) (length ^ (length >>> 32));
        result = 31 * result + type;
        result = 31 * result + (valid ? 1 : 0);
        return result;
    }
}

package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
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
 * PromoCode, 2015/03/23 17:36 administrator
 * <p/>
 * ********************************************************************************************************************
 */

@Entity
@Table(name="PROMO_CODE")
public class PromoCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROMO_CODE_ID")
    private int promoCodeId;

    @Column(name="KEYWORD")
    String keyword;

    @Column(name="DISCOUNT")
    String discount;

    @Column(name="DATE_FROM")
    Date from;

    @Column(name="DATE_TO")
    Date to;

    @Column(name="TYPE")
    int type;

    @Column(name="DESCRIPTION")
    String description;

    @OneToMany(mappedBy = "promoCode")
    private Set<PersonMembershipPlan> personMembershipPlans;

    @OneToMany(mappedBy = "promoCode")
    private Set<ResearchGroupMembershipPlan> researchGroupMembershipPlans;

    public int getPromoCodeId() {
        return promoCodeId;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDiscount() {
        return discount;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Set<PersonMembershipPlan> getPersonMembershipPlans() {
        return personMembershipPlans;
    }

    public Set<ResearchGroupMembershipPlan> getResearchGroupMembershipPlans() {
        return researchGroupMembershipPlans;
    }

    public void setPromoCodeId(int promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
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

        PromoCode promoCode = (PromoCode) o;

        if (promoCodeId != promoCode.promoCodeId) return false;
        if (type != promoCode.type) return false;
        if (description != null ? !description.equals(promoCode.description) : promoCode.description != null)
            return false;
        if (discount != null ? !discount.equals(promoCode.discount) : promoCode.discount != null) return false;
        if (from != null ? !from.equals(promoCode.from) : promoCode.from != null) return false;
        if (keyword != null ? !keyword.equals(promoCode.keyword) : promoCode.keyword != null) return false;
        if (to != null ? !to.equals(promoCode.to) : promoCode.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = promoCodeId;
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}

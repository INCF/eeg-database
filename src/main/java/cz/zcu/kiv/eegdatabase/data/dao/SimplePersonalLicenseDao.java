/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SimplePersonalLicenseDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicenseState;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

/**
 *
 * @author bydga
 */
public class SimplePersonalLicenseDao extends SimpleGenericDao<PersonalLicense, Integer> implements PersonalLicenseDao {
    
    protected Log log = LogFactory.getLog(getClass());
    
	public SimplePersonalLicenseDao() {
		super(PersonalLicense.class);
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group, PersonalLicenseState state) {
		if (group != null) {
			Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
			criteria.add(Restrictions.eq("licenseState", state));
			criteria.createAlias("license", "l");
			criteria.add(Restrictions.ne("l.licenseType", LicenseType.OWNER));
			criteria.add(Restrictions.eq("l.researchGroup.researchGroupId", group.getResearchGroupId()));
			return criteria.list();
		} else {
			return new ArrayList<PersonalLicense>();
		}
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(Person applicant, PersonalLicenseState state) {
		Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
		criteria.add(Restrictions.eq("person.personId", applicant.getPersonId()));
		criteria.add(Restrictions.eq("licenseState", state));
		return criteria.list();
	}

	@Override
	public boolean personHasLicense(int personId, int licenseId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
		criteria.add(Restrictions.eq("person.personId", personId));
		criteria.add(Restrictions.eq("license.licenseId", licenseId));
		criteria.setProjection(Projections.rowCount());
		int count = ((Number) criteria.uniqueResult()).intValue();
		return count > 0;
	}

	@Override
	public Integer create(PersonalLicense newInstance) {
		boolean present = this.personHasLicense(newInstance.getPerson().getPersonId(),
				newInstance.getLicense().getLicenseId());
		if (present) {
			//already there
			return -1;
		}
		return super.create(newInstance);
	}

	@Override
	public List<License> getUsersLicenses(Person person) {
		Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
		criteria.add(Restrictions.eq("person.personId", person.getPersonId()));
		criteria.setProjection(Projections.property("license"));
		return criteria.list();
	}

	@Override
	public byte[] getAttachmentContent(int personalLicenseId) {
		String query = "from PersonalLicense pl where pl.personalLicenseId = :id";
        PersonalLicense result =  (PersonalLicense) this.getSession().createQuery(query).setInteger("id", personalLicenseId).uniqueResult();
        try {
            return result.getAttachmentContent().getBytes(1, (int) result.getAttachmentContent().length());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return new byte[0];
        }
	}
}

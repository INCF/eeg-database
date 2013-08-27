/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author bydga
 */
public class SimplePersonalLicenseDao extends SimpleGenericDao<PersonalLicense, Integer> implements PersonalLicenseDao {

    public SimplePersonalLicenseDao() {
	super(PersonalLicense.class);
    }

	@Override
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group) {
		Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
        criteria.add(Restrictions.eq("license.researchGroup", group));
        return criteria.list();
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted) {
		Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
        criteria.add(Restrictions.eq("person", applicant));
		if (!accepted) {
			criteria.add(Restrictions.eq("confirmedDate", null));
		}
		else {
			criteria.add(Restrictions.ne("confirmedDate", null));
		}
        return criteria.list();
	}
}

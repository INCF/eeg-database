/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

/**
 *
 * @author bydga
 */
public class SimplePersonalLicenseDao extends SimpleGenericDao<PersonalLicense, Integer> implements PersonalLicenseDao {

	public SimplePersonalLicenseDao() {
		super(PersonalLicense.class);
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group, boolean confirmed) {
		if (group != null) {
			Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
			if (!confirmed) {
				criteria.add(Restrictions.isNull("confirmedDate"));
			} else {
				criteria.add(Restrictions.isNotNull("confirmedDate"));
			}
			criteria.createAlias("license", "l");
			criteria.add(Restrictions.ne("l.licenseType", LicenseType.OWNER));
			criteria.add(Restrictions.eq("l.researchGroup.researchGroupId", group.getResearchGroupId()));
			return criteria.list();
		} else {
			return new ArrayList<PersonalLicense>();
		}
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted) {
		Criteria criteria = this.getSession().createCriteria(PersonalLicense.class);
		criteria.add(Restrictions.eq("person.personId", applicant.getPersonId()));
		if (!accepted) {
			criteria.add(Restrictions.eq("confirmedDate", null));
		} else {
			criteria.add(Restrictions.ne("confirmedDate", null));
		}
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
		String query = "select attachmentContent from PersonalLicense pl where pl.personalLicenseId = :id";
        List<byte[]> result =  this.getSession().createQuery(query).setInteger("id", personalLicenseId).list();
        return result.isEmpty() ? null : result.get(0);
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author bydga
 */
public class SimpleExperimentPackageDao extends SimpleGenericDao<ExperimentPackage, Integer> implements ExperimentPackageDao {

	public SimpleExperimentPackageDao() {
		super(ExperimentPackage.class);
	}

	@Override
	public List<ExperimentPackage> listVisiblePackages(Person person) {
		String HQL = "SELECT DISTINCT ep FROM ExperimentPackage ep, ExperimentPackageConnection epc, ExperimentPackageLicense epl, PersonalLicense pl"
				+ " WHERE ep.experimentPackageId = epl.experimentPackage.experimentPackageId"
				+ " AND ( (epl.license.licenseType != :licenseType)"
				+ " OR (epl.license.licenseId = pl.license.licenseId"
				+ " AND pl.person.personId = :personId))"
				+ " AND ep.experimentPackageId = epc.experimentPackage.experimentPackageId";

		Query query = getSessionFactory().getCurrentSession().createQuery(HQL);
		query.setParameter("licenseType", LicenseType.OWNER);
		query.setParameter("personId", person.getPersonId());
		return query.list();
	}
}

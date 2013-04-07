/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import org.hibernate.Session;

/**
 *
 * @author bydga
 */
public class SimpleExperimentPackageLicenseDao extends SimpleGenericDao<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseDao {

    public SimpleExperimentPackageLicenseDao() {
	super(ExperimentPackageLicense.class);
    }
	
	
	@Override
	public void removeLicenseFromPackage(int packageId, int licenseId )
	{
		String hqlQuery = "delete from ExperimentPackageLicense epl where epl.experimentPackage = :ep and epl.license = :l";
        Session session = getSession();
        session.createQuery(hqlQuery).setInteger("ep", packageId).setInteger("l", licenseId).executeUpdate();
	}
}

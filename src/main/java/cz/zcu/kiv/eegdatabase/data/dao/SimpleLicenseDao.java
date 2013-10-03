/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bydga
 */
public class SimpleLicenseDao extends SimpleGenericDao<License, Integer> implements LicenseDao {

    public SimpleLicenseDao() {
	super(License.class);
    }

	@Override
	public List<License> getLicensesByType(int researchGroupId, LicenseType licenseType) {
		List<LicenseType> types = new ArrayList<LicenseType>(1);
		types.add(licenseType);
		return this.getLicensesByType(researchGroupId, types);
	}

	@Override
	public License getPublicLicense() {
		LicenseType licenseType = LicenseType.OPEN_DOMAIN;
		String hqlQuery = "select l from License l where l.licenseType=:licenseType";
		
		return (License) this.getSession().createQuery(hqlQuery).setParameter("licenseType", licenseType).list().iterator().next();
	}

	@Override
	public List<License> getLicensesByType(int reseachGroupId, List<LicenseType> licenseType) {
		String hqlQuery = "select l from License l where l.licenseType IN (:licenseType) and l.researchGroup=:researchGroup";

		return this.getSession().createQuery(hqlQuery).setInteger("researchGroup", reseachGroupId).setParameterList("licenseType", licenseType).list();
	}
    
}

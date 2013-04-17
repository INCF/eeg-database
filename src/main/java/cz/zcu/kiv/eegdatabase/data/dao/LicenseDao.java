/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.List;

/**
 *
 * @author bydga
 */
public interface LicenseDao extends GenericDao<License, Integer> {
	
	public List<License> getLicensesByType(int reseachGroupId, LicenseType licenseType);
	
	public License getPublicLicense();
}

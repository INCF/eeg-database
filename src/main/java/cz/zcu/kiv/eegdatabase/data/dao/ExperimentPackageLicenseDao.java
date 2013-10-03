/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;

/**
 *
 * @author bydga
 */
public interface ExperimentPackageLicenseDao extends GenericDao<ExperimentPackageLicense, Integer> {
	
	public void removeLicenseFromPackage(int packageId, int licenseId );
	
	public void removeAllConnections(ExperimentPackage pack);
}

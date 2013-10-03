/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.util.List;

/**
 *
 * @author bydga
 */
public interface ExperimentPackageDao extends GenericDao<ExperimentPackage, Integer> {

	/**
	 * Returns list of visible packages for a person.
	 *
	 * That is all but private (except the user has owner license).
	 * @return
	 */
	public List<ExperimentPackage> listVisiblePackages(Person person);
	
}
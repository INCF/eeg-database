/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;

/**
 *
 * @author bydga
 */
public class SimpleExperimentPackageDao extends SimpleGenericDao<ExperimentPackage, Integer> implements ExperimentPackageDao {

    public SimpleExperimentPackageDao() {
	super(ExperimentPackage.class);
    }
    
}

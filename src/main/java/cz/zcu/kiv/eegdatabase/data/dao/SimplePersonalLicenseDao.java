/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;

/**
 *
 * @author bydga
 */
public class SimplePersonalLicenseDao extends SimpleGenericDao<PersonalLicense, Integer> implements PersonalLicenseDao {

    public SimplePersonalLicenseDao() {
	super(PersonalLicense.class);
    }
}

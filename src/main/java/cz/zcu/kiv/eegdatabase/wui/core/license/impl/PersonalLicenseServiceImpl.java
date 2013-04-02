/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;

/**
 *
 * @author veveri
 */
public class PersonalLicenseServiceImpl extends GenericServiceImpl<PersonalLicense, Integer> implements PersonalLicenseService{

    public PersonalLicenseServiceImpl() {
    }

    public PersonalLicenseServiceImpl(GenericDao<PersonalLicense, Integer> dao) {
	super(dao);
    }

}

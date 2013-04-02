/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;

/**
 *
 * @author veveri
 */
public class LicenseServiceImpl extends GenericServiceImpl<License, Integer> implements LicenseService {

    public LicenseServiceImpl() {
    }

    public LicenseServiceImpl(GenericDao<License, Integer> dao) {
	super(dao);
    }

}

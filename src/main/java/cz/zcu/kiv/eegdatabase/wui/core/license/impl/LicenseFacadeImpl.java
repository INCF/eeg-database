/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;

/**
 *
 * @author J. Danek
 */
public class LicenseFacadeImpl extends GenericFacadeImpl<License, Integer> implements LicenseFacade {

    public LicenseFacadeImpl() {
    }

    public LicenseFacadeImpl(GenericService<License, Integer> service) {
	super(service);
    }


}

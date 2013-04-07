/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author J. Danek
 */
public class LicenseFacadeImpl extends GenericFacadeImpl<License, Integer> implements LicenseFacade {

	
	private LicenseService licenseService;
	
    public LicenseFacadeImpl() {
    }

    public LicenseFacadeImpl(GenericService<License, Integer> service) {
	super(service);
    }
	
	@Required
	public void setLicenseService(LicenseService licenseRervice)
	{
		this.licenseService = licenseRervice;
	}

	@Override
	public void addLicenseForPackage(License license, ExperimentPackage pack) {
		this.licenseService.addLicenseForPackage(license, pack);
	}

	@Override
	public void removeLicenseFromPackage(License license, ExperimentPackage pack) {
		this.licenseService.removeLicenseFromPackage(license, pack);
	}
	
}

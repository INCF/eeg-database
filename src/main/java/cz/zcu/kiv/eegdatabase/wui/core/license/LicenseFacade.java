/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import java.io.Serializable;

/**
 *
 * @author j. Danek
 */
public interface LicenseFacade extends GenericFacade<License, Integer> {
	
	public void addLicenseForPackage(License license, ExperimentPackage group);
	
	public void removeLicenseFromPackage(License license, ExperimentPackage group);
	
}

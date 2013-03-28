/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.components.page;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author veveri
 */
public class TestPage extends BasePage {

	@SpringBean
	LicenseDao licenseDao;
	@SpringBean
	ExperimentDao experimentDao;
	@SpringBean
	ResearchGroupDao researchGroupDao;

	public TestPage() {

		//global public license
		License publicLicense = new License();
		publicLicense.setDescription("All experiments published under this license are available for all purposes");
		publicLicense.setLicenseType(LicenseType.OPEN_DOMAIN);
		publicLicense.setPrice(0);
		publicLicense.setTitle("Public license");

		List<ResearchGroup> groups = researchGroupDao.getAllRecords();
		for (ResearchGroup group : groups) {
			List<Experiment> publicExperiments = new ArrayList<Experiment>();
			List<Experiment> privateExperiments = new ArrayList<Experiment>();

			//divide all group's experiments on public/private
			for (Experiment experiment : group.getExperiments()) {
				if (experiment.isPrivateExperiment()) {
					privateExperiments.add(experiment);
				} else {
					publicExperiments.add(experiment);
				}
			}

			if (publicExperiments.size() > 0) {

				//create experiment package for public experiments
				ExperimentPackage publicExperimentPackage = new ExperimentPackage();
				
				publicExperimentPackage.setName("Default public pack");
				publicExperimentPackage.setResearchGroup(group);
				group.getExperimentPackages().add(publicExperimentPackage);

				for (Experiment ex : publicExperiments) {
					ExperimentPackageConnection epc = new ExperimentPackageConnection();

					epc.setExperiment(ex);
					ex.getExperimentPackageConnections().add(epc);

					epc.setExperimentPackage(publicExperimentPackage);
					publicExperimentPackage.getExperimentPackageConnections().add(epc);
				}

				ExperimentPackageLicense experimentPackageLicense = new ExperimentPackageLicense();
				
				experimentPackageLicense.setExperimentPackage(publicExperimentPackage);
				publicExperimentPackage.getExperimentPackageLicenses().add(experimentPackageLicense);
				
				experimentPackageLicense.setLicense(publicLicense);
				publicLicense.getExperimentPackageLicenses().add(experimentPackageLicense);


			}


			if (privateExperiments.size() > 0) {

				//create experiment package for public experiments
				ExperimentPackage privateExperimentPackage = new ExperimentPackage();
				
				privateExperimentPackage.setName("Default private pack");
				privateExperimentPackage.setResearchGroup(group);
				group.getExperimentPackages().add(privateExperimentPackage);

				for (Experiment ex : publicExperiments) {
					ExperimentPackageConnection epc = new ExperimentPackageConnection();

					epc.setExperiment(ex);
					ex.getExperimentPackageConnections().add(epc);

					epc.setExperimentPackage(privateExperimentPackage);
					privateExperimentPackage.getExperimentPackageConnections().add(epc);
				}

				License privateLicense = new License();
				privateLicense.setDescription("Default generated private license");
				privateLicense.setTitle("Private License");
				privateLicense.setLicenseType(LicenseType.PRIVATE);
				privateLicense.setPrice(0);

				ExperimentPackageLicense experimentPackageLicense = new ExperimentPackageLicense();
				
				experimentPackageLicense.setExperimentPackage(privateExperimentPackage);
				privateExperimentPackage.getExperimentPackageLicenses().add(experimentPackageLicense);
				experimentPackageLicense.setLicense(privateLicense);
				privateLicense.getExperimentPackageLicenses().add(experimentPackageLicense);
			}

		}


	}
}

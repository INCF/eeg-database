/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.accesscontrol.temporary;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Data migration logic.
 *
 * Thread.sleep is used to avoid running out of connections.
 * Not pretty, yet suffitient for the time being.
 *
 * @author J. Danek
 */
public class DbMigrationPage extends BasePage {

	@SpringBean
	PersonalLicenseService personalLicenseService;
	@SpringBean
	PersonFacade personFacade;
	@SpringBean
	LicenseFacade licenseFacade;
	@SpringBean
	ExperimentPackageFacade experimentPackageFacade;
	@SpringBean
	ExperimentPackageConnectionService experimentPackageConnectionService;
	@SpringBean
	ExperimentPackageLicenseService experimentPackageLicenseService;
	private Log log = LogFactory.getLog(getClass());

	@SpringBean
	ExperimentsFacade experimentsFacade;
	@SpringBean
	ResearchGroupFacade researchGroupFacade;
	
	private void doWork() {
		//global public license

		log.error("DB Migration Start");

		//create system public license
		License publicLicense = new License();
		publicLicense.setDescription("All experiments published under this license are available for all purposes");
		publicLicense.setLicenseType(LicenseType.OPEN_DOMAIN);
		publicLicense.setPrice(0);
		publicLicense.setResearchGroup(null);
		publicLicense.setTitle("Public license");
		int id = licenseFacade.create(publicLicense);
		publicLicense = licenseFacade.read(id);



		List<ResearchGroup> groups = researchGroupFacade.getAllRecords();
		log.error("got " + groups.size() + " groups");

		List<Experiment> publicExperiments;
		List<Experiment> privateExperiments;
		for (ResearchGroup group : groups) {
			publicExperiments = new ArrayList<Experiment>();
			privateExperiments = new ArrayList<Experiment>();

			try {
			    Thread.sleep(200);
			} catch (Exception exc) {

			}

			//divide all group's experiments on public/private
			List<Experiment> experiments = experimentsFacade.readByParameter("researchGroup.researchGroupId", group.getResearchGroupId());

			for (Experiment experiment : experiments) {
				if (experiment.isPrivateExperiment()) {
					privateExperiments.add(experiment);
				} else {
					publicExperiments.add(experiment);
				}
			}

			log.info("Group " + group.getTitle() + "has " + publicExperiments.size() + " public experiments");

			//create group's public package
			ExperimentPackage publicExperimentPackage = new ExperimentPackage();
			publicExperimentPackage.setName("Default public pack");
			publicExperimentPackage.setResearchGroup(group);
			//get id and reload the newly created instance to ensure hibernate cache sanity
			id = experimentPackageFacade.create(publicExperimentPackage);
			publicExperimentPackage = experimentPackageFacade.read(id);

			for (Experiment ex : publicExperiments) {
			    if(ex.getExperimentId() == 0) { //avoid invalid db state
				continue;
			    }

			    log.info("ResearchGRoupID: " + group.getResearchGroupId());
			    log.info("ExperimentID: " + ex.getExperimentId());
			    //connect the experiment to the package
			    ExperimentPackageConnection epc = new ExperimentPackageConnection();
			    epc.setExperiment(ex);
			    epc.setExperimentPackage(publicExperimentPackage);
			    experimentPackageConnectionService.create(epc);
			    try {
				Thread.sleep(100);
			    } catch (Exception exc) {
				
			    }
			}

			//bind the package to the public license
			ExperimentPackageLicense experimentPackageLicense = new ExperimentPackageLicense();
			experimentPackageLicense.setExperimentPackage(publicExperimentPackage);
			experimentPackageLicense.setLicense(publicLicense);
			experimentPackageLicenseService.create(experimentPackageLicense);
			log.info("Public experiments migration FINISHED");

			if (privateExperiments.size() > 0) {
				log.info("Group " + group.getTitle() + "has " + privateExperiments.size() + " private experiments");

				//create experiment package for private experiments
				ExperimentPackage privateExperimentPackage = new ExperimentPackage();
				privateExperimentPackage.setName("Default private pack");
				privateExperimentPackage.setResearchGroup(group);
				//get id and reload the newly created instance to ensure hibernate cache sanity
				id = experimentPackageFacade.create(privateExperimentPackage);
				privateExperimentPackage = experimentPackageFacade.read(id);

				for (Experiment ex : privateExperiments) {
				    if(ex.getExperimentId() == 0) { //avoid invalid db state
					continue;
				    }
				    
				    ExperimentPackageConnection epc = new ExperimentPackageConnection();
				    epc.setExperiment(ex);
				    epc.setExperimentPackage(privateExperimentPackage);
				    experimentPackageConnectionService.create(epc);

				    try {
					Thread.sleep(100);
				    } catch (Exception exc) {

				    }
				}

				//create private license for the package
				License privateLicense = new License();
				privateLicense.setDescription("Default generated owner license");
				privateLicense.setTitle("Owner License");
				privateLicense.setLicenseType(LicenseType.OWNER);
				privateLicense.setPrice(0);
				privateLicense.setResearchGroup(group);
				id = this.licenseFacade.create(privateLicense);
				privateLicense = licenseFacade.read(id);

				//bind the package to the license
				experimentPackageLicense = new ExperimentPackageLicense();
				experimentPackageLicense.setExperimentPackage(privateExperimentPackage);
				experimentPackageLicense.setLicense(privateLicense);
				experimentPackageLicenseService.create(experimentPackageLicense);
			}

		}
		
		
	}

	
	private void doWork2() {
		
		// set public licences
		log.error("Public licenses adding started.");
		List<License> publicLicences = licenseFacade.readByParameter("licenseType", LicenseType.OPEN_DOMAIN);
		License publicLicense = publicLicences.get(0);
		List<Person> allPersons = personFacade.getAllRecords();
		
		for(Person person : allPersons) {
			PersonalLicense personalLicence = new PersonalLicense();
			personalLicence.setPerson(person);
			personalLicence.setLicense(publicLicense);
			personalLicenseService.create(personalLicence);
			log.info("Added public license " + publicLicense.getLicenseId() + " to person " + person.getPersonId() + ".");
		}
		log.error("Public licenses adding ended.");
		
		
		// set private licences
		log.error("Private licenses adding started.");
		List<License> privateLicences = licenseFacade.readByParameter("licenseType", LicenseType.OWNER);
		int groupId;
		for (License licence : privateLicences) {
			
			log.info("Licence ID: " + licence.getLicenseId());
			log.info("Licence Title: " + licence.getTitle());
			
			List<ExperimentPackageLicense> experimentPackageLicenses = experimentPackageLicenseService.readByParameter("license.licenseId", licence.getLicenseId());
			
			for( ExperimentPackageLicense epl : experimentPackageLicenses ) {
				
				log.info("--EPL package: " + epl.getExperimentPackage().getExperimentPackageId());
				log.info("--EPL licence: " + epl.getExperimentPackageLicenseId());
				
				groupId = epl.getExperimentPackage().getResearchGroup().getResearchGroupId();
				List<ResearchGroupMembership> memberships = researchGroupFacade.readMemberhipByParameter("researchGroup.researchGroupId", groupId);


				for( ResearchGroupMembership membership : memberships ) {

					log.info("------NEW PersonalLicense: person " + membership.getPerson().getPersonId() + ", license " + licence.getLicenseId());

					PersonalLicense personalLicence = new PersonalLicense();
					personalLicence.setPerson(membership.getPerson());
					personalLicence.setLicense(licence);
					personalLicenseService.create(personalLicence);
					try {
					    Thread.sleep(100);
					} catch (Exception exc) {

					}
				}
			}
			
		}
		log.error("Private licenses adding ended.");
	}

	public DbMigrationPage() {
	    doWork();
	    doWork2();
	}
}

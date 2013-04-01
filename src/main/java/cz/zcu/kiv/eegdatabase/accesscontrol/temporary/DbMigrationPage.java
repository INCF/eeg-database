/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.accesscontrol.temporary;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author veveri
 */
public class DbMigrationPage extends BasePage {

	@SpringBean
	PersonalLicenseDao personalLicenseDao;
	@SpringBean
	PersonDao personDao;
	@SpringBean
	LicenseDao licenseDao;
	@SpringBean
	ExperimentPackageDao experimentPackageDao;
	@SpringBean
	ExperimentPackageConnectionDao experimentPackageConnectionDao;
	@SpringBean
	ExperimentPackageLicenseDao experimentPackageLicenseDao;
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
		publicLicense.setTitle("Public license");
		int id = licenseDao.create(publicLicense);
		publicLicense = licenseDao.read(id);



		List<ResearchGroup> groups = researchGroupFacade.getAllRecords();
		log.error("got " + groups.size() + " groups");

		List<Experiment> publicExperiments;
		List<Experiment> privateExperiments;
		for (ResearchGroup group : groups) {
			publicExperiments = new ArrayList<Experiment>();
			privateExperiments = new ArrayList<Experiment>();

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
			id = experimentPackageDao.create(publicExperimentPackage);
			publicExperimentPackage = experimentPackageDao.read(id);

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
			    experimentPackageConnectionDao.create(epc);
			}

			//bind the package to the public license
			ExperimentPackageLicense experimentPackageLicense = new ExperimentPackageLicense();
			experimentPackageLicense.setExperimentPackage(publicExperimentPackage);
			experimentPackageLicense.setLicense(publicLicense);
			experimentPackageLicenseDao.create(experimentPackageLicense);
			log.info("Public experiments migration FINISHED");

			if (privateExperiments.size() > 0) {
				log.info("Group " + group.getTitle() + "has " + privateExperiments.size() + " private experiments");
				//create experiment package for private experiments
				ExperimentPackage privateExperimentPackage = new ExperimentPackage();
				privateExperimentPackage.setName("Default private pack");
				privateExperimentPackage.setResearchGroup(group);
				//get id and reload the newly created instance to ensure hibernate cache sanity
				id = experimentPackageDao.create(privateExperimentPackage);
				privateExperimentPackage = experimentPackageDao.read(id);

				for (Experiment ex : privateExperiments) {
				    if(ex.getExperimentId() == 0) { //avoid invalid db state
					continue;
				    }
				    
				    ExperimentPackageConnection epc = new ExperimentPackageConnection();
				    epc.setExperiment(ex);
				    epc.setExperimentPackage(privateExperimentPackage);
				    experimentPackageConnectionDao.create(epc);
				}

				//create private license for the package
				License privateLicense = new License();
				privateLicense.setDescription("Default generated private license");
				privateLicense.setTitle("Private License");
				privateLicense.setLicenseType(LicenseType.PRIVATE);
				privateLicense.setPrice(0);
				id = this.licenseDao.create(privateLicense);
				privateLicense = licenseDao.read(id);

				//bind the package to the license
				experimentPackageLicense = new ExperimentPackageLicense();
				experimentPackageLicense.setExperimentPackage(privateExperimentPackage);
				experimentPackageLicense.setLicense(privateLicense);
				experimentPackageLicenseDao.create(experimentPackageLicense);
			}

		}
		
		
	}

	
	private void doWork2() {
		
		// set public licences
		log.error("Public licenses adding started.");
		List<License> publicLicences = licenseDao.readByParameter("licenseType", 0);
		License publicLicense = publicLicences.get(0);
		List<Person> allPersons = personDao.getAllRecords();
		
		for(Person person : allPersons) {
			PersonalLicense personalLicence = new PersonalLicense();
			personalLicence.setPerson(person);
			personalLicence.setLicense(publicLicense);
			personalLicenseDao.create(personalLicence);
			log.info("Added public license " + publicLicense.getLicenseId() + " to person " + person.getPersonId() + ".");
		}
		log.error("Public licenses adding ended.");
		
		
		// set private licences
		log.error("Private licenses adding started.");
		List<License> privateLicences = licenseDao.readByParameter("licenseType", 3);
		for (License licence : privateLicences) {
			
			log.info("Licence ID: " + licence.getLicenseId());
			log.info("Licence Title: " + licence.getTitle());
			
			List<ExperimentPackageLicense> experimentPackageLicenses = experimentPackageLicenseDao.readByParameter("license", licence.getLicenseId());
			
			for( ExperimentPackageLicense epl : experimentPackageLicenses ) {
				
				log.info("--EPL package: " + epl.getExperimentPackage());
				log.info("--EPL licence: " + epl.getExperimentPackageLicenseId());
				
				List<ExperimentPackage> experimentPackages = experimentPackageDao.readByParameter("experimentPackageId", epl.getExperimentPackageLicenseId());
				
				for( ExperimentPackage ep : experimentPackages) {
					
					log.info("----Package ID: " + ep.getExperimentPackageId());
					log.info("----Package research group: " + ep.getResearchGroup());
					
					Set<ResearchGroupMembership> memberships = ep.getResearchGroup().getResearchGroupMemberships();
					
					for( ResearchGroupMembership membership : memberships ) {
						
						log.info("------NEW PersonalLicense: person " + membership.getPerson().getPersonId() + ", license " + licence.getLicenseId());
						
						//PersonalLicense personalLicence = new PersonalLicense();
						//personalLicence.setPerson(membership.getPerson());
						//personalLicence.setLicense(licence);
						//personalLicenseDao.create(personalLicence);
					}
				}
			}
			
		}
		log.error("Private licenses adding ended.");
	}

	public DbMigrationPage() {
		doWork2();
	}
}

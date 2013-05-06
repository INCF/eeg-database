package cz.zcu.kiv.eegdatabase.logic.workflow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.webservices.processor.DataProcessor;
import cz.zcu.kiv.eegdatabase.webservices.processor.generated.DataFile;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

public class WorkflowServiceImpl implements WorkflowService {

	/**
	 * Taking all user definied values, creates workflow definition file and
	 * creating new thread for data processing.
	 */

	private List<DataFile> data = new ArrayList<DataFile>();
	private List<String> stepDef = new ArrayList<String>();
	private List<String> resultNames = new ArrayList<String>();
	private List<String> stepNames = new ArrayList<String>();
	private Set<Integer> fileIds = new TreeSet<Integer>();

	private String workflow;
	private String experimentName; //Natvrdo prirazene, neimplementovano

	Log log = LogFactory.getLog(getClass());

	@Autowired
	ServiceResultDao resultDao;

	@Autowired
	PersonDao personDao;

	@Autowired
	FileFacade fileFacade;

	@Autowired
	ExperimentsFacade facade;

	/**
	 * Create workflow definition file and new thread for workflow processing
	 */

	@Override
	public void runService() {
		Person owner = personDao.getLoggedPerson();
		StringBuilder createDef = new StringBuilder();
		createDef
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><workflow name=\"Workflow\">");
		for (String step : stepDef) {
			createDef.append(step);
		}
		createDef.append("</workflow>");
		workflow = createDef.toString();
		new InvokeProcessor(owner).start();
	}

	/**
	 * Creating segment of workfow definition file from user defined parameters.
	 * Adding step name to list of steps.
	 */

	@Override
	public void addToWorkflow(String name, String format, String store,
			String method, String params, List<String> fileNames) {
		if (store.equalsIgnoreCase("true")) {
			resultNames.add(name);
		}
		stepNames.add(experimentName +"_" +name); 
		StringBuilder workstep = new StringBuilder();
		String line1 = "<workstep name=\"" + name + "\" format=\"" + format
				+ "\" store=\"" + store + "\">";
		workstep.append(line1);
		for (String file : fileNames) {
			String line = "<data>" + file + "</data>";
			workstep.append(line);
		}
		String line2 = "<method params=\"" + params + "\">" + method
				+ "</method></workstep>";
		workstep.append(line2);
		stepDef.add(workstep.toString());
	}
	
	/**
	 * Clearing defined workflow
	 */
	
	@Override
	public void clearWorkflow(){
		stepDef.clear();
	}

	/**
	 * Adding fileIds to list of fileIds. Each id can be in list only once.
	 */

	@Override
	public void addFileIds(int[] files) {
		for (int a = 0; a < files.length; a++) {
			fileIds.add(files[a]);
		}
	}

	// TODO nasledujici metody jsou z duvodu popisneho souboru pro vice
	// workunit. V klientu neni podpora implementovana.

	@Override
	public void beginExperiment(String name) {
		experimentName = name;
		stepDef.add("<workunit name=\"" + name + "\">");
	}

	@Override
	public void endExperiment() {
		stepDef.add("</workunit>");
	}

	/**
	 * Getting all experiments from database and createing name with id string
	 * 
	 */

	@Override
	public List<String> getExperiments() {
		List<String> output = new ArrayList<String>();
		List<Experiment> experiments = facade.getAllRecords();
		for (Experiment e : experiments) {
			int exId = e.getExperimentId();
			String nameId = "";
			if (exId < 100) {
				nameId = "0" + exId;
			} else {
				nameId = "" + exId;
			}
			String name = nameId + ":" + e.getEnvironmentNote();
			output.add(name);
		}
		Collections.sort(output);
		return output;
		// TODO mozno rozsirit pouze o experimenty podporujici moznost
		// zpracovani
	}

	/**
	 * Get available methods form workflow service
	 */

	@Override
	public List<String> getMethods() {
		DataProcessor proc = new DataProcessor();
		return proc.getMethods();
	}

	@Override
	public List<String> getStepNames() {
		return stepNames;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	private class InvokeProcessor extends Thread {

		/**
		 * New thread getting files, processing them and storing results
		 */

		private Person serviceOwner;
		private List<ServiceResult> results = new ArrayList<ServiceResult>();

		public InvokeProcessor(Person serviceOwner) {
			this.serviceOwner = serviceOwner;
		}

		public void run() {
			for (String name : resultNames) {
				ServiceResult service = new ServiceResult();
				service.setOwner(serviceOwner);
				service.setStatus("running");
				service.setTitle(name);
				service.setFilename(name + ".xml");
				resultDao.create(service);
				results.add(service);
				log.debug("Service result " + service.getTitle()
						+ " created in database.");
			}
			for (Integer a : fileIds) {
				DataFileDTO content = fileFacade.getFile(a);
				DataFile file = new DataFile();
				file.setFileName(content.getFileName());
				file.setFile(content.getFileContent());
				data.add(file);
			}
			DataProcessor proc = new DataProcessor();
			List<byte[]> result = proc.invokeService(data, workflow);
			workflow = "";
			storeResults(result);
		}

		/**
		 * Storing results with data blob to database.
		 * 
		 * @param data
		 */

		private void storeResults(List<byte[]> data) {
			int index = 0;
			for (byte[] result : data) {
				ServiceResult service = results.get(index);
				try {
					service.setFigure(new SerialBlob(result));
				} catch (SerialException e) {
					log.error(e);
				} catch (SQLException e) {
					log.equals(e);
				}
				service.setStatus("finished");
				resultDao.update(service);
				log.debug("Service result " + service.getTitle()
						+ " updated with blob data in database.");
				index++;
			}
		}
	}
}

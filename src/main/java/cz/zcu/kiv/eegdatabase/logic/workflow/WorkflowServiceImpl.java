package cz.zcu.kiv.eegdatabase.logic.workflow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.webservices.processor.DataProcessor;
import cz.zcu.kiv.eegdatabase.webservices.processor.generated.DataFile;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

public class WorkflowServiceImpl implements WorkflowService {

	private List<DataFile> data = new ArrayList<DataFile>();
	private List<String> stepDef = new ArrayList<String>();
	private List<String> resultNames = new ArrayList<String>();
	private Set<Integer> fileIds = new TreeSet<Integer>();
	private String workflow;

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	@Autowired
	ServiceResultDao resultDao;

	@Autowired
	PersonDao personDao;

	@Autowired
	FileFacade fileFacade;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void runService() {
		Person owner = personDao.getLoggedPerson();
		StringBuilder createDef = new StringBuilder();
		createDef.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><workflow name=\"Workflow\">");
		for(String step : stepDef){
			createDef.append(step);
		}
		createDef.append("</workflow>");
		workflow = createDef.toString();
		new InvokeProcessor(owner).start();
	}

	@Override
	public void addToWorkflow(String name, String format, String store,
			String method, String params, List<String> fileNames) {
		if (store.equalsIgnoreCase("true")) {
			resultNames.add(name);
		}
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

	@Override
	public void addFileIds(int[] files) {
		for (int a = 0; a < files.length; a++) {
			fileIds.add(files[a]);
		}
	}

	@Override
	public void beginExperiment(String name) {
		stepDef.add("<workunit name=\"" + name + "\">");
	}

	@Override
	public void endExperiment() {
		stepDef.add("</workunit>");
	}

	private class InvokeProcessor extends Thread {

		private Person serviceOwner;
		private List<ServiceResult> results = new ArrayList<ServiceResult>();

		public InvokeProcessor(Person serviceOwner) {
			this.serviceOwner = serviceOwner;
		}

		public void run() {
			for(String name : resultNames){
				ServiceResult service = new ServiceResult();
				service.setOwner(serviceOwner);
				service.setStatus("running");
				service.setTitle(name);
				service.setFilename(name + ".xml");
				resultDao.create(service);
				results.add(service);
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
			storeResults(result);
		}

		private void storeResults(List<byte[]> data) {
			int index = 0;
			for (byte[] result : data) {
				ServiceResult service = results.get(index);
				try {
					service.setFigure(new SerialBlob(result));
				} catch (SerialException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				service.setStatus("finished");
				resultDao.update(service);
				index++;
			}
		}
	}
}

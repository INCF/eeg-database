package cz.zcu.kiv.eegdatabase.logic.workflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleServiceResultDao;

import cz.zcu.kiv.eegdatabase.webservices.processor.generated.DataFile;

import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

public class WorkflowServiceImpl implements WorkflowService {

	private List<DataFile> data = new ArrayList<DataFile>();
	private String workflow;
	
	@Autowired
	SimpleServiceResultDao resultDao;

	@Override
	public void runService(final int[] pole, final FileFacade fileFacade){
		
		
	//	final Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
		
	//	ServiceResult serviceRes = new ServiceResult();
    //    serviceRes.setOwner(loggedUser);
    //    serviceRes.setStatus("running");
    //    serviceRes.setTitle("test");
    //    serviceRes.setFilename("test.xml");
    //    resultDao.create(serviceRes);
//		
		Thread thread = new Thread(new Runnable()
		{
		public void run()
		{
			for(int a = 0; a < pole.length; a++){
				DataFileDTO content = fileFacade.getFile(pole[a]);
				DataFile file = new DataFile();
				file.setFileName(content.getFileName());
				file.setFile(content.getFileContent());
				data.add(file);
			}
//			EEGprocessor proc = new EEGprocessor();
	//		List<byte[]> result = proc.invokeService(data, workflow);
	}
		});
		thread.start();
	}

	@Override
	public void storeWorkflow() {
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(
					"E:\\Code\\EEGData\\workflow.xml"));
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line.trim());
			}
			setWorkflow(sb.toString());
			br.close();
		} catch (Exception e) {
		}
	}

	/**
	 * @return the workflow
	 */
	public String getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow
	 *            the workflow to set
	 */
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	
	/**
	 * @return the data
	 */
	public List<DataFile> getData() {
		return data;
	}
}

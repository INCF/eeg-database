package cz.zcu.kiv.eegdatabase.webservices.processor;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;

import cz.zcu.kiv.eegdatabase.webservices.processor.generated.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.processor.generated.ProcessService;

public class DataProcessor {
	
	/**
	 * Communication with webservice
	 */

	Map<String, Object> properties = new HashMap<String, Object>();
	JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	Log log = LogFactory.getLog(getClass());

	public DataProcessor() {
		properties.put("mtom-enabled", Boolean.TRUE);
		factory.setServiceClass(ProcessService.class);
		factory.setAddress(PropertiesLoader.getKey("endpoint"));
		factory.setProperties(properties);
		factory.setUsername(PropertiesLoader.getKey("username"));
		factory.setPassword(PropertiesLoader.getKey("password"));
	}
	
	/**
	 * Getting list of available mthods
	 * @return
	 */

	public List<String> getMethods() {
		List<String> availableMethods = new ArrayList<String>();
		try {
			availableMethods = createService().getAvailableMethods();
		} catch (Exception e) {
			log.error(e);
		}
		return availableMethods;
	}
	
	/**
	 * Processing workflow. Result is deserialized to list of byte arrays which
	 * reprezent results.
	 * 
	 * @param data
	 * @param workflow
	 * @return
	 */

	public List<byte[]> invokeService(List<DataFile> data, String workflow) {
		List<byte[]> finalOutput = null;
		try {
			byte[] output = createService().processWorkflow(data, workflow);
			ByteArrayInputStream bis = new ByteArrayInputStream(output);
			ObjectInput in = null;
			try {
				in = new ObjectInputStream(bis);
				finalOutput = (List<byte[]>) in.readObject();
			} catch (Exception e) {
				log.error(e);
			} finally {
				bis.close();
				in.close();
			}
		} catch (Exception e) {
			log.error(e);
		}
		return finalOutput;
	}

	private ProcessService createService() {
		ProcessService service = (ProcessService) factory.create();
		Client cl = ClientProxy.getClient(service);
		HTTPConduit httpConduit = (HTTPConduit) cl.getConduit();
		httpConduit.getClient().setReceiveTimeout(0);
		return service;
	}

}

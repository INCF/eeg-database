package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.webservices.EDPClient.ProcessService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingServiceImpl implements SignalProcessingService {

    private ProcessService service;

    @Override
    public List<String> getAvailableMethods() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
                factory.setServiceClass(ProcessService.class);
                factory.setAddress
                        ("http://147.228.63.134:8080/eegdataprocessor/webservice/webservice/processService");
                factory.setUsername("jan.stebetak@seznam.cz");
                factory.setPassword("stebjan");

                service = (ProcessService) factory.create();
                //TODO this solution is workaround. Solve the problem with xml configuration
            //    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

               // service = (ProcessService) context.getBean("service");
                return service.getAvailableMethods();
    }
}

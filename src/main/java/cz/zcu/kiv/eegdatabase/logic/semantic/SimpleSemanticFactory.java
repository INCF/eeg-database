package cz.zcu.kiv.eegdatabase.logic.semantic;

import com.hp.hpl.jena.shared.CannotEncodeCharacterException;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import tools.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Factory for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 24.2.11
 * Time: 13:36
 */
public class SimpleSemanticFactory implements InitializingBean, ApplicationContextAware, SemanticFactory {

    private Log log = LogFactory.getLog(getClass());
    private ApplicationContext context;
    private List<GenericDao> gDaoList = new ArrayList<GenericDao>();
    private List<Object> dataList = new ArrayList<Object>();

    private File ontologyFile;      // temporary ontology document
    private Resource ontology;      // owl document with static ontology statements

    @Autowired
    private TransactionTemplate transactionTemplate;


    /**
     * Creates temporary files for ontology document storing and
     * sets the timer to run the transformation process regularly.
     */
    public void init() {
        try {
            ontologyFile = File.createTempFile("ontology_", ".rdf.tmp");
            ontologyFile.deleteOnExit();
        } catch (IOException e) {
            log.error("Could not create the temporary rdf/xml file to store the ontology!", e);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TransformTask(), 100 * 1000, 24 * 3600 * 1000);  // TODO upravit interval
    }


    /**
     * Creates list of instances of DAO
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        String[] beanNamesForType = context.getBeanNamesForType(GenericDao.class);
        for(String name : beanNamesForType) {
            gDaoList.add((GenericDao) context.getBean(name));
            break; // ???
        }
    }

    
    @Override
    public InputStream getOntology(String syntax) throws IOException {
        InputStream is;
        JenaBeanExtension jbe;

        // check the validity of user request on serialization syntax
        if (syntax == null || syntax.equalsIgnoreCase("owl")
                    || syntax.equalsIgnoreCase(Syntax.RDF_XML_ABBREV))
            syntax = Syntax.RDF_XML;
        syntax = syntax.toUpperCase();

        // check if the temporary file contains error log instead of ontology
        if (errorLog() == true)
            return new FileInputStream(ontologyFile);

        // get the ontology document in required syntax
        is = new FileInputStream(ontologyFile);
        if ( !syntax.equals(Syntax.RDF_XML)) {
            jbe = new JenaBeanExtensionTool();
            jbe.loadStatements(is, Syntax.RDF_XML);
            is = jbe.getOntologyDocument(syntax);
        }

        return is;
    }


    @Override
    public InputStream getOntologySchema(String syntax) throws IOException {
        InputStream is;
        JenaBeanExtension jbe;

        // check if the temporary file contains error log instead of ontology
        if (errorLog() == true)
            return new FileInputStream(ontologyFile);

        // create the ontology schema
        is = new FileInputStream(ontologyFile);
        jbe = new JenaBeanExtensionTool();
        jbe.loadStatements(is, Syntax.RDF_XML);
        is = jbe.getOntologySchema(Syntax.RDF_XML_ABBREV);

        return is;
    }


    @Override
    public InputStream getOntologyOwlApi(String syntax) throws IOException,
                                    OWLOntologyCreationException, OWLOntologyStorageException {
        InputStream is;
        OwlApi owlApi;

        if (syntax == null)
            syntax = Syntax.OWL_XML;

        // check if the temporary file contains error log instead of ontology
        if (errorLog() == true)
            return new FileInputStream(ontologyFile);

        // get the serialization from the OWL API
        is = new FileInputStream(ontologyFile);
        owlApi = new OwlApiTool(is);
        is = owlApi.getOntologyDocument(syntax);

        return is;
    }


    /**
     * Loads date for transforms POJO object to resouces of semantic web.
     */
    private void loadData() {
         for (GenericDao gDao : gDaoList) {
            dataList.addAll(gDao.getAllRecords());
         }
    }


    /**
     * Sets application context.
     * @param ac - application context
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }


    /**
     * Sets resource with static ontology statements.
     * @param ontology - resource in rdf/xml
     */
    public void setOntology(Resource ontology) {
        this.ontology = ontology;
    }


    /**
     * Transforms the object-oriented model into the OWL ontology.
     * Serialization of this ontology is stored in a temporary file.
     */
    public void transformModel() {
        loadData();
        JenaBeanExtension jbe = null;
        OutputStream out = null;

        try {
            jbe = creatingJenaBean();
            out = new FileOutputStream(ontologyFile);
            jbe.writeOntologyDocument(out, Syntax.RDF_XML);
            out.close();
        } catch (FileNotFoundException e) {
            log.error("Could not find the temporary rdf/xml file to store the ontology!", e);
        } catch (IOException e) {
            log.error("Could not close the temporary rdf/xml file!", e);
        } catch (Exception e) {
            log.error("Could not create the ontology!", e);
            try {
                java.io.StringWriter sw = new java.io.StringWriter(1024);
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                pw.close();
                String error = "ERROR Could not create the ontology!\n\n" + sw.toString();
                out = new FileOutputStream(ontologyFile);
                out.write(error.getBytes());
                out.close();
            } catch (Exception e2) {
                log.error("Could not write the error message to the temporary rdf/xml file!");
            }
        }

    }


    /**
     * Creates an instance of JenaBeanExtension with loaded model.
     * @return instance of JenaBeanExtension
     */
    private JenaBeanExtension creatingJenaBean() {
        JenaBeanExtension jbe = new JenaBeanExtensionTool();
        try {
            jbe.loadStatements(ontology.getInputStream(), Syntax.RDF_XML_ABBREV);
        } catch (IOException e) {
            log.error("Could not open the input stream associated with the ontology " +
                    "configuration document: " + ontology.getFilename(), e);
        }
        jbe.loadOOM(dataList);
        jbe.declareAllClassesDisjoint();
        return jbe;
    }


    /**
     * Determines whether the temporary ontology file contains an error log instead
     * of the ontology.
     * @return true if the ontology file contains error log, otherwise false
     * @throws IOException
     */
    private boolean errorLog() throws IOException {
        InputStream is = new FileInputStream(ontologyFile);
        Scanner sc = new Scanner(is);
        boolean error = sc.next().equalsIgnoreCase("error");
        is.close();
        return error;
    }

    


    /**
     * Represents the transformation task for a timer.
     * This task should be run regularly to keep the ontology up-to-date.
     */
    private class TransformTask extends TimerTask {

        @Override
        public void run() {
            log.debug("Starting OOP to OWL transformation process.");
            System.out.println("\nStarting transformation to OWL.");
            transactionTemplate.execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    transformModel();
                    return null;
                }
            });
            log.debug("OOP to OWL transformation process finished.");
            System.out.println("Transformation finished.");
        }

    }


}



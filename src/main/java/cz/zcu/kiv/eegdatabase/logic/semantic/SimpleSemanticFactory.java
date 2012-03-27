package cz.zcu.kiv.eegdatabase.logic.semantic;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private JenaBeanExtension jenaBean;
    private JenaBeanExtension jenaBeanStructure;

    private Timer timer;
    private File ontologyFile;      // temporary ontology document
    private File ontStructureFile;  // temporary ontology structure document
    private Resource ontology;      // owl document with static ontology statements

    @Autowired
    private TransactionTemplate transactionTemplate;


    /**
     * Runs the transformation when initializing this bean.
     */
    public void init() {

        // zde je treba vytvorit session ??

        log.debug("Starting OOP to OWL transformation process.");
            transactionTemplate.execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                   // transformModel();
                    return null;
                }
            });

        log.debug("OOP to OWL transformation process ended.");


        //timer = new Timer();
        //timer.scheduleAtFixedRate(new TransformTask(), 120 * 1000, 500 * 1000);
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
    public InputStream generateOntology(String syntax) throws IOException {
        return generateOntology(syntax, false);
    }


    @Override
    public InputStream generateOntology(String syntax, boolean structureOnly) throws IOException {
        InputStream is;
        String lang;

        if (syntax == null || syntax.equalsIgnoreCase("owl"))
            syntax = Syntax.RDF_XML;
        lang = syntax.toUpperCase();
        if (structureOnly && lang.equals(Syntax.RDF_XML))
            lang = Syntax.RDF_XML_ABBREV;

        is = creatingJenaBean(structureOnly).getOntologyDocument(lang);

        // z temporary file
        /*if (structureOnly) {
            is = new FileInputStream(ontStructureFile);
        } else {
            is = new FileInputStream(ontologyFile);
            if (! lang.equals(Syntax.RDF_XML)) {
                JenaBeanExtension jbe = new JenaBeanExtensionTool();
                jbe.loadStatements(is);
                is = jbe.getOntologyDocument(lang);
            }
        }*/

        return is;
    }


    @Override
    public InputStream generateOntologyOwlApi(String syntax) throws IOException,
                                    OWLOntologyCreationException, OWLOntologyStorageException {
        InputStream is;
        OwlApi owlApi;

        is = creatingJenaBean(false).getOntologyDocument();
        owlApi = new OwlApiTool(is);
        is = owlApi.convertToSemanticStandard(syntax);

        return is;
    }


    /**
     * Creates JenaBeanExtensionTool for transforming POJO objects into an ontology document.
     * @param  structureOnly - if true no data are loaded (only their structure)
     * @return jenaBean - Jena bean
     */
    private JenaBeanExtension creatingJenaBean(boolean structureOnly) {
        JenaBeanExtension jbe = structureOnly ? jenaBeanStructure : jenaBean;

        // uchovavani instance JenaBeanu je prozatimni reseni - casem vhodne pravidelne generovani
        if (jbe == null) {
            loadData();
            jbe = new JenaBeanExtensionTool();
            try {
                jbe.loadStatements(ontology.getInputStream());
            } catch (IOException e) {
                log.error("Could not open the input stream associated with the ontology " +
                        "configuration document: " + ontology.getFilename(), e);
            }
            jbe.loadOOM(dataList, structureOnly);
            if (structureOnly)
                jenaBeanStructure = jbe;
            else
                jenaBean = jbe;
        }
        return jbe;
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
        createTempFiles();
        JenaBeanExtension jbe;
        jbe = createOntModel(false); // TODO nahradit metodou creatingJenabean()
        try {
            OutputStream out = new FileOutputStream(ontologyFile);
            jbe.writeOntologyDocument(out);
            out.close();
            System.out.println("Model zapsan do souboru: " + ontologyFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Could not create the temporary rdf/xml file to store the ontology!", e);
            ontologyFile = null;
        }
        jbe = createOntModel(true); // TODO nahradit metodou creatingJenabean()
        try {
            OutputStream out = new FileOutputStream(ontStructureFile);
            jbe.writeOntologyDocument(out, Syntax.RDF_XML_ABBREV);
            out.close();
            System.out.println("Struktura modelu zapsana do souboru: " + ontStructureFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Could not create the temporary rdf/xml file to store the ontology structure!", e);
            ontStructureFile = null;
        }
    }


    // prozatim misto creatingJenabean()
    private JenaBeanExtension createOntModel(boolean structureOnly) {
        JenaBeanExtension jbe = new JenaBeanExtensionTool();
        loadStaticOntologyStatements(jbe);
        jbe.loadOOM(dataList, structureOnly);
        return jbe;
    }


    /**
     * Loads static ontology statements into the model.
     * @param jbe - instance of JenaBeanExtension in which to load the statements
     * @return JenaBeanExtension instance
     */
    private JenaBeanExtension loadStaticOntologyStatements(JenaBeanExtension jbe) {
        try {
            jbe.loadStatements(ontology.getInputStream());
        } catch (IOException e) {
            log.error("Could not open the input stream associated with the ontology " +
                    "configuration document: " + ontology.getFilename(), e);
        }
        System.out.println("Statements loaded.");
        return jbe;
    }


    /**
     * Creates temporary files for storing the ontology serialization.
     */
    private void createTempFiles() {
        try {
            if (ontologyFile == null) {
                ontologyFile = File.createTempFile("ontology_", ".rdf.tmp");
                ontologyFile.deleteOnExit();
            }
            if (ontStructureFile == null) {
                ontStructureFile = File.createTempFile("ontologyStructure_", ".rdf.tmp");
                ontStructureFile.deleteOnExit();
            }
        } catch (IOException e) {
            log.error("Could not create the temporary rdf/xml file to store the ontology!", e);
        }
    }




    private class TransformTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("Spoustim transformaci...");
            transformModel();
            System.out.println("Hotovo.");
        }
    }


}



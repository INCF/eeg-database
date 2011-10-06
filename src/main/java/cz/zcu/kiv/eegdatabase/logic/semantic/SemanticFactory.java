package cz.zcu.kiv.eegdatabase.logic.semantic;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.jenaBean.JenaBean;
import cz.zcu.kiv.jenaBean.JenaBeanTool;
import cz.zcu.kiv.owlApi.OwlApi;
import cz.zcu.kiv.owlApi.OwlApiTool;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 24.2.11
 * Time: 13:36
 */
public class SemanticFactory implements InitializingBean, ApplicationContextAware {
    private ApplicationContext context;
    private List<GenericDao> gDaoList = new ArrayList<GenericDao>();
    private List dataList = new ArrayList();

    /**
     * Creates list of instances of DAO
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        String[] beanNamesForType = context.getBeanNamesForType(GenericDao.class);
        for(String name : beanNamesForType) {
            gDaoList.add((GenericDao) context.getBean(name));
            break;
        }
    }

    /**
     * Transforms POJO object to resouces of semantic web
     * @param typeTransform - param from user (rdf, owl, ttl)
     * @return  bout - generated resource of semantic web(rdf, owl, ttl)
     * @throws IOException
     * @throws OWLOntologyStorageException
     * @throws OWLOntologyCreationException
     */
    public InputStream transformPOJOToSemanticResource(String typeTransform) throws IOException, OWLOntologyStorageException, OWLOntologyCreationException {
        InputStream is = null;
        OwlApi owlApi = new OwlApiTool(creatingJenaBean());
        is = owlApi.convertToSemanticStandard(typeTransform);
        return is;
    }

    /**
     * Generates RDF
     * @return  is - RDF
     * @throws IOException
     */
     public InputStream  generateRDF() throws IOException{
        InputStream is = null;
        is = creatingJenaBean().getOutJB();
        return is;
    }


    /**
     * Creates Jena bean for creating RDF
     * @return jenaBean - Jena bean
     * @throws IOException
     */
    private JenaBeanTool creatingJenaBean() throws IOException {
       loadData();
       JenaBean jenaBean = new JenaBeanTool(dataList);
       return (JenaBeanTool)jenaBean;
    }

    /**
     * Loads date for transforms POJO object to resouces of semantic web
     */
    private void loadData() {
         for (GenericDao gDao : gDaoList) {
            dataList.addAll(gDao.getAllRecords());
        }
    }

    /**
     * Sets application context
     * @param ac - application context
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }
}



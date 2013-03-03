package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 0:30
 * To change this template use File | Settings | File Templates.
 */
public class PojoIndexer<T> implements Indexer<T> {

    @Autowired
    private SolrServer solrServer;

    protected Log log = LogFactory.getLog(getClass());

    public PojoIndexer() {
    }

    /**
     * Adds the annotated POJO fields as a document to the Solr index.
     * @param instance The POJO to be indexed.
     * @throws IOException
     * @throws SolrServerException
     * @throws IllegalAccessException
     */
    public void index(T instance) throws IOException, SolrServerException, IllegalAccessException {

        SolrInputDocument document = new SolrInputDocument();
        Field[] fields = instance.getClass().getDeclaredFields();

        String className =  instance.getClass().getName();
        for (Field field : fields) {
            // add uuid, class name and id of the instance to the solr index
            if (field.isAnnotationPresent(SolrId.class)) {
                field.setAccessible(true); // necessary since the id field is private
                int id = 0;
                try {
                    id = (Integer) field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                document.addField(IndexField.UUID.getValue(), className + id);
                document.addField(IndexField.ID.getValue(), id);
                document.addField(IndexField.CLASS.getValue(), className);

            }

            // check presence of the SolrField annotation for each field
            else if (field.isAnnotationPresent(SolrField.class)) {
                field.setAccessible(true); // necessary since all fields are private
                Object fieldValue = field.get(instance);
                System.out.println(fieldValue.toString());

                document.addField(field.getAnnotation(SolrField.class).name().getValue(), fieldValue);
            }
        }

        if(document.isEmpty()) {
            log.info("Nothing added to the solr index.");
            return;
        }

        solrServer.add(document); // if the document already exists, it is replaced by the new one
        UpdateResponse response = solrServer.commit();

        log.info("Document " + document.get("uuid").getValue().toString() + " added to the solr index.");
        long time = response.getElapsedTime();
        int status = response.getStatus();

        log.info("Time elapsed: " + time + ", status code: " + status);
    }

    /**
     * Removes the indexed POJO values from the Solr index.
     * @param instance POJO whose document in the index should be removed.
     * @throws IOException
     * @throws SolrServerException
     */
    public void unindex(T instance) throws IOException, SolrServerException {

        Field[] fields = instance.getClass().getDeclaredFields();
        String className =  instance.getClass().getName();
        int id = 0;

        for (Field field : fields) {
            if (field.isAnnotationPresent(SolrId.class)) {
                field.setAccessible(true); // necessary since the id field is private
                try {
                    id = (Integer) field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        String uuid = className + id;
        solrServer.deleteById(uuid);
    }

    public void getDocumentFieldValuePairs(SolrInputDocument document) {
        Iterator<String> nameIterator = document.getFieldNames().iterator();
        while (nameIterator.hasNext()) {
            String name = nameIterator.next();
            System.out.println(name + ": " + document.getFieldValue(name));
        }
    }

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
}

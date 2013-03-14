package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 0:30
 * To change this template use File | Settings | File Templates.
 */
public class PojoIndexer<T> extends Indexer<T> {


    protected Log log = LogFactory.getLog(getClass());

    public PojoIndexer() {
    }

    @Override
    public SolrInputDocument prepareForIndexing(T instance) throws IOException, SolrServerException, IllegalAccessException {
        SolrInputDocument document = getDocumentFromAnnotatedFields(instance);
        document = createDocumentId(instance, document);
        return document;
    }

    /**
     * Removes the indexed POJO values from the Solr index.
     *
     * @param instance POJO whose document in the index should be removed.
     * @throws IOException
     * @throws SolrServerException
     */
    public void unindex(T instance) throws IOException, SolrServerException {

        Field[] fields = instance.getClass().getDeclaredFields();
        String className = instance.getClass().getName();
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
        solrServer.commit();
    }

    @Override
    public void unindexAll() throws IOException, SolrServerException {
        solrServer.deleteByQuery("!source:linkedin");
        solrServer.commit();
    }

    private SolrInputDocument getDocumentFromAnnotatedFields(T instance) throws IllegalAccessException {
        SolrInputDocument document = new SolrInputDocument();
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            // check presence of the SolrField annotation for each field
            if (field.isAnnotationPresent(SolrField.class)) {
                field.setAccessible(true); // necessary since all fields are private
                Object fieldValue = field.get(instance); // actual value of the annotated field
                log.debug(instance.getClass().getName());

                // null values are skipped
                if (fieldValue == null) {
                    continue;
                }

                log.info("Indexing - field: "
                        + field.getAnnotation(SolrField.class).name().getValue()
                        + " value: " + fieldValue.toString());

                document.addField(field.getAnnotation(SolrField.class).name().getValue(), fieldValue);
            }
        }

        return document;
    }

    private SolrInputDocument createDocumentId(T instance, SolrInputDocument document) {
        Field[] fields = instance.getClass().getDeclaredFields();
        String className = instance.getClass().getName();
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
                log.debug("ID: " + id);
                document.addField(IndexField.UUID.getValue(), className + id);
                document.addField(IndexField.ID.getValue(), id);
                document.addField(IndexField.CLASS.getValue(), className);
                break;
            }
        }
        return document;
    }

    public void getDocumentFieldValuePairs(SolrInputDocument document) {
        Iterator<String> nameIterator = document.getFieldNames().iterator();
        while (nameIterator.hasNext()) {
            String name = nameIterator.next();
            log.info(name + ": " + document.getFieldValue(name));
        }
    }
}

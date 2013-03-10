package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 28.2.13
 * Time: 1:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class Indexer<T> {

    @Autowired
    protected SolrServer solrServer;

    protected Log log = LogFactory.getLog(getClass());

    protected abstract void index(T instance) throws Exception; // TODO specify (own) exceptions

    protected abstract void unindex(T instance) throws Exception; // TODO specify (own) exceptions

    protected SolrInputDocument getDocumentFromAnnotatedFields(T instance) throws IllegalAccessException {
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
                log.info("Indexing - field: "
                        + field.getAnnotation(SolrField.class).name().getValue()
                        + " value: " + fieldValue.toString());

                document.addField(field.getAnnotation(SolrField.class).name().getValue(), fieldValue);
            }
        }

        return document;
    }
}

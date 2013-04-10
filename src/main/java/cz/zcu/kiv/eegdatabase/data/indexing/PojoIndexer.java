package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.Indexed;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FullTextSearchUtils;
import javassist.util.proxy.MethodFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.search.SearchUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.hibernate.Hibernate;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.reflections.Reflections;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Indexer of POJO classes.
 *
 * User: Jan Koren
 * Date: 19.2.13
 */
public class PojoIndexer extends Indexer {


    protected Log log = LogFactory.getLog(getClass());

    // levels of indexing traversal
    private final int LEVEL_PARENT = 0;
    private final int LEVEL_CHILD = 1;

    public PojoIndexer() {
    }

    @Override
    public SolrInputDocument prepareForIndexing(Object instance) throws IOException, SolrServerException, IllegalAccessException {

        Class clazz = instance.getClass();
        // first find out whether the class shall be indexed
        if(!clazz.isAnnotationPresent(Indexed.class)) {
            return null;
        }

        String className = instance.getClass().getName();
        Field[] fields = clazz.getDeclaredFields();

        // pak zjisteni, jestli nektery jeji field ma anotaci @SolrId
        int id = getId(fields, instance);
        // a z id vytvorit nasledujici fieldy Solr dokumentu
        SolrInputDocument document = new SolrInputDocument();
        document.addField(IndexField.UUID.getValue(), className + id);
        document.addField(IndexField.ID.getValue(), id);
        document.addField(IndexField.CLASS.getValue(), FullTextSearchUtils.getDocumentType(clazz));

        // pak vytazeni hodnot fieldu, ktere maji anotaci @SolrField
        //SolrInputDocument document = getDocumentFromAnnotatedFields(instance);
        Map<String, Object> documentFields = null;
        try {
            documentFields = getDocumentFromAnnotatedFields(instance, LEVEL_PARENT);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        for(String key : documentFields.keySet()) {
            document.addField(key, documentFields.get(key));
        }


        // pote projit vsechny kolekce, pro kazdy objekt kolekce najit @SolrField anotace u jeho fieldu
        // a jeho @SolrId pro sparovani polozek v multivalued listu rodicovskeho dokumentu


        //document = createDocumentId(instance, document);
        return document;
    }

    private int getId(Field[] fields, Object instance) {
        for (Field field : fields) {
            // add uuid, class name and id of the instance to the solr index
            if (field.isAnnotationPresent(SolrId.class)) {
                field.setAccessible(true); // necessary since the id field is private
                int id = 0;
                try {
                    id = (Integer) field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                log.debug("ID: " + id);
                return id;
            }
        }
        // class doesn't contain the @SolrId annotation, throw an exception
        throw new RuntimeException("None of the fields contains the @SolrId annotation.");
    }

    /**
     * Removes the indexed POJO values from the Solr index.
     *
     * @param instance POJO whose document in the index should be removed.
     * @throws IOException
     * @throws SolrServerException
     */
    public void unindex(Object instance) throws IOException, SolrServerException {

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

    /**
     * Creates a document for indexing. The document consists of values of fields having indexing annotations.
     * Depending on the annotation type, fields are either put to the document or traversed recursively to pick
     * property values of nested objects.
     * @param instance Parent object to be indexed,
     * @param level Current level of traversal. Can be either parent or child level. In case of the parent level, the
     *              algorithm can continue indexing the child elements located in the child level. In both levels
     *              indexing of String and primitive types is allowed.
     * @return Field-value pairs representing a document to be indexed.
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private Map<String, Object> getDocumentFromAnnotatedFields(Object instance, int level) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> solrFields = new HashMap<String, Object>();
        //SolrInputDocument document = new SolrInputDocument();
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
                String fieldName = field.getAnnotation(SolrField.class).name().getValue();

                if(level == LEVEL_CHILD) {
                    fieldName = "child_" + fieldName;
                }

                log.debug("Indexing - field: " + fieldName
                        + " value: " + fieldValue.toString());

                // POJO has more fields having the same annotation parameter
                if(solrFields.containsKey(fieldName)) {
                    solrFields.put(fieldName, solrFields.get(fieldName) + " " + fieldValue);
                }
                // the annotated value is being read for the first time
                else {
                    solrFields.put(fieldName, fieldValue);
                }
            }
            // traverse collections and objects of the parent object
            else if (level == LEVEL_PARENT) {
                // check if it is a collection
                if (field.isAnnotationPresent(Indexed.class) && Collection.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true); // necessary since all fields are private
                    Object collectionInstance = field.get(instance);
                    if (collectionInstance == null) {
                        continue;
                    }
                    // convert the collection to an array to enable transparent manipulation independent
                    // on the specific collection subclass
                    Method objectToArray = field.getType().getMethod("toArray");
                    Object array = objectToArray.invoke(collectionInstance);
                    // get array size
                    int length = Array.getLength(array);

                    Map<String, List<Object>> solrFieldsChildren = new HashMap<String, List<Object>>();
                    for(int i = 0; i < length; i++) {
                        Object element = Array.get(array, i);
                        // scan only the @SolrField-annotated fields of the child objects
                        Map<String, Object> solrFieldsChild = getDocumentFromAnnotatedFields(element, LEVEL_CHILD);
                        // add all field-value pairs of each object of the collection to the map of the whole collection
                        for(Map.Entry<String, Object> entry : solrFieldsChild.entrySet()) {

                            if(solrFieldsChildren.containsKey(entry.getKey())) {
                                List<Object> list = solrFieldsChildren.get(entry.getKey());
                                list.add(entry.getValue());
                                solrFieldsChildren.put(entry.getKey(), list);
                            }
                            else {
                                List<Object> list = new ArrayList<Object>();
                                list.add(entry.getValue());
                                solrFieldsChildren.put(entry.getKey(), list);
                            }
                        }
                    }
                    // add the index values of the children from the collection to the parent fields
                    solrFields.putAll(solrFieldsChildren);
                }
                // check if it's an object
                else if (field.isAnnotationPresent(Indexed.class) && field.getType().isInstance(Object.class)) {
                    // scan only the @SolrField-annotated fields of the child objects
                    Map<String, Object> solrFieldsChild = getDocumentFromAnnotatedFields(field.get(instance), LEVEL_CHILD);
                    solrFields.putAll(solrFieldsChild);
                }
            }
        }

        //return document;
        return solrFields;
    }

    /**
     * Writes field-value pairs for a given solr document.
     * @param document The input solr document.
     */
    public void getDocumentFieldValuePairs(SolrInputDocument document) {
        Iterator<String> nameIterator = document.getFieldNames().iterator();
        while (nameIterator.hasNext()) {
            String name = nameIterator.next();
            log.info(name + ": " + document.getFieldValue(name));
        }
    }
}

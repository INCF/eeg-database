package cz.zcu.kiv.eegdatabase.data.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
import org.hibernate.SessionFactory;

/**
 * Interface for connecting logic and data layer.
 * This interface makes create, showing, delete,
 * read and update data possible.
 * @author Pavel Bořík, A06208
 */
public interface GenericDao <T, PK extends Serializable>{

	
	SessionFactory getSessionFactory();
	
    /**
     * Create new record (row) in database.
     * @param newInstance - Object that will be created in database
     * @return record (row) saving in database
     */
    PK create(T newInstance);

    /**
     * Method read record (row) in database.
     * Record select in agreement with Primary Key (id).
     * @param id - Id selecting (searching) record
     * @return object that was selected in database in
     * agreement with PK
     */
    T read(PK id);

    /**
     * Method read record (row) in database based on column and it's value.
     * @param parameterName - hibernate name of the parameter (column)
     * @param parameterValue - value of the parameter
     * @return object that was selected in database
     */
    List <T> readByParameter(String parameterName, Object parameterValue);

	/**
	 * Read records from database based on columns and values.
	 * @param paramMap map of pairs columnName - value
	 * @return
	 */
	List<T> readByParameter(Map<String, Object> paramMap);

    /**
     * Method update data in database.
     * @param transientObject - updating object
     */
    void update(T transientObject);

    /**
     * Delete data from database.
     * @param persistentObject - Object that will be deleted from database
     */
    void delete(T persistentObject);

    /**
     * Method gets all records from database.
     * @return list that includes all records
     */
    List<T> getAllRecords();

    /**
     * Get specific count of records from database.
     * Count is given values of params.
     * @param first - first select records (start from grant zero)
     * @param max - count of records
     * @return list that includes specific count of records
     */
    List<T> getRecordsAtSides(int first, int max);

    /**
     * Method gets count of records in database.
     * @return count of records in database
     */
    int getCountRecords();

    public Map<T, String>  getFulltextResults(String fullTextQuery) throws ParseException;

    /**
     * Gets all records with all of their properties set.
     * The method enforces eager loading of all properties that have their getter methods.
     * @return List of records with eagerly loaded properties.
     */
    public List<T> getAllRecordsFull();

    List<T> findByExample(T example);
}

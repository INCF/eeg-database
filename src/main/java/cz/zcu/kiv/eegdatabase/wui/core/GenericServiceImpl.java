package cz.zcu.kiv.eegdatabase.wui.core;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author veveri
 */
public class GenericServiceImpl <T, PK extends Serializable> implements GenericService<T, PK> {

    private GenericDao<T, PK> dao;

    public GenericServiceImpl() {
	this(null);
    }

    public GenericServiceImpl(GenericDao<T, PK> dao) {
	this.dao = dao;
    }

    @Override
    @Transactional
    public PK create(T newInstance) {
	return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly=true)
    public T read(PK id) {
	return dao.read(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> readByParameter(String parameterName, Object parameterValue) {
	return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(T transientObject) {
	dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(T persistentObject) {
	dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> getAllRecords() {
	return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly=true)
    public List<T> getRecordsAtSides(int first, int max) {
	return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly=true)
    public int getCountRecords() {
	return dao.getCountRecords();
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author veveri
 */
public class GenericFacadeImpl<T, PK extends Serializable> implements GenericFacade<T, PK> {

	private GenericService<T, PK> service;

	public GenericFacadeImpl() {
		this(null);
	}

	public GenericFacadeImpl(GenericService<T, PK> service) {
		this.service = service;
	}

	@Override
	@Transactional
	public PK create(T newInstance) {
		return service.create(newInstance);
	}

	@Override
	@Transactional(readOnly = true)
	public T read(PK id) {
		return service.read(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> readByParameter(String parameterName, Object parameterValue) {
		return service.readByParameter(parameterName, parameterValue);
	}

	@Override
	@Transactional
	public void update(T transientObject) {
		service.update(transientObject);
	}

	@Override
	@Transactional
	public void delete(T persistentObject) {
		service.delete(persistentObject);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllRecords() {
		return service.getAllRecords();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getRecordsAtSides(int first, int max) {
		return service.getRecordsAtSides(first, max);
	}

	@Override
	@Transactional(readOnly = true)
	public int getCountRecords() {
		return service.getCountRecords();
	}

	@Override
	public List<T> getUnique(T example) {
		return this.service.getUnique(example);
	}
}

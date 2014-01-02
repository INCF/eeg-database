/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

import cz.zcu.kiv.eegdatabase.data.elasticsearch.repositories.SampleExperimentRepository;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.io.Serializable;
import javax.annotation.Resource;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 *
 * @author bydga
 */
public class MyInterceptor extends EmptyInterceptor {

	@Resource
	private SampleExperimentRepository sampleExperimentRepository;

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		boolean res = super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
		if (entity instanceof Experiment) {
			Experiment e = (Experiment) entity;
			e.getElasticExperiment().setExperimentId("" + id);
			this.sampleExperimentRepository.index(e.getElasticExperiment());

		}

		return res;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		boolean res = super.onSave(entity, id, state, propertyNames, types);

		if (entity instanceof Experiment) {
			Experiment e = (Experiment) entity;
			e.getElasticExperiment().setExperimentId("" + id);
			this.sampleExperimentRepository.index(e.getElasticExperiment());
		}

		return res;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		super.onDelete(entity, id, state, propertyNames, types);
		if (entity instanceof Experiment) {
			Experiment e = (Experiment) entity;
			this.sampleExperimentRepository.delete(e.getElasticExperiment());
		}
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		boolean res = super.onLoad(entity, id, state, propertyNames, types); //To change body of generated methods, choose Tools | Templates.

		if (entity instanceof Experiment) {
			Experiment e = (Experiment) entity;
			ExperimentElastic elastic = sampleExperimentRepository.findOne("" + e.getExperimentId());
			if (elastic != null) {
				e.setElasticExperiment(elastic);
			}
		}

		return res;
	}
}

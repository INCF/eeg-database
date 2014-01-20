/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

import cz.zcu.kiv.eegdatabase.data.elasticsearch.repositories.SampleExperimentRepository;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import java.io.Serializable;
import java.util.ArrayList;
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
			this.syncExperimentParams(e);
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
			this.syncExperimentParams(e);
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

	
	/**
	 * Just temporal method. Keeps synced specific params that are stored in ES with its originals in relational DB. 
	 * As soon as the bussiness code will be completely switched to GenericParameters, all original experiment properties will be dropped and this method will not be necessary.
	 * @param e 
	 */
	private void syncExperimentParams(Experiment e) {

		GenericParameter param;
		e.setGenericParameters(new ArrayList<GenericParameter>());

		for (Hardware hw : e.getHardwares()) {
			param = new GenericParameter("hardware", hw.getTitle());
			if (!"".equals(hw.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", hw.getDescription()));
			}
			e.getGenericParameters().add(param);
		}

		for (Software sw : e.getSoftwares()) {
			param = new GenericParameter("software", sw.getTitle());
			if (!"".equals(sw.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", sw.getDescription()));
			}

			e.getGenericParameters().add(param);
		}


		for (Disease dis : e.getDiseases()) {
			param = new GenericParameter("diesease", dis.getTitle());
			if (!"".equals(dis.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", dis.getDescription()));
			}

			e.getGenericParameters().add(param);
		}


		for (ProjectType type : e.getProjectTypes()) {
			param = new GenericParameter("projectType", type.getTitle());
			if (!"".equals(type.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", type.getDescription()));
			}

			e.getGenericParameters().add(param);
		}


		for (Pharmaceutical pharm : e.getPharmaceuticals()) {
			param = new GenericParameter("pharmaceutical", pharm.getTitle());
			if (!"".equals(pharm.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", pharm.getDescription()));
			}

			e.getGenericParameters().add(param);
		}


		Digitization d = e.getDigitization();
		param = new GenericParameter("digitization", d.getFilter());
		param.getAttributes().add(new ParameterAttribute("gain", "" + d.getGain()));
		param.getAttributes().add(new ParameterAttribute("samplingRate", "" + d.getSamplingRate()));
		e.getGenericParameters().add(param);


		Weather w = e.getWeather();
		param = new GenericParameter("weather", w.getTitle());
		if (!"".equals(w.getDescription())) {
			param.getAttributes().add(new ParameterAttribute("description", "" + w.getDescription()));
		}
		e.getGenericParameters().add(param);


		e.getGenericParameters().add(new GenericParameter("temperature", e.getTemperature()));
	}
}

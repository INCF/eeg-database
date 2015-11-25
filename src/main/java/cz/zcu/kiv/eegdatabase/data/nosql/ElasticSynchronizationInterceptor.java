/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * MyInterceptor.java, 2013/10/02 00:01 Martin Bydzovsky
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.nosql;

import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ParameterAttribute;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bydga
 */
public class ElasticSynchronizationInterceptor extends EmptyInterceptor {

    protected Log log = LogFactory.getLog(getClass());

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        boolean res = super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        if (entity instanceof Experiment) {
            Experiment e = (Experiment) entity;
            this.syncExperimentParams(e);
            e.getElasticExperiment().setExperimentId("" + id);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(e.getElasticExperiment());
            indexQuery.setId("" + id);
            this.elasticsearchTemplate.index(indexQuery);

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
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(e.getElasticExperiment());
            indexQuery.setId("" + id);
            this.elasticsearchTemplate.index(indexQuery);

        }
        
        return res;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        super.onDelete(entity, id, state, propertyNames, types);
        if (entity instanceof Experiment) {
            Experiment e = (Experiment) entity;
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.setQuery(new IdsQueryBuilder("experiment").addIds("" + e.getExperimentId()));
            this.elasticsearchTemplate.delete(deleteQuery);
        }
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        boolean res = super.onLoad(entity, id, state, propertyNames, types); // To change body of generated methods, choose Tools | Templates.
        if (entity instanceof Experiment) {
            Experiment e = (Experiment) entity;
          // try {
                if (e.getDataFiles().isEmpty()) {

//                If there are data files, the experiment is red for details, so metadata is needed
                   SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + e.getExperimentId())).build();
                   List<ExperimentElastic> elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
                   if (elastic.size() > 0 && elastic.get(0) != null) {
                       e.setElasticExperiment(elastic.get(0));
                   }
               }
//            } catch (LazyInitializationException ex) {
//                return res;
//            }

        }

        return res;
    }

    /**
     * Just temporal method. Keeps synced specific params that are stored in ES with its originals in relational DB. As soon as the bussiness code will be completely switched to
     * GenericParameters, all original experiment properties will be dropped and this method will not be necessary.
     * 
     * @param e
     */
    private void syncExperimentParams(Experiment e) {
        
        List<GenericParameter> syncedParams = getGenericParamaters("hardware", e.getGenericParameters());
        syncedParams.addAll(getGenericParamaters("software", e.getGenericParameters()));
        syncedParams.addAll(getGenericParamaters("diesease", e.getGenericParameters()));
        syncedParams.addAll(getGenericParamaters("pharmaceutical", e.getGenericParameters()));
        syncedParams.addAll(getGenericParamaters("digitization", e.getGenericParameters()));
        syncedParams.addAll(getGenericParamaters("temperature", e.getGenericParameters()));
        syncedParams.addAll(getGenericParamaters("weather", e.getGenericParameters()));
        log.trace("synced parameters " + syncedParams.size());
        
        GenericParameter param;
        log.trace("before remove all parameters " + e.getGenericParameters().size());
        e.getGenericParameters().removeAll(syncedParams);
        log.trace("after remove all parameters " + e.getGenericParameters().size());
        
        e.getElasticExperiment().setGroupId(e.getResearchGroup().getResearchGroupId());
        e.getElasticExperiment().setUserId(e.getPersonByOwnerId().getPersonId());

		for (Hardware hw : e.getHardwares()) {
			param = new GenericParameter("hardware", hw.getTitle());
			if (!"".equals(hw.getDescription())) {
				param.getAttributes().add(new ParameterAttribute("description", hw.getDescription()));
			}

			if (!"".equals(hw.getType())) {
				param.getAttributes().add(new ParameterAttribute("type", hw.getType()));
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
        if (d != null) {
            param = new GenericParameter("digitization", d.getFilter());
            param.getAttributes().add(new ParameterAttribute("gain", "" + d.getGain()));
            param.getAttributes().add(new ParameterAttribute("samplingRate", "" + d.getSamplingRate()));
            e.getGenericParameters().add(param);
        }

        Weather w = e.getWeather();
        if (w != null) {
            param = new GenericParameter("weather", w.getTitle());
            if (!"".equals(w.getDescription())) {
                param.getAttributes().add(new ParameterAttribute("description", "" + w.getDescription()));
            }
            e.getGenericParameters().add(param);
        }

		e.getGenericParameters().add(new GenericParameter("temperature", (double)e.getTemperature()));
	}
	
	public List<GenericParameter> getGenericParamaters(String paramName, List<GenericParameter> params) {
        List<GenericParameter> out = new ArrayList<GenericParameter>();
        for (GenericParameter p : params) {
            if (p.getName().equals(paramName)) {
                out.add(p);
            }
        }
        return out;
    }
}

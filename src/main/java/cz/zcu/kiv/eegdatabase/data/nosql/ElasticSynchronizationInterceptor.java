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
    private boolean loadSemantic;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        boolean res = super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        if (entity instanceof Experiment) {
            Experiment e = (Experiment) entity;
            e.getElasticExperiment().setExperimentId(Integer.parseInt("" + id));
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
            e.getElasticExperiment().setExperimentId(Integer.parseInt("" + id));
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
        log.debug("onLoad: " + entity);
        boolean res = super.onLoad(entity, id, state, propertyNames, types);
        if(loadSemantic) {
            if (entity instanceof Experiment) {
                Experiment e = (Experiment) entity;
                SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + e.getExperimentId())).build();
                List<ExperimentElastic> elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
                if (elastic.size() > 0 && elastic.get(0) != null) {
                    e.setElasticExperiment(elastic.get(0));
                }
            }
        }
        return res;
    }

    public boolean isLoadSemantic() {
        return loadSemantic;
    }

    public void setLoadSemantic(boolean loadSemantic) {
        this.loadSemantic = loadSemantic;
    }
}

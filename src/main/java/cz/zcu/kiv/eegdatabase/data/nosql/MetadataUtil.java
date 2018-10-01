package cz.zcu.kiv.eegdatabase.data.nosql;

import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the eeg-database project

 * ==========================================
 *
 * Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * MetadataUtil, 2018/10/01 11:37 petr-jezek
 *
 **********************************************************************************************************************/
public class MetadataUtil {

    protected Log log = LogFactory.getLog(getClass());

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    public MetadataUtil() {
        log.debug("MetadataUtil created");
    }

    public Experiment loadMetadata(Experiment experiment) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + experiment.getExperimentId())).build();
        List<ExperimentElastic> elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
        if (elastic.size() > 0 && elastic.get(0) != null) {
            experiment.setElasticExperiment(elastic.get(0));
        }
        return experiment;
    }
}

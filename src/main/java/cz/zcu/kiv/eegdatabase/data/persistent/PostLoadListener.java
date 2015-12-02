package cz.zcu.kiv.eegdatabase.data.persistent;

import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ParameterAttribute;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stebjan on 2.12.2015.
 */
public class PostLoadListener implements PostLoadEventListener {

    protected Log log = LogFactory.getLog(getClass());

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Object obj = event.getEntity();
        if (obj instanceof Experiment) {
            Experiment e = (Experiment) obj;
            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + e.getExperimentId())).build();
            List<ExperimentElastic> elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
            if (elastic.size() > 0 && elastic.get(0) != null) {
                e.setElasticExperiment(elastic.get(0));
            }
        }
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

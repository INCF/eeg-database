package cz.zcu.kiv.eegdatabase.data.persistent;

import cz.zcu.kiv.eegdatabase.data.nosql.MetadataUtil;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.hibernate.LazyInitializationException;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stebjan on 2.12.2015.
 */
public class PostLoadListener implements PostLoadEventListener {

    protected Log log = LogFactory.getLog(getClass());

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    MetadataUtil metadataUtil;

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Object obj = event.getEntity();
        if (obj instanceof Experiment) {
            Experiment e = (Experiment) obj;
            try {
                e.getDataFiles().size();
            } catch (LazyInitializationException ex) {
                metadataUtil.loadMetadata(e);
            }

        }

    }
}
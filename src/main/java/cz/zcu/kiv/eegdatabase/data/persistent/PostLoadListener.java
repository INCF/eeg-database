package cz.zcu.kiv.eegdatabase.data.persistent;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.annotation.Resource;

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
            e.getDataFiles().isEmpty();
        }
    }
}

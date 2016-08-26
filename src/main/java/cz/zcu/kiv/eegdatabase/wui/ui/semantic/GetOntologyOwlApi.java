package cz.zcu.kiv.eegdatabase.wui.ui.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.io.InputStream;

/**
 * Created by petr-jezek on 26.8.16.
 */
public class GetOntologyOwlApi  extends Page {

    private Log log = LogFactory.getLog(getClass());
    @SpringBean
    private SemanticFactory semanticFactory;

    public GetOntologyOwlApi(final PageParameters parameters) throws Exception {

        StringValue type = parameters.get("type");
        log.debug("type: " + type);
        InputStream is = semanticFactory.getOntologyOwlApi(type.toString());
        SemanticUtil.processSemanticStream(this, is);
    }
}

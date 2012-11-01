package cz.zcu.kiv.eegdatabase.wui.components.utils;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;

public class PageParametersUtils {

    public static PageParameters getPageParameters(String key, Object object) {

        PageParameters param = new PageParameters();
        param.add(key, object);

        return param;
    }

    public static PageParameters getDefaultPageParameters(Object object) {

        return getPageParameters(BasePage.DEFAULT_PARAM_ID, object);
    }

    public static PageParameters addParameters(PageParameters parameters,
            String key, Object object) {

        parameters.add(key, object);
        return parameters;
    }
}

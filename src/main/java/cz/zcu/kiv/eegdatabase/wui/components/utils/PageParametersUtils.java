package cz.zcu.kiv.eegdatabase.wui.components.utils;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;

/**
 * Utilities class for page parameters.
 * 
 * @author Jakub Rinkes
 * 
 */
public class PageParametersUtils {

    public static final String GROUP_PARAM = "GROUP";

    /**
     * Created page parameters with parameter key and object
     * 
     * @param key
     *            name of parameter
     * @param object
     *            value of parameter
     * @return page parameter object with parameter: key = object.
     */
    public static PageParameters getPageParameters(String key, Object object) {

        PageParameters param = new PageParameters();
        param.add(key, object);

        return param;
    }

    /**
     * Created page parameter with default parameter key and object. Default parameter is in BasePage.class.
     * 
     * @param object
     *            value of parameter
     * @return return page parameters with parameter: DEFAULT_PARAM_ID = object.
     */
    public static PageParameters getDefaultPageParameters(Object object) {

        return getPageParameters(BasePage.DEFAULT_PARAM_ID, object);
    }

    public static PageParameters addParameters(PageParameters parameters,
            String key, Object object) {

        parameters.add(key, object);
        return parameters;
    }

    /**
     * Get url for page and parameter from wicket. Wicket will generate absolute url for this page.
     * 
     * @param page
     * @param parameters
     * @return
     */
    public static String getUrlForPage(Class page, PageParameters parameters) {

        return RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(RequestCycle.get().urlFor(page, parameters).toString()));
    }
}

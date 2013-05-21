package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 14.5.13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareConverter implements IConverter<Software> {
    private SoftwareFacade softwareFacade;

    public SoftwareConverter(SoftwareFacade softwareFacade) {
        this.softwareFacade = softwareFacade;
    }

    @Override
    public Software convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        Software software = new Software();
        software.setTitle(s);
        List<Software> softwares = softwareFacade.getUnique(software);
        return (softwares != null && softwares.size() > 0) ? softwares.get(0) : software;

    }

    @Override
    public String convertToString(Software software, Locale locale) {
        return software.getTitle();
    }
}
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 12.5.13
 * Time: 15:18
 */
public class PharmaceuticalConverter implements IConverter<Pharmaceutical> {
    private PharmaceuticalFacade pharmaceuticalFacade;

    public PharmaceuticalConverter(PharmaceuticalFacade pharmaceuticalFacade) {
        this.pharmaceuticalFacade = pharmaceuticalFacade;
    }

    @Override
    public Pharmaceutical convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        List<Pharmaceutical> pharmaceuticals = pharmaceuticalFacade.readByParameter("title", s);
        return (pharmaceuticals != null && pharmaceuticals.size() > 0) ? pharmaceuticals.get(0) : new Pharmaceutical();
    }

    @Override
    public String convertToString(Pharmaceutical pharmaceutical, Locale locale) {
        return pharmaceutical.getAutoCompleteData();
    }
}

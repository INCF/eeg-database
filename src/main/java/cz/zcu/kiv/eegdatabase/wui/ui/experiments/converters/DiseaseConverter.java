package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DiseaseFacade;
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
public class DiseaseConverter implements IConverter<Disease> {
    private DiseaseFacade diseaseFacade;

    public DiseaseConverter(DiseaseFacade diseaseFacade) {
        this.diseaseFacade = diseaseFacade;
    }

    @Override
    public Disease convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        List<Disease> diseases = diseaseFacade.readByParameter("title", s);
        return (diseases != null && diseases.size() > 0) ? diseases.get(0) : null;

    }

    @Override
    public String convertToString(Disease disease, Locale locale) {
        return disease.getAutoCompleteData();
    }
}

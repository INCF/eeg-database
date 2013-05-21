package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 12.5.13
 * Time: 15:19
 */
public class ResearchGroupConverter implements IConverter<ResearchGroup> {
    private ResearchGroupFacade researchGroupFacade;

    public ResearchGroupConverter(ResearchGroupFacade researchGroupFacade){
        this.researchGroupFacade = researchGroupFacade;
    }

    @Override
    public ResearchGroup convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        ResearchGroup researchGroup = new ResearchGroup();
        researchGroup.setTitle(s);
        List<ResearchGroup> researchGroups = researchGroupFacade.getUnique(researchGroup);
        return (researchGroups != null && researchGroups.size() > 0) ? researchGroups.get(0) : researchGroup;
    }

    @Override
    public String convertToString(ResearchGroup researchGroup, Locale locale) {
        return researchGroup.getAutoCompleteData();
    }
}

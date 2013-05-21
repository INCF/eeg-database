package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

public class StimulusConverter implements IConverter<Stimulus> {
    private StimulusFacade stimulusFacade;

    public StimulusConverter(StimulusFacade stimulusFacade){
        this.stimulusFacade = stimulusFacade;
    }

    @Override
    public Stimulus convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        Stimulus stimulus = new Stimulus();
        stimulus.setDescription(s);
        List<Stimulus> stimuluses = stimulusFacade.getUnique(stimulus);
        return (stimuluses != null && stimuluses.size() > 0) ? stimuluses.get(0) : stimulus;
    }

    @Override
    public String convertToString(Stimulus stimulus, Locale locale) {
        return stimulus.getAutoCompleteData();
    }
}

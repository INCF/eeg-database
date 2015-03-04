package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;

public class ListTemplateProvider extends BasicDataProvider<Template> {

    private static final long serialVersionUID = 580989629929303867L;
    
    public ListTemplateProvider(TemplateFacade facade, int personId) {
        super("templateId", SortOrder.ASCENDING);
        
        List<Template> list = facade.getTemplatesByPerson(personId);
        super.listModel.setObject(list);
    }
}

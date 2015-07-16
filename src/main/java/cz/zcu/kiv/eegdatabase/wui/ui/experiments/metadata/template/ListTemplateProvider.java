/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ListTemplateProvider.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
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
    
    public ListTemplateProvider(TemplateFacade facade) {
        super("templateId", SortOrder.ASCENDING);
        
        List<Template> list = facade.getDefaultTemplates();
        super.listModel.setObject(list);
    }
}

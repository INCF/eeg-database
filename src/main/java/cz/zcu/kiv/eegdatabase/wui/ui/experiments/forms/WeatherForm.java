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
 *   WeatherForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRelId;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

public class WeatherForm extends Form<Weather> {

    private static final long serialVersionUID = 5565668130652416049L;

    @SpringBean
    private WeatherFacade facade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    public WeatherForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Weather>(new Weather()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addWeatherHeader", ResourceUtils.getModel("pageTitle.addWeatherDefinition")));

        ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
        final DropDownChoice<ResearchGroup> researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new Model<ResearchGroup>(), choices, renderer);

        researchGroupChoice.setRequired(true);
        researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
        add(researchGroupChoice);

        TextField<String> title = new TextField<String>("title");
        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.title"));
        add(title);

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.description"));
        add(description);

        add(new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                Weather weather = WeatherForm.this.getModelObject();

                int weatherId = weather.getWeatherId();
                ResearchGroup group = researchGroupChoice.getModelObject();
                int researchGroupId = group.getResearchGroupId();

                if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                    if (!facade.canSaveDefaultTitle(weather.getTitle(), weatherId)) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        target.add(feedback);
                        return;
                    }
                } else {
                    if (!facade.canSaveTitle(weather.getTitle(), researchGroupId, weatherId)) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        target.add(feedback);
                        return;
                    }
                }

                // Creating new
                if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                    facade.createDefaultRecord(weather);
                    window.close(target);
                } else {
                    int pkWeather = facade.create(weather);
                    WeatherGroupRelId weatherGroupRelId = new WeatherGroupRelId(pkWeather, researchGroupId);
                    WeatherGroupRel weatherGroupRel = new WeatherGroupRel(weatherGroupRelId, group, weather);
                    facade.createGroupRel(weatherGroupRel);
                    window.close(target);
                }
                
            }
            
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
            
        });
        
        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {
            
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
    }
}

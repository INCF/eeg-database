package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate.XMLTemplateReader;
import cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate.XMLTemplateWriter;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.model.LoadableListModel;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.TemplateFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SectionCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SubsectionsCell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the ${PROJECT_NAME} project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * ${NAME}, 2014/07/01 11:57 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
@AuthorizeInstantiation(value = {"ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN"})
public class ExperimentFormPageTest extends MenuPage {

    protected static Log log = LogFactory.getLog(ExperimentFormPageTest.class);

    @SpringBean
    private TemplateFacade templateFacade;

    public ExperimentFormPageTest() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        //----------dropdown---------
        ChoiceRenderer<Template> renderer = new ChoiceRenderer<Template>("name", "templateId");
        LoadableListModel<Template> choiceModel = new LoadableListModel<Template>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Template> load() {
                return getTemplates();
            }

        };
        final DropDownChoice<Template> dropDownChoice = new DropDownChoice<Template>("templateDD",
                new Model<Template>((choiceModel.getObject().get(0))), choiceModel, renderer);
        //---------form---------
        final List<SectionType> sections = readTemplate(dropDownChoice.getModelObject());

        IModel<List<SectionType>> formModel
                = new CompoundPropertyModel<List<SectionType>>(sections);
        final Form<List<SectionType>> form = new Form<List<SectionType>>("form", formModel){
            @Override
            protected void onSubmit() {
                super.onSubmit();
            }
        };

        form.setOutputMarkupId(true);
        //--------listView-------
        final ListView<SectionType> view = new PropertyListView<SectionType>("row", form.getModel()) {
            @Override
            protected void populateItem(ListItem item) {
                SectionCell sectionCell = new SectionCell("cell1", item.getModel());
                SubsectionsCell subsectionsCell = new SubsectionsCell("cell2", item.getModel());
                item.add(sectionCell);
                item.add(subsectionsCell);
            }
        };
        view.setOutputMarkupId(true);
        //-----save components-----
        Form saveForm = new Form("saveForm");
        final TextField<String> saveName = new TextField<String>("saveText",
                Model.of(dropDownChoice.getModelObject().getName()));
        saveName.setOutputMarkupId(true);

        final Button save = new Button("saveButton"){
            @Override
            public void onSubmit() {
                List<SectionType> sections = form.getModelObject();
                String name = saveName.getModelObject();
                Person user = EEGDataBaseSession.get().getLoggedUser();
                Template template = templateFacade.getTemplateByPersonAndName(user.getPersonId(), name);
                //template with same name exists => update
                if(template != null){
                    try {
                        template = createTemplate(sections, template);
                        templateFacade.update(template);
                    } catch (XMLStreamException e) {
                        log.error(e.getMessage(), e);
                        error(ResourceUtils.getString("error.writingTemplate"));
                        return;
                    }

                } else { //template does not exists => create
                    try {
                        template = createTemplate(sections, name);
                        templateFacade.create(template);
                    } catch (XMLStreamException e) {
                        log.error(e.getMessage(), e);
                        error(ResourceUtils.getString("error.writingTemplate"));
                        return;
                    }
                }
            }
        };
        //---------behavior---------
        OnChangeAjaxBehavior onChangeBehavior = new OnChangeAjaxBehavior() {
            protected void onUpdate(AjaxRequestTarget target) {
                form.setDefaultModel(
                        new CompoundPropertyModel<List<SectionType>>(readTemplate(dropDownChoice.getModelObject())));
                view.setDefaultModel(form.getModel());
                form.add(view);
                saveName.setDefaultModel(Model.of(dropDownChoice.getModelObject().getName()));
                target.add(form);
                target.add(saveName);
            }
        };
        //---------------------------

        dropDownChoice.add(onChangeBehavior);
        form.add(view);
        add(dropDownChoice);
        add(form);
        saveForm.add(save);
        saveForm.add(saveName);
        add(saveForm);
    }

    /**
     * Loads default and user's templates from database
     * @return templates
     */
    private List<Template> getTemplates(){
        int loggedUserId = EEGDataBaseSession.get().getLoggedUser().getPersonId();
        List<Template> userTemplates = templateFacade.getUsableTemplates(loggedUserId);
        if (userTemplates == null) userTemplates = new ArrayList<Template>();

        return userTemplates;
    }

    /**
     * Reads xml template into list of sections
     * @param template selected template
     * @return list of sections
     */
    private List<SectionType> readTemplate(Template template)
    {
        List<SectionType> sections = new ArrayList<SectionType>();

        try {
            sections = XMLTemplateReader.readTemplate(template.getTemplate());
        } catch (XMLStreamException e) {
            log.error(e.getMessage(), e);
        }

        return sections;
    }

    private Template createTemplate(List<SectionType> sections, String name) throws XMLStreamException {
        Template template = new Template();
            byte[] xmlData = XMLTemplateWriter.writeTemplate(sections);
            template.setTemplate(xmlData);
            template.setIsDefault(false);
            template.setName(name);
            template.setPersonByPersonId(EEGDataBaseSession.get().getLoggedUser());

        return template;
    }

    private Template createTemplate(List<SectionType> sections, Template template) throws XMLStreamException {
            byte[] xmlData = XMLTemplateWriter.writeTemplate(sections);
            template.setTemplate(xmlData);

        return template;
    }
}

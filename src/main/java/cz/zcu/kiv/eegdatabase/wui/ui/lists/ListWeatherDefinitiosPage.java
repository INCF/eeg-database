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
 *   ListWeatherDefinitiosPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ResearchGroupSelectForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.WeatherFormPage;

/**
 * Page with list of weather definitions.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListWeatherDefinitiosPage extends MenuPage {

    private static final long serialVersionUID = 7423167902623052151L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    WeatherFacade facade;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade security;

    public ListWeatherDefinitiosPage() {

        setupComponents();
    }

    private void setupComponents() {

        setPageTitle(ResourceUtils.getModel("pageTitle.weatherDefinitionsList"));

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupPlaceholderTag(true);

        final ListModelWithResearchGroupCriteria<Weather> model = new ListModelWithResearchGroupCriteria<Weather>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Weather> loadList(ResearchGroup group) {
                if (group == null || group.getResearchGroupId() == CoreConstants.DEFAULT_ITEM_ID)
                    return facade.getDefaultRecords();
                else {
                    return facade.getRecordsByGroup(group.getResearchGroupId());
                }
            }
        };

        List<ResearchGroup> groups;
        final boolean isAdmin = security.isAdmin();
        final boolean isExperimenter = security.userIsExperimenter();
        if (isAdmin) {
            ResearchGroup defaultGroup = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID,
                    EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultWeather"), "-");
            groups = researchGroupFacade.getAllRecords();
            groups.add(0, defaultGroup);
        } else
            groups = researchGroupFacade.getResearchGroupsWhereMember(EEGDataBaseSession.get().getLoggedUser());

        PropertyListView<Weather> weathers = new PropertyListView<Weather>("weathers", model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Weather> item) {
                item.add(new Label("weatherId"));
                item.add(new Label("title"));
                item.add(new Label("description"));

                PageParameters parameters = PageParametersUtils.getDefaultPageParameters(item.getModelObject().getWeatherId())
                        .add(PageParametersUtils.GROUP_PARAM,
                                (model.getCriteriaModel().getObject() == null) ? CoreConstants.DEFAULT_ITEM_ID : model.getCriteriaModel().getObject().getResearchGroupId());

                item.add(new BookmarkablePageLink<Void>("edit", WeatherFormPage.class, parameters));
                item.add(new AjaxLink<Void>("delete") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        int id = item.getModelObject().getWeatherId();
                        ResearchGroup group = model.getCriteriaModel().getObject();

                        if (facade.canDelete(id)) {
                            if (group != null) {
                                int groupId = group.getResearchGroupId();

                                if (groupId == CoreConstants.DEFAULT_ITEM_ID) { // delete default weather if it's from default group
                                    if (!facade.hasGroupRel(id)) { // delete only if it doesn't have group relationship
                                        facade.delete(item.getModelObject());
                                        setResponsePage(ListWeatherDefinitiosPage.class);
                                    } else {
                                        getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    }
                                } else {
                                    WeatherGroupRel h = facade.getGroupRel(id, groupId);
                                    if (!facade.isDefault(id)) { // delete only non default weather
                                        facade.delete(item.getModelObject());
                                    }
                                    facade.deleteGroupRel(h);
                                    setResponsePage(ListWeatherDefinitiosPage.class);
                                }
                            }

                        } else {
                            getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                        }

                        target.add(container);
                        target.add(getFeedback());
                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
                    {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('Are you sure you want to delete item?');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }

                }.setVisibilityAllowed(isAdmin || isExperimenter));

            }
        };
        container.add(weathers);

        AjaxLink<Void> link = new AjaxLink<Void>("addWeatherLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                int researchGroupId = (model.getCriteriaModel().getObject() == null) ? CoreConstants.DEFAULT_ITEM_ID : model.getCriteriaModel().getObject().getResearchGroupId();
                setResponsePage(WeatherFormPage.class, PageParametersUtils.getPageParameters(PageParametersUtils.GROUP_PARAM, researchGroupId));
            }
        };
        link.setVisibilityAllowed(isAdmin || isExperimenter);

        add(new ResearchGroupSelectForm("form", model.getCriteriaModel(), new ListModel<ResearchGroup>(groups), container, isAdmin));
        add(link, container);

    }

}

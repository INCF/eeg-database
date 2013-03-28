package cz.zcu.kiv.eegdatabase.wui.ui.lists.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.StringValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRelId;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListWeatherDefinitiosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WeatherFormPage extends MenuPage {

    private static final long serialVersionUID = 877654540988964188L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    WeatherFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public WeatherFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue weatherParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListWeatherDefinitiosPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (weatherParam.isNull() || weatherParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), weatherParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addWeatherDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addWeatherDefinition")));

        Weather newWeather = new Weather();

        add(new WeatherForm("form", new CompoundPropertyModel<Weather>(newWeather), new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int weatherId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.editWeatherDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editWeatherDefinition")));

        add(new WeatherForm("form", new CompoundPropertyModel<Weather>(facade.read(weatherId)),
                new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private ResearchGroup getGroup(int researchGroupId) {
        ResearchGroup group;
        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID)
            group = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID, EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultWeather"), "-");
        else
            group = groupFacade.read(researchGroupId);
        return group;
    }

    private class WeatherForm extends Form<Weather> {

        private static final long serialVersionUID = 1L;

        public WeatherForm(String id, IModel<Weather> model, final Model<ResearchGroup> groupModel,
                final FeedbackPanel feedback, final WeatherFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> title = new TextField<String>("title");
            title.setLabel(ResourceUtils.getModel("label.title"));
            title.add(StringValidator.maximumLength(30));
            title.setRequired(true);

            TextField<String> description = new TextField<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(30));
            description.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);

                    Weather weather = WeatherForm.this.getModelObject();

                    int weatherId = weather.getWeatherId();
                    ResearchGroup group = groupModel.getObject();
                    int researchGroupId = group.getResearchGroupId();
                    
                    if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                        if (!facade.canSaveDefaultTitle(weather.getTitle(), weatherId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                        if (!facade.canSaveDefaultDescription(weather.getDescription(), weatherId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    } else {
                        if (!facade.canSaveTitle(weather.getTitle(), researchGroupId, weatherId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                        if (!facade.canSaveDescription(weather.getTitle(), researchGroupId, weatherId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    }

                    if (weatherId > 0) {
                        // Editing one
                        log.debug("Editing existing weather object.");

                        if (facade.isDefault(weather.getWeatherId())) {

                            if (researchGroupId != CoreConstants.DEFAULT_ITEM_ID) {
                                // new weather
                                Weather newWeather = new Weather();
                                newWeather.setDefaultNumber(0);
                                newWeather.setTitle(weather.getTitle());
                                newWeather.setDescription(weather.getDescription());
                                int newId = facade.create(newWeather);
                                WeatherGroupRel rel = facade.getGroupRel(weatherId, researchGroupId);
                                // delete old rel, create new one
                                WeatherGroupRelId newRelId = new WeatherGroupRelId();
                                WeatherGroupRel newRel = new WeatherGroupRel();
                                newRelId.setWeatherId(newId);
                                newRelId.setResearchGroupId(researchGroupId);
                                newRel.setId(newRelId);
                                newRel.setWeather(newWeather);
                                newRel.setResearchGroup(group);
                                facade.deleteGroupRel(rel);
                                facade.createGroupRel(newRel);
                            } else {
                                if (!facade.hasGroupRel(weatherId) && facade.canDelete(weatherId)) {
                                    facade.update(weather);
                                } else {
                                    getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    this.setEnabled(true);
                                    return;
                                }
                            }
                        } else {
                            facade.update(weather);
                        }
                    } else {

                        // Creating new
                        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                            log.debug("Creating new default weather object.");
                            facade.createDefaultRecord(weather);
                        } else {
                            log.debug("Creating new group weather object.");
                            int pkWeather = facade.create(weather);

                            WeatherGroupRelId weatherGroupRelId = new WeatherGroupRelId(pkWeather, researchGroupId);

                            WeatherGroupRel weatherGroupRel = new WeatherGroupRel(weatherGroupRelId, group, weather);
                            facade.createGroupRel(weatherGroupRel);
                        }

                    }

                    setResponsePage(ListWeatherDefinitiosPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, description, title);
        }

    }
}

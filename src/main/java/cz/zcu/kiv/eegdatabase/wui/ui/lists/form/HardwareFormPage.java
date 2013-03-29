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

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListHardwareDefinitionsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

/**
 * Page add / edit action of hardware definitions.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class HardwareFormPage extends MenuPage {

    private static final long serialVersionUID = -761588925622549613L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    HardwareFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public HardwareFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue hardwareParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListHardwareDefinitionsPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (hardwareParam.isNull() || hardwareParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), hardwareParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addHardwareDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addHardwareDefinition")));

        Hardware newHardware = new Hardware();

        add(new HardwareForm("form", new CompoundPropertyModel<Hardware>(newHardware), new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int hardwareId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.editHardwareDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editHardwareDefinition")));

        add(new HardwareForm("form", new CompoundPropertyModel<Hardware>(facade.read(hardwareId)),
                new Model<ResearchGroup>(getGroup(researchGroupId)), getFeedback(), facade));
    }

    private ResearchGroup getGroup(int researchGroupId) {
        ResearchGroup group;
        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID)
            group = new ResearchGroup(CoreConstants.DEFAULT_ITEM_ID, EEGDataBaseSession.get().getLoggedUser(), ResourceUtils.getString("label.defaultHardware"), "-");
        else
            group = groupFacade.read(researchGroupId);
        return group;
    }

    private class HardwareForm extends Form<Hardware> {

        private static final long serialVersionUID = 1L;

        public HardwareForm(String id, IModel<Hardware> model, final Model<ResearchGroup> groupModel,
                final FeedbackPanel feedback, final HardwareFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> title = new TextField<String>("title");
            title.setLabel(ResourceUtils.getModel("label.title"));
            title.setRequired(true);

            TextField<String> type = new TextField<String>("type");
            type.setLabel(ResourceUtils.getModel("label.type"));
            type.setRequired(true);

            TextField<String> description = new TextField<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);
                    
                    Hardware hardware = HardwareForm.this.getModelObject();

                    int hardwareId = hardware.getHardwareId();
                    ResearchGroup group = groupModel.getObject();
                    int researchGroupId = group.getResearchGroupId();
                    
                    if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                        if (!facade.canSaveDefaultTitle(hardware.getTitle(), hardwareId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    } else {
                        if (!facade.canSaveTitle(hardware.getTitle(), researchGroupId, hardwareId)) {
                            getFeedback().error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                            return;
                        }
                    }

                    if (hardwareId > 0) {
                        // Editing one
                        log.debug("Editing existing hardware object.");

                        if (facade.isDefault(hardwareId)) {

                            if (researchGroupId != CoreConstants.DEFAULT_ITEM_ID) {
                                // new hardware
                                Hardware newHw = new Hardware();
                                newHw.setDefaultNumber(0);
                                newHw.setDescription(hardware.getDescription());
                                newHw.setTitle(hardware.getTitle());
                                newHw.setType(hardware.getType());
                                int newId = facade.create(newHw);
                                HardwareGroupRel rel = facade.getGroupRel(hardwareId, researchGroupId);
                                // delete old rel, create new one
                                HardwareGroupRelId newRelId = new HardwareGroupRelId();
                                HardwareGroupRel newRel = new HardwareGroupRel();
                                newRelId.setHardwareId(newId);
                                newRelId.setResearchGroupId(researchGroupId);
                                newRel.setId(newRelId);
                                newRel.setHardware(newHw);
                                newRel.setResearchGroup(group);
                                facade.deleteGroupRel(rel);
                                facade.createGroupRel(newRel);
                            } else {
                                if (!facade.hasGroupRel(hardwareId) && facade.canDelete(hardwareId)) {
                                    facade.update(hardware);
                                } else {
                                    getFeedback().error(ResourceUtils.getString("text.itemInUse"));
                                    this.setEnabled(true);
                                    return;
                                }
                            }
                        } else {
                            facade.update(hardware);
                        }
                    } else {

                        // Creating new
                        if (researchGroupId == CoreConstants.DEFAULT_ITEM_ID) {
                            log.debug("Creating new default hardware object.");
                            facade.createDefaultRecord(hardware);
                        } else {
                            log.debug("Creating new group hardware object.");
                            int pkHardware = facade.create(hardware);

                            HardwareGroupRelId hardwareGroupRelId = new HardwareGroupRelId(pkHardware, researchGroupId);
                            HardwareGroupRel hardwareGroupRel = new HardwareGroupRel(hardwareGroupRelId, group, hardware);
                            facade.createGroupRel(hardwareGroupRel);
                        }

                    }
                    
                    setResponsePage(ListHardwareDefinitionsPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, description, title, type);
        }

    }
}

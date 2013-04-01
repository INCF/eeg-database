package cz.zcu.kiv.eegdatabase.wui.ui.lists.form;

import java.util.HashSet;
import java.util.Set;

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

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.ArtifactFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListArtifactDefinitionsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsLeftPageMenu;

/**
 * Page add / edit action of artifact definitions.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArtifactFormPage extends MenuPage {

    private static final long serialVersionUID = 7504584906065279245L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ArtifactFacade facade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public ArtifactFormPage(PageParameters parameters) {

        StringValue groupParam = parameters.get(PageParametersUtils.GROUP_PARAM);
        StringValue artifactParam = parameters.get(DEFAULT_PARAM_ID);

        if (groupParam.isNull() || groupParam.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListArtifactDefinitionsPage.class);

        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));

        if (artifactParam.isNull() || artifactParam.isEmpty())
            setupAddComponents(groupParam.toInt());
        else
            setupEditComponents(groupParam.toInt(), artifactParam.toInt());
    }

    private void setupAddComponents(int researchGroupId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addArtifactDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addArtifactDefinition")));

        Artifact newArtifact = new Artifact();
        Set<ResearchGroup> groups = new HashSet<ResearchGroup>(0);
        ResearchGroup group = groupFacade.read(researchGroupId);
        groups.add(group);
        newArtifact.setResearchGroups(groups);

        add(new ArtifactForm("form", new CompoundPropertyModel<Artifact>(newArtifact), new Model<ResearchGroup>(group), getFeedback(), facade));
    }

    private void setupEditComponents(int researchGroupId, int artifactId) {
        setPageTitle(ResourceUtils.getModel("pageTitle.addEditArtifactDefinition"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addEditArtifactDefinition")));

        add(new ArtifactForm("form", new CompoundPropertyModel<Artifact>(facade.read(artifactId)),
                new Model<ResearchGroup>(groupFacade.read(researchGroupId)), getFeedback(), facade));
    }

    private class ArtifactForm extends Form<Artifact> {

        private static final long serialVersionUID = 1L;

        public ArtifactForm(String id, IModel<Artifact> model, Model<ResearchGroup> groupModel, final FeedbackPanel feedback, final ArtifactFacade facade) {
            super(id, model);

            TextField<String> group = new TextField<String>("group", new PropertyModel<String>(groupModel.getObject(), "title"));
            group.setEnabled(false);
            group.setLabel(ResourceUtils.getModel("label.researchGroup"));

            TextField<String> compensation = new TextField<String>("compensation");
            compensation.setLabel(ResourceUtils.getModel("label.compensation"));
            compensation.setRequired(true);

            TextField<String> rejectCondition = new TextField<String>("rejectCondition");
            rejectCondition.setLabel(ResourceUtils.getModel("label.rejectCondition"));
            rejectCondition.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    this.setEnabled(false);
                    target.add(this);
                    target.add(feedback);
                    
                    Artifact artifact = ArtifactForm.this.getModelObject();

                    if (artifact.getArtifactId() > 0) {
                        // Editing
                        log.debug("Editing existing artifact object.");
                        facade.update(artifact);
                    } else {
                        facade.create(artifact);
                    }

                    setResponsePage(ListArtifactDefinitionsPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            };

            add(submit, group, compensation, rejectCondition);
        }

    }

}

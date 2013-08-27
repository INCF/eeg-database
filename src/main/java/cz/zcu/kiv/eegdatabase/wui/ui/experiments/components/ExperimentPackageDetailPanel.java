package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseEditForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageDetailPanel extends Panel {

	@SpringBean
	private LicenseFacade licenseFacade;
	@SpringBean
	private ExperimentPackageFacade experimentPackageFacade;

	private IModel<ResearchGroup> resGroupModel;
	private IModel<ExperimentPackage> packageModel;
	private IModel<License> licenseModel;
	private IModel<List<License>> optionsModel;

	private Form form;
	private ModalWindow addLicenseWindow;
	private DropDownChoice<License> licenseSelect;

	public ExperimentPackageDetailPanel(String id, IModel<ResearchGroup> resGroup) {
		super(id);
		this.packageModel = new Model<ExperimentPackage>(new ExperimentPackage());
		this.licenseModel = new Model<License>();
		
		this.resGroupModel = resGroup;
		form = new StatelessForm("form");
		this.add(form);

		addBasicInfoFields();
		addLicenseSelect();
		addLicenseAddWindow();
		addControls();
	}


	private void addBasicInfoFields() {
		FormComponent c = new RequiredTextField("name", new PropertyModel(packageModel, "name"));
		c.setLabel(ResourceUtils.getModel("label.experimentPackage.name"));

		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
	}

	private void generateLicenseChoices() {
		optionsModel = new ListModelWithResearchGroupCriteria<License>() {

			@Override
			protected List<License> loadList(ResearchGroup group) {
				List<License> l = licenseFacade.getLicenseTemplates(group);
				l.add(0, licenseFacade.getPublicLicense());
				if(group.isPaidAccount()) {
					l.add(1, licenseFacade.getOwnerLicense(group));
				}
				return l;
			}
		};

		((ListModelWithResearchGroupCriteria) optionsModel).setCriteriaModel(resGroupModel);
	}

	private void addLicenseSelect() {
		generateLicenseChoices();
		licenseSelect = new DropDownChoice<License>("licensePolicy", licenseModel, optionsModel, new IChoiceRenderer<License>() {

			@Override
			public Object getDisplayValue(License object) {
				return object.getTitle();
			}

			@Override
			public String getIdValue(License object, int index) {
				return String.valueOf(index);
			}
		});
		licenseSelect.setOutputMarkupId(true);

		form.add(licenseSelect);
	}

	/**
	 * Add window which allows to add new license to the package.
	 */
	private void addLicenseAddWindow() {
		addLicenseWindow = new ModalWindow("addLicenseWindow");
		addLicenseWindow.setAutoSize(true);
		addLicenseWindow.setResizable(false);
		addLicenseWindow.setMinimalWidth(600);
		addLicenseWindow.setWidthUnit("px");

		IModel<List<License>> blpModel = new LoadableDetachableModel<List<License>>() {
			@Override
			protected List<License> load() {
				return licenseFacade.getLicenseTemplates(resGroupModel.getObject());
			}
		};

		Panel content = new LicenseEditForm(addLicenseWindow.getContentId(), new Model<License>(new License()), blpModel, new PropertyModel<Boolean>(resGroupModel, "paidAccount")) {
			@Override
			protected void onSubmitAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
				License obj = model.getObject();
				obj.setTemplate(true);
				obj.setResearchGroup(resGroupModel.getObject());
				licenseFacade.create(obj);
				//close window
				ModalWindow.closeCurrent(target);
				//update license templates
				optionsModel.detach();
				target.add(licenseSelect);
			}

			@Override
			protected void onCancelAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
				ModalWindow.closeCurrent(target);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.licenseModel.setObject(new License());
			}

		}.setDisplayRemoveButton(false);

		addLicenseWindow.setContent(content);
		this.add(addLicenseWindow);
	}

	private void addControls() {
		AjaxButton b = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				ExperimentPackage pck = packageModel.getObject();
				pck.setResearchGroup(resGroupModel.getObject());
				experimentPackageFacade.createExperimentPackage(pck, licenseModel.getObject());
				ExperimentPackageDetailPanel.this.onSubmitAction(target, form);
			}
			
		};

		form.add(b);

		AjaxLink addLink = new AjaxLink("addLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				addLicenseWindow.show(target);
			}

		};
		form.add(addLink);

	}

	protected void onSubmitAction(AjaxRequestTarget target, Form<?> form) {
		
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		if(this.resGroupModel.getObject() != null) {
			this.generateLicenseChoices();
			packageModel.setObject(new ExperimentPackage());

			licenseModel.setObject(licenseFacade.getPublicLicense());
		}
	}

}

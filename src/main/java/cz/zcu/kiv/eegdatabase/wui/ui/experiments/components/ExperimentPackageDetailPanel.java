package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.license.LicenseEditForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
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

	private enum LicensePolicy {
		PUBLIC,
		PRIVATE,
		LICENSED
	}

	private IModel<ResearchGroup> resGroupModel;
	private IModel<ExperimentPackage> packageModel;
	private IModel<License> licenseModel;
	private IModel<LicensePolicy> policy;

	private boolean  licenseFormVisible;

	private Form form;
	private LicenseEditForm licenseForm;

	public ExperimentPackageDetailPanel(String id, IModel<ResearchGroup> resGroup) {
		super(id);
		this.packageModel = new Model<ExperimentPackage>(new ExperimentPackage());
		this.licenseModel = new Model<License>();
		policy = new Model<LicensePolicy>();
		
		this.resGroupModel = resGroup;
		form = new StatelessForm("form");
		this.add(form);

		addBasicInfoFields();
		addLicenseTypeSelect();
		addLicenseForm();
		addControls();
	}


	private void addBasicInfoFields() {
		FormComponent c = new RequiredTextField("name", new PropertyModel(packageModel, "name"));
		c.setLabel(ResourceUtils.getModel("label.experimentPackage.name"));

		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
	}

	private List<LicensePolicy> generateLicenseTypeChoices() {
		return Arrays.asList(LicensePolicy.values());
	}

	private void addLicenseTypeSelect() {
		DropDownChoice<LicensePolicy> policySelect = new AjaxDropDownChoice<LicensePolicy>("licensePolicy", policy, generateLicenseTypeChoices(), new EnumChoiceRenderer<LicensePolicy>()) {

			@Override
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, LicensePolicy option) {
				switch(option) {
					case PUBLIC:
						licenseModel.setObject(licenseFacade.getPublicLicense());
						licenseFormVisible = false;
						break;
					case PRIVATE:
						licenseModel.setObject(licenseFacade.getOwnerLicense(resGroupModel.getObject()));
						licenseFormVisible = false;
						break;
					case LICENSED:
						licenseModel.setObject(new License());
						licenseFormVisible = true;
						break;
				}

				target.add(licenseForm);
			}

		};

		form.add(policySelect);
	}

	private void addLicenseForm() {
		IModel<List<License>> blpModel = new LoadableDetachableModel<List<License>>() {

			@Override
			protected List<License> load() {
				List<LicenseType> typs = new ArrayList<LicenseType>();
				typs.add(LicenseType.ACADEMIC);
				typs.add(LicenseType.BUSINESS);
				return licenseFacade.getLicensesForGroup(resGroupModel.getObject(), typs);
			}
			
		};
		licenseForm = new LicenseEditForm("licenseForm", licenseModel, blpModel) {

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(licenseFormVisible);
			}

		};
		licenseForm.setDisplayControls(false);
		licenseForm.setOutputMarkupPlaceholderTag(true);
		form.add(licenseForm);
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

	}

	protected void onSubmitAction(AjaxRequestTarget target, Form<?> form) {
		
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		packageModel.setObject(new ExperimentPackage());

		licenseModel.setObject(licenseFacade.getPublicLicense());
		policy.setObject(LicensePolicy.PUBLIC);

		this.licenseFormVisible = false;
	}

}

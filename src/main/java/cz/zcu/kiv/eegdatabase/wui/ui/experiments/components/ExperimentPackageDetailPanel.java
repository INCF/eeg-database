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
 *   ExperimentPackageDetailPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.LicenseEditForm;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.components.ListModelWithResearchGroupCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageDetailPanel extends Panel {

	@SpringBean
	private LicenseFacade licenseFacade;
	@SpringBean
	private ExperimentPackageFacade experimentPackageFacade;

    @SpringBean
    private ExperimentPackageLicenseFacade experimentPackageLicenseFacade;

	private IModel<ResearchGroup> resGroupModel;
	private IModel<ExperimentPackage> packageModel;
	private IModel<License> licenseModel;
	private IModel<List<License>> optionsModel;

	private Form form;
	private ModalWindow addLicenseWindow;
	private DropDownChoice<License> licenseSelect;
    private LicenseEditForm licenseEditForm;

    private List<License> licenses;

	public ExperimentPackageDetailPanel(String id, IModel<ResearchGroup> resGroup) {
		super(id);
		this.packageModel = new Model<ExperimentPackage>(new ExperimentPackage());
		this.licenseModel = new Model<License>();
		this.licenses = new ArrayList<License>();
		this.resGroupModel = resGroup;
		form = new StatelessForm("form");
		this.add(form);

		addBasicInfoFields();
		addLicenseSelect();
		addLicenseAddWindow();
		addControls();
	}


	private void addBasicInfoFields() {
		FormComponent c = new TextField("name", new PropertyModel(packageModel, "name"));
        c.setRequired(true);
		c.setLabel(ResourceUtils.getModel("label.experimentPackage.name"));

		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
	}

	
	private void generateLicenseChoices() {
		optionsModel = new ListModelWithResearchGroupCriteria<License>() {

			@Override
			protected List<License> loadList(ResearchGroup group) {
				//List<License> l = licenseFacade.getLicenseTemplates();
				//return l;
                return licenses;
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
		addLicenseWindow.setResizable(true);
		addLicenseWindow.setMinimalWidth(600);
		addLicenseWindow.setWidthUnit("px");
        addLicenseWindow.showUnloadConfirmation(false);
        addLicenseWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {
                licenseEditForm.clearLicenseModel();
            }
        });

		IModel<List<License>> blpModel = new LoadableDetachableModel<List<License>>() {
			@Override
			protected List<License> load() {
				return licenseFacade.getLicenseTemplates();
			}
		};

		licenseEditForm = new LicenseEditForm(addLicenseWindow.getContentId(), new Model<License>(new License()), blpModel) {
		    
			@Override
			protected void onSubmitAction(IModel<ExperimentPackageLicense> model, AjaxRequestTarget target, Form<?> form) {
				License obj = model.getObject().getLicense();

                if (obj.getLicenseId() == 0) {
                    obj.setTemplate(false);
                    obj.setResearchGroup(resGroupModel.getObject());
                    //licenseFacade.create(obj);
                    //}
                    ModalWindow.closeCurrent(target);
                    //update license templates
                    licenses.add(obj);
                    optionsModel.detach();
                    target.add(licenseSelect);
                }

			}

			@Override
			protected void onCancelAction(IModel<ExperimentPackageLicense> model, AjaxRequestTarget target, Form<?> form) {
				ModalWindow.closeCurrent(target);
			}

			/*@Override
			protected void onConfigure() {
				super.onConfigure();
				this.licenseModel.setObject(new License());
			}*/

		}.setDisplayRemoveButton(false);

		addLicenseWindow.setContent(licenseEditForm);
		this.add(addLicenseWindow);
	}

	private void addControls() {
		AjaxButton b = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
                this.setEnabled(false);

				ExperimentPackage pck = packageModel.getObject();
				pck.setResearchGroup(resGroupModel.getObject());
                //experimentPackageFacade.createExperimentPackage(pck, licenseModel.getObject());
                experimentPackageFacade.create(pck);
                ExperimentPackageLicense experimentPackageLicense;

                for (License lic : licenses) {
                    licenseFacade.create(lic);
                    experimentPackageLicense = new ExperimentPackageLicense();
                    experimentPackageLicense.setExperimentPackage(pck);
                    experimentPackageLicense.setLicense(lic);
                    experimentPackageLicenseFacade.create(experimentPackageLicense);
                }


                licenses.clear();
                optionsModel.detach();
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

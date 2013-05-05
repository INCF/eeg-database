package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.logic.workflow.WorkflowService;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER",
		"ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowFormPage extends MenuPage {

	private static final long serialVersionUID = -1967810037377960414L;

	@SpringBean
	ExperimentsFacade facade;

	@SpringBean
	FileFacade fileFacade;

	@SpringBean
	WorkflowService workflowService;

	private List<String> fileNames = new ArrayList<String>();
	private final String[] fooList = new String[] { "KIV_FORMAT",
			"DOUBLE_FORMAT" };
	private final String[] storeList = new String[] { "true", "false" };
	private List<String> exps = new ArrayList<String>();
	private List<String> results = new ArrayList<String>();
	private List<String> methods = new ArrayList<String>();

	WebMarkupContainer outputContainer;
	String category;
	String data;
	String store;
	String method;
	String message;

	public WorkflowFormPage() {
		setPageTitle(ResourceUtils.getModel("pageTitle.workflowForm"));
		exps = workflowService.getExperiments();
		results = workflowService.getResultNames();
		methods = workflowService.getMethods();
		workflowService.beginExperiment("Experiment1");
		add(new ButtonPageMenu("leftMenu", WorkflowPageLeftMenu.values()));
		add(new MyForm("form"));
		outputContainer = new WebMarkupContainer("outputContainer");
		outputContainer.setOutputMarkupPlaceholderTag(true);
		outputContainer.setOutputMarkupId(true);
		outputContainer.setVisible(false);
		add(outputContainer);

		outputContainer.add(new Label("message", new PropertyModel<String>(
				this, "message")));
	}

	/**
	 * Method taking fileID for experiment files Checking only for eeg whdr and
	 * wmrk files
	 * 
	 * @param experimentID
	 * @return
	 */

	private int[] setFiles(int experimentID) {
		List<DataFile> data = facade.getDataFilesWhereExpId(experimentID);
		int[] pole = new int[data.size()];
		for (int a = 0; a < pole.length; a++) {
			if (data.get(a).getFilename().endsWith(".eeg")
					|| data.get(a).getFilename().endsWith(".vhdr")
					|| data.get(a).getFilename().endsWith(".vmrk")) {
				pole[a] = data.get(a).getDataFileId();
				fileNames.add(data.get(a).getFilename());
			}
		}
		return pole;
	}

	public class MyForm extends StatelessForm<Object> {

		/**
		 * Class working with all fields on page. Getting all values and add
		 * step to workflow or call service
		 */
		private static final long serialVersionUID = 7867439631046449269L;
		List<String> categoryList = Arrays.asList(fooList);
		List<String> storeValueList = Arrays.asList(storeList);
		List<String> dataList = new ArrayList<String>();
		List<String> methodList = new ArrayList<String>();

		public MyForm(String id) {
			super(id);
			setOutputMarkupId(true);

			final DropDownChoice<Object> inputType = new DropDownChoice<Object>(
					"inputType", new PropertyModel<Object>(
							WorkflowFormPage.this, "category"),
					Model.ofList(categoryList));
			final DropDownChoice<Object> inputData = new DropDownChoice<Object>(
					"inputData", new PropertyModel<Object>(
							WorkflowFormPage.this, "data"), new PropertyModel(
							this, "dataList"));
			final DropDownChoice<Object> methodName = new DropDownChoice<Object>(
					"methodName", new PropertyModel<Object>(
							WorkflowFormPage.this, "method"),
					new PropertyModel(this, "methodList"));
			final DropDownChoice<Object> storeResult = new DropDownChoice<Object>(
					"storeResult", new PropertyModel<Object>(
							WorkflowFormPage.this, "store"),
					Model.ofList(storeValueList));

			inputData.setOutputMarkupId(true);
			methodName.setOutputMarkupId(true);
			storeResult.setOutputMarkupId(true);
			

			inputType.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				/**
				 * Changing input data regarding to selected type
				 */
				private static final long serialVersionUID = -2505171599693180817L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (category.equals(categoryList.get(0))) {
						dataList = exps;
					} else if (category.equals(categoryList.get(1))) {
						dataList = results;
					}
					if (target != null) {
						target.add(inputData);
					}
				}
			});

			category = categoryList.get(0);
			dataList = exps;
			methodList = methods;
			store = storeValueList.get(0);

			add(inputType);
			add(inputData);
			add(methodName);
			add(storeResult);

			final TextField<String> stepName = new TextField<String>(
					"stepName", new Model<String>());
			stepName.setOutputMarkupId(true);
			final TextField<String> methodParams = new TextField<String>(
					"methodParams", new Model<String>());
			methodParams.setOutputMarkupId(true);

			stepName.add(new AjaxFormComponentUpdatingBehavior("onfocus") {

				/**
				 * Dismissing last message
				 */
				private static final long serialVersionUID = -2505171599693180817L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (target != null) {
						outputContainer.setVisible(false);
						if (target != null) {
							target.add(outputContainer);
						}
					}
				}
			});
			
			
			
			add(stepName);
			add(methodParams);

			AjaxButton addButton = new AjaxButton("addButton",
					ResourceUtils.getModel("button.addToWorkflow"), this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {

				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					String sName = stepName.getModelObject();
					String mParams = methodParams.getModelObject();
					if (data.contains(":")) {
						int expId = Integer.parseInt(data.split(":")[0]);
						int[] fileIds = setFiles(expId);
						workflowService.addFileIds(fileIds);
					} else {
						fileNames.add(data);
					}
					workflowService.addToWorkflow(sName, category, store,
							method, mParams, fileNames);
					results = workflowService.getResultNames();
					fileNames.clear();
					stepName.setModelValue(null);
					methodParams.setModelValue(null);
					inputData.setModelValue(null);
					methodName.setModelValue(null);
					store = storeValueList.get(0);
					category = categoryList.get(0);
					target.add(stepName);
					target.add(methodParams);
					target.add(inputData);
					target.add(methodName);
					target.add(storeResult);
					target.add(inputType);
					message = ResourceUtils.getString("message.workflow.added");
					outputContainer.setVisible(true);
					if (target != null) {
						target.add(outputContainer);
					}
					// TODO dodìlat kontrolu zda jsou všechny fieldy korektnì
					// vyplnìné
				}
			};
			add(addButton);
			AjaxButton submitButton = new AjaxButton("submitButton",
					ResourceUtils.getModel("button.submitWorkflow"), this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {

				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					workflowService.endExperiment();
					workflowService.runService();
					message = ResourceUtils.getString("message.workflow.submited");
					outputContainer.setVisible(true);
					if (target != null) {
						target.add(outputContainer);
					}
					stepName.setModelValue(null);
					methodParams.setModelValue(null);
					inputData.setModelValue(null);
					methodName.setModelValue(null);
					store = storeValueList.get(0);
					category = categoryList.get(0);
					target.add(stepName);
					target.add(methodParams);
					target.add(inputData);
					target.add(methodName);
					target.add(storeResult);
					target.add(inputType);
				}
			};
			add(submitButton);
		}

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

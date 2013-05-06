package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
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

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER",
		"ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowFormPage extends MenuPage {

	private static final long serialVersionUID = -1967810037377960414L;

	@SpringBean
	ExperimentsFacade facade;

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
		results = workflowService.getStepNames();
		methods = workflowService.getMethods();
		workflowService.beginExperiment("Experiment1"); // Natvrdo pridany
														// nazev. Neni
														// implementovana volba
		add(new ButtonPageMenu("leftMenu", WorkflowPageLeftMenu.values()));
		Form<?> form = new MyForm("form");
		form.add(new AjaxEventBehavior("onclick") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				outputContainer.setVisible(false);
				if (target != null) {
					target.add(outputContainer);
				}
			}
		});
		form.setOutputMarkupId(true);
		add(form);
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
	 * wmrk files and returning array of these ids.
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
		 * Class working with all control fields on the page. Getting all
		 * values, add step to workflow or call workflow service.
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
				 * Changing input data type regarding to selected input type
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

			add(stepName);
			add(methodParams);

			AjaxButton addButton = new AjaxButton("addButton",
					ResourceUtils.getModel("button.addToWorkflow"), this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
				}

				/**
				 * Method invoking when add button is pressed. All values are
				 * taken and sent to WorkflowService method to add generated
				 * step to workflow. All fields are required. After creating
				 * step all fields are initiated.
				 * 
				 */

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					String sName = stepName.getModelObject();
					String mParams = methodParams.getModelObject();

					if (data != null && method != null && sName != null
							&& mParams != null) {

						if (data.contains(":")) {
							int expId = Integer.parseInt(data.split(":")[0]);
							int[] fileIds = setFiles(expId);
							workflowService.addFileIds(fileIds);
						} else {
							fileNames.add(data);
						}
						workflowService.addToWorkflow(sName, category, store,
								method, mParams, fileNames);
						results = workflowService.getStepNames();
						fileNames.clear();
						stepName.setModelValue(null);
						methodParams.setModelValue(null);
						inputData.setModelValue(null);
						methodName.setModelValue(null);
						store = storeValueList.get(0);
						category = categoryList.get(0);
						dataList = exps;
						target.add(stepName);
						target.add(methodParams);
						target.add(inputData);
						target.add(methodName);
						target.add(storeResult);
						target.add(inputType);
						message = ResourceUtils
								.getString("message.workflow.added");
						outputContainer.setVisible(true);
						if (target != null) {
							target.add(outputContainer);
						}
					} else {
						message = ResourceUtils
								.getString("message.workflow.errorFields");
						outputContainer.setVisible(true);
						if (target != null) {
							target.add(outputContainer);
						}
					}
					// TODO dodelat kontrolu zda jsou vsechny fieldy korektne›
					// vyplneny. Pripadne zda dany experiment obsahuje vsechny
					// pottrebne data.
				}
			};
			add(addButton);
			AjaxButton submitButton = new AjaxButton("submitButton",
					ResourceUtils.getModel("button.submitWorkflow"), this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {

				}

				/**
				 * Method is invoking WorkflowService method to invoke data
				 * processing. First all values are added to new step and then
				 * workflow service is started. After submiting all fields are
				 * and step lists are cleared.
				 */

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					String sName = stepName.getModelObject();
					String mParams = methodParams.getModelObject();

					if (data != null && method != null && sName != null
							&& mParams != null) {

						if (data.contains(":")) {
							int expId = Integer.parseInt(data.split(":")[0]);
							int[] fileIds = setFiles(expId);
							workflowService.addFileIds(fileIds);
						} else {
							fileNames.add(data);
						}
						workflowService.addToWorkflow(sName, category, store,
								method, mParams, fileNames);
						results = workflowService.getStepNames();
						fileNames.clear();

						workflowService.endExperiment();
						workflowService.runService();
						message = ResourceUtils
								.getString("message.workflow.submited");
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
						dataList = exps;
						target.add(stepName);
						target.add(methodParams);
						target.add(inputData);
						target.add(methodName);
						target.add(storeResult);
						target.add(inputType);
						workflowService.clearWorkflow();
					} else {
						message = ResourceUtils
								.getString("message.workflow.errorFields");
						outputContainer.setVisible(true);
						if (target != null) {
							target.add(outputContainer);
						}
					}
				}
				// TODO pripadne dodelat presmrovni na jinou stranku. Napr.
				// stranku se seznamem vysledku
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

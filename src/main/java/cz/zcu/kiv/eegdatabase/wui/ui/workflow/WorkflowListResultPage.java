package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import java.util.ArrayList;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER",
		"ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowListResultPage extends MenuPage {

	private static final long serialVersionUID = -1610693580181696939L;

	@SpringBean
	ServiceResultDao serviceResult;
	
	@SpringBean
	PersonDao personDao;
	
	List<ServiceResult> results = new ArrayList<ServiceResult>();
	List<String> filenames = new ArrayList<String>();
	
	String chosenFile;

	public WorkflowListResultPage() {
		setPageTitle(ResourceUtils.getModel("pageTitle.listOfResults"));
		results = serviceResult.getResultByPerson(personDao.getLoggedPerson().getPersonId());
		for(ServiceResult res : results){
			filenames.add(res.getFilename());
		}
		add(new ButtonPageMenu("leftMenu", WorkflowPageLeftMenu.values()));
		add(new MyForm("form"));
	}


	
	public class MyForm extends StatelessForm<Object>{

		/**
		 * Pouze prototyp, kterej zobrazuje soubory ServiceResultu uživatele.
		 * Potøeba dodìlat strankù pro manipulaci s výsledky.
		 * 
		 */
		private static final long serialVersionUID = 6331997702954052360L;

		public MyForm(String id) {
			super(id);
			setOutputMarkupId(true);
			DropDownChoice<Object> resultChoser = new DropDownChoice<Object>(
					"resultChoser", new PropertyModel<Object>(
							WorkflowListResultPage.this, "chosenFile"),
					Model.ofList(filenames));
			add(resultChoser);
		}
	}
}

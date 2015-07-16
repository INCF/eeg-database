package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxConfirmLink;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;

@AuthorizeInstantiation("ROLE_ADMIN")
public class ODMLMigrationPage extends MenuPage {
    
    private static final long serialVersionUID = 7181261981873858763L;
    
    @SpringBean
    private TemplateFacade templateFacade;
    
    public ODMLMigrationPage() {
        
        setPageTitle(ResourceUtils.getModel("pageTitle.migration"));

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        
        AjaxConfirmLink<Void> migrationLink = new AjaxConfirmLink<Void>("migrationLink", "Are you sure you want migrate data into ElasticSearch db.") {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                
                target.add(getFeedback());

                if(templateFacade.migrateSQLToES()) {
                    info("migration is done.");
                } else {
                    error("migration failed.");
                }
            }
        };
        
        add(migrationLink);
        
    }
}

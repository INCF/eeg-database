package cz.zcu.kiv.eegdatabase.wui.components.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class UnderConstructPage extends MenuPage {
    
    private static final long serialVersionUID = 1L;

    public UnderConstructPage() {
        
    }
    
    public UnderConstructPage(PageParameters params) {
        
    }

}

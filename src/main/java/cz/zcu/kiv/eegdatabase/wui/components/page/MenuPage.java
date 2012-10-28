package cz.zcu.kiv.eegdatabase.wui.components.page;

import org.apache.wicket.markup.html.link.ExternalLink;

import cz.zcu.kiv.eegdatabase.wui.components.feedback.BaseFeedbackMessagePanel;
import cz.zcu.kiv.eegdatabase.wui.components.menu.ddm.MainMenu;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class MenuPage extends BasePage {

    private static final long serialVersionUID = -9208758802775141621L;

    private BaseFeedbackMessagePanel feedback;

    public MenuPage() {

        feedback = new BaseFeedbackMessagePanel("base_feedback");
        add(feedback);
        
        add(new MainMenu("mainMenu"));
        
        add(new ExternalLink("footerLink", ResourceUtils.getString("general.footer.link"), ResourceUtils.getString("general.footer.link.title")));
    }
    
    public BaseFeedbackMessagePanel getFeedback() {
        return feedback;
    }
}

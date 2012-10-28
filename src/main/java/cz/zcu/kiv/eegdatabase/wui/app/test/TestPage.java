package cz.zcu.kiv.eegdatabase.wui.app.test;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;

public class TestPage extends MenuPage {
    
    
    public TestPage() {
        
        // image
        add(ResourceUtils.getImage("logo","logo.png"));
        
        // title on page
        add(new Label("title", ResourceUtils.getModel("general.test")));
        // paragraphs
        add(new Label("p1", "This is the first paragraph like a span"));
        add(new Label("p2", "This is the first paragraph like a paragraph from html"));
        
        // html link with static text
        add(new Link<Void>("homepageLinkStatic") {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });
        
        // html link with text from properties
        add(new Link<Void>("homepageLink") {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });
    }
}

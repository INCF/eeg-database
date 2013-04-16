package cz.zcu.kiv.eegdatabase.wui.components.repeater;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

/**
 * Ajax-powered navigator component with scroll-to-the-top animation on click.
 *
 * @author Jakub Danek
 */
public class CustomAjaxPagingNavigator extends AjaxPagingNavigator {

    /**
     *
     * @param id component id
     * @param pageable a list or a table etc.
     */
    public CustomAjaxPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    /**
     *
     * @param id component id
     * @param pageable a list or a table etc
     * @param labelProvider label provider for the link text
     */
    public CustomAjaxPagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        super(id, pageable, labelProvider);
    }

    @Override
    protected void onAjaxEvent(AjaxRequestTarget target) {
        super.onAjaxEvent(target);
        //scroll to the top of the page
        String scr = "$('html, body').animate({ scrollTop: 10 }, 'fast', 'linear', function(){});";
        target.appendJavaScript(scr);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        this.setVisible(this.getPageable().getPageCount() > 1);
    }

}
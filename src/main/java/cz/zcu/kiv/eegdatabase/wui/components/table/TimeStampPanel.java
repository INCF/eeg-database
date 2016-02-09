package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import java.text.SimpleDateFormat;

/**
 * Created by Lichous on 28.4.15.
 */
public class TimeStampPanel extends Panel {

    private static final long serialVersionUID = 7354220507444746354L;

    public TimeStampPanel(String id, String propertyExpression, final IModel model, String timestampFormat) {
        super(id);
        final PropertyModel paramModel = new PropertyModel(model, propertyExpression);

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat);
            add(new Label("time",dateFormat.format(paramModel.getObject())));
        } catch(IllegalArgumentException e)  {
            e.printStackTrace();
        }

    }
}

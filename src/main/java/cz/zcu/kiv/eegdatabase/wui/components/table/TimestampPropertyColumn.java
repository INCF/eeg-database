package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class TimestampPropertyColumn<T, S> extends PropertyColumn<T, S> {

    private static final long serialVersionUID = -6212200024939578411L;
    private String formatPattern;

    public TimestampPropertyColumn(final IModel<String> displayModel, final S sortProperty,
            final String propertyExpression, String formatPattern) {
        super(displayModel, sortProperty, propertyExpression);
        this.formatPattern = formatPattern;
    }

    @Override
    public IModel getDataModel(IModel rowModel) {
        final IModel superModel = super.getDataModel(rowModel);
        if (superModel != null && superModel.getObject() != null && Timestamp.class.isAssignableFrom(superModel.getObject().getClass())) {
            final Timestamp object = (Timestamp) superModel.getObject();
            return new AbstractReadOnlyModel() {

                public Object getObject() {
                    return new SimpleDateFormat(formatPattern, EEGDataBaseSession.get().getLocale()).format((Date)object);
                }
            };
        } else {
            return superModel;
        }
    }

}

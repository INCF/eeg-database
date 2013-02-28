package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;

import com.ibm.icu.text.SimpleDateFormat;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class TimestampLabel extends Label {

    private static final long serialVersionUID = -6164869657967617094L;

    public TimestampLabel(String id, Serializable object, String formatPatter) {
        super(id,
                object == null ? "" : new SimpleDateFormat(formatPatter, EEGDataBaseSession.get().getLocale()).format((Date) object));
    }
}

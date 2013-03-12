package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.convert.IConverter;

import com.ibm.icu.text.SimpleDateFormat;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class TimestampConverter implements IConverter<Timestamp> {

    private static final long serialVersionUID = -7117613197673869117L;

    protected Log log = LogFactory.getLog(getClass());

    private String pattern = "MM/dd/yyyy";
    private String parsePattern = "MM/dd/yyyy";

    public TimestampConverter() {

    }

    public TimestampConverter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Timestamp convertToObject(String value, Locale locale) {
        try {
            return new Timestamp(new SimpleDateFormat(parsePattern).parse(value).getTime());
        } catch (ParseException e) {

            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String convertToString(Timestamp value, Locale locale) {
        return new SimpleDateFormat(pattern, EEGDataBaseSession.get().getLocale()).format(value);
    }

}

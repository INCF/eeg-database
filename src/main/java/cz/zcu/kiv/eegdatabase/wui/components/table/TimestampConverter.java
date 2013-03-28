package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.convert.IConverter;

import com.ibm.icu.text.SimpleDateFormat;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

/**
 * Custom conterter for working with timestamp in textfield. Contains output and input format.
 * 
 * @author Jakub Rinkes
 * 
 */
public class TimestampConverter implements IConverter<Timestamp> {

    private static final long serialVersionUID = -7117613197673869117L;

    protected Log log = LogFactory.getLog(getClass());

    private String formatPattern = "MM/dd/yyyy";
    private String parsePattern = "MM/dd/yyyy";

    public TimestampConverter() {

    }

    public TimestampConverter(String formatPattern) {

        this.formatPattern = formatPattern;
    }

    public TimestampConverter(String formatPattern, String parsePattern) {

        this.formatPattern = formatPattern;
        this.parsePattern = parsePattern;
    }

    @Override
    public Timestamp convertToObject(String value, Locale locale) {
        try {
            return new Timestamp(new SimpleDateFormat(getParsePattern()).parse(value).getTime());
        } catch (ParseException e) {

            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String convertToString(Timestamp value, Locale locale) {
        return new SimpleDateFormat(getFormatPattern(), EEGDataBaseSession.get().getLocale()).format(value);
    }
    
    public String getParsePattern() {
        return parsePattern;
    }
    
    public String getFormatPattern() {
        return formatPattern;
    }

}

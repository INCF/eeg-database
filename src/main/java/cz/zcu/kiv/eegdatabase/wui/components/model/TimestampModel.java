package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;

public class TimestampModel implements IModel<String>
{
    private final IModel<Timestamp> inner;
    private static final long serialVersionUID = 190887916985140272L;
    private String format;

    public TimestampModel(IModel<Timestamp> inner, String format)
    {
        this.inner = inner;
        this.format = format;
    }

    public void detach()
    {
        inner.detach();
    }

    @Override
    public String getObject()
    {
        Timestamp date = (Timestamp) inner.getObject();
        if (date == null)
        {
            return "";
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(date);
    }
    
    @Override
    public void setObject(String s)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        try
        {
            Date date = dateFormatter.parse(s);
            inner.setObject(new Timestamp(date.getTime()));
        } catch (ParseException e)
        {
            throw new WicketRuntimeException("Unable to parse date.", e);
        }
    }
    
    
}

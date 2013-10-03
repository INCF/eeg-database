/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   TimestampModel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;

/**
 * Implementation of model with Timestamp object inside. In constructor is format getting formated output.
 * 
 * @author Jakub Rinkes
 * 
 */
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

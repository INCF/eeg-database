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
 *   TimestampConverter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.convert.ConversionException;
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
            
            long time = new SimpleDateFormat(getParsePattern()).parse(value).getTime();
            return new Timestamp(time);
        
        } catch (ParseException e) {
            log.info(e.getMessage(), e);
            throw new ConversionException("Wrong Date Format: " + parsePattern);
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

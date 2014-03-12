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
 *   DateTimeFieldPicker.java, 2014/03/15 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form.input;

import java.util.Date;

import org.apache.wicket.model.IModel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

public class DateTimeFieldPicker extends DatePicker {

    private static final long serialVersionUID = 1L;

    public DateTimeFieldPicker(String id) {
        super(id, prepareDatePickerOptions());
    }

    public DateTimeFieldPicker(String id, IModel<Date> model) {
        super(id, model, prepareDatePickerOptions());
    }

    
    public DateTimeFieldPicker(String id, IModel<Date> model, String pattern, Options options) {
        super(id, model, pattern, options);
    }
    
    public DateTimeFieldPicker(String id, Options options) {
        super(id, options);
    }
    
    private static Options prepareDatePickerOptions(){
        
        Options options = new Options();
        options.set("showOtherMonths", true);
        options.set("selectOtherMonths", true);
        options.set("changeMonth", true);
        options.set("changeYear", true);
        
        return options;
    }

}

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
 *   StyleClassPropertyColumn.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

/**
 * Enhancement table column for CSS class style. 
 * 
 * @author Jakub Rinkes
 *
 * @param <T> type of object
 * @param <S> sort parameter
 */
public class StyleClassPropertyColumn<T, S> extends PropertyColumn<T, S> {

    private static final long serialVersionUID = 7884643917380980331L;

    private String styleClass;

    public StyleClassPropertyColumn(IModel<String> displayModel, String propertyExpression, String CSSClass) {
        super(displayModel, propertyExpression);
        this.styleClass = CSSClass;
    }

    public StyleClassPropertyColumn(IModel<String> displayModel, S sortProperty, String propertyExpression, String CSSClass) {
        super(displayModel, sortProperty, propertyExpression);
        this.styleClass = CSSClass;
    }

    @Override
    public String getCssClass() {
        return styleClass;
    }
}

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
 *   TimestampPropertyColumn.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.table;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

/**
 * Enhancement table column, added timestamp output formater.
 * 
 * @author Jakub Rinkes
 * 
 * @param <T>
 * @param <S>
 */
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
                    return new SimpleDateFormat(getFormatPattern(), EEGDataBaseSession.get().getLocale()).format((Date) object);
                }
            };
        } else {
            return superModel;
        }
    }

    public String getFormatPattern() {
        return formatPattern;
    }

}

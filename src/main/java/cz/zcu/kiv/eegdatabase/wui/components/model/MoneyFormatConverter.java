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
 *   MoneyFormatConverter.java, 2014/11/27 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractNumberConverter;

/**
 * Implementation of wicket model with money format and currency.
 * 
 * @author Jakub Rinkes
 * 
 * @param <T>
 *            type of model object.
 */
public class MoneyFormatConverter extends AbstractNumberConverter {

    private static final long serialVersionUID = 1L;
    private Currency currency;
    private int presicion;
    private NumberFormat formatter;

    public MoneyFormatConverter(Currency currency, int presicion) {

        this.presicion = presicion;
        this.currency = currency;

        formatter = NumberFormat.getCurrencyInstance();

        formatter.setCurrency(currency);
        formatter.setMaximumFractionDigits(presicion);
        formatter.setMinimumFractionDigits(presicion);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public Object convertToObject(String value, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("method convertToObject(..) not implemented. Converter is used only for labels");
    }

    @Override
    public NumberFormat getNumberFormat(Locale locale) {
        return formatter;
    }

    @Override
    protected Class getTargetType() {
        return BigDecimal.class;
    }

}

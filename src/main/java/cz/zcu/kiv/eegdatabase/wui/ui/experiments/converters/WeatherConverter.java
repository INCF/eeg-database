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
 *   WeatherConverter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 14.5.13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class WeatherConverter implements IConverter<Weather> {
    private WeatherFacade weatherFacade;

    public WeatherConverter(WeatherFacade weatherFacade) {
        this.weatherFacade = weatherFacade;
    }

    @Override
    public Weather convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        List<Weather> weathers = weatherFacade.readByParameter("title", s);
        return (weathers != null && weathers.size() > 0) ? weathers.get(0) : new Weather();

    }

    @Override
    public String convertToString(Weather weather, Locale locale) {
        return weather.getTitle();
    }
}

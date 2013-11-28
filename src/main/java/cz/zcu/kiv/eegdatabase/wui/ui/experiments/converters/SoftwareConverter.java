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
 *   SoftwareConverter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

public class SoftwareConverter implements IConverter<Software> {

    private static final long serialVersionUID = -4540322401110648665L;

    private SoftwareFacade softwareFacade;

    public SoftwareConverter(SoftwareFacade softwareFacade) {
        this.softwareFacade = softwareFacade;
    }

    @Override
    public Software convertToObject(String s, Locale locale) {
        if (Strings.isEmpty(s)) {
            return null;
        }

        List<Software> softwares = softwareFacade.readByParameter("title", s);
        return (softwares != null && softwares.size() > 0) ? softwares.get(0) : new Software();

    }

    @Override
    public String convertToString(Software software, Locale locale) {
        return software.getTitle();
    }
}

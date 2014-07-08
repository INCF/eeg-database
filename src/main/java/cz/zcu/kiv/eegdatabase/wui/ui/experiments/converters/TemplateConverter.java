package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.core.common.TemplateFacade;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * TemplateConverter, 2014/07/08 15:53 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class TemplateConverter implements IConverter<Template> {

    private TemplateFacade templateFacade;

    public TemplateConverter(TemplateFacade templateFacade) {
        this.templateFacade = templateFacade;
    }

    @Override
    public Template convertToObject(String s, Locale locale) throws ConversionException {
        if (Strings.isEmpty(s)) {
            return null;
        }
        int id = Integer.parseInt(s);
        List<Template> templates = templateFacade.readByParameter("templateId", s);
        return (templates != null && templates.size() > 0) ? templates.get(0) : new Template();
    }

    @Override
    public String convertToString(Template template, Locale locale) {
        return ""+template.getTemplateId();
    }
}

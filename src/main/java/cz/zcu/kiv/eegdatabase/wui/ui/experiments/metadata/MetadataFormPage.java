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
 *   MetadataFormPage.java, 2015/03/03 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata;

import java.io.InputStream;

import odml.core.Reader;
import odml.core.Section;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.sun.xml.messaging.saaj.util.ByteInputStream;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template.ListTemplatePage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class MetadataFormPage extends MenuPage {

    private static final long serialVersionUID = -4170790197539589617L;
    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private TemplateFacade templateFacade;

    public MetadataFormPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.metadata.new"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        InputStream template = EEGDataBaseApplication.get().getServletContext().getResourceAsStream("/files/odML/testtemplate.xml");

        Section section = null;
        Reader reader = new Reader();
        try {
            section = reader.load(template);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        add(new MetadataForm("metadata-form", new Model<Section>(section)));
    }

    public MetadataFormPage(final PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.metadata.new"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        StringValue value = parameters.get(DEFAULT_PARAM_ID);
        if (value.isEmpty() || value.isNull()) {
            throw new RestartResponseAtInterceptPageException(ListTemplatePage.class);
        }

        int templateId = value.toInt();
        Template template = templateFacade.read(templateId);

        Reader reader = new Reader();
        try {
            Section section = reader.load(new ByteInputStream(template.getTemplate(), template.getTemplate().length));
            add(new MetadataForm("metadata-form", new Model<Section>(section)));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestartResponseAtInterceptPageException(ListTemplatePage.class);
        }

    }
}

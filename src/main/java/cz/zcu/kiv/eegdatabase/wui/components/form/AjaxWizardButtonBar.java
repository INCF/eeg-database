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
 *   AjaxWizardButtonBar.java, 2013/11/29 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardButtonBar;
import org.apache.wicket.markup.html.form.Form;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class AjaxWizardButtonBar extends WizardButtonBar {

    private static final long serialVersionUID = 1L;

    public AjaxWizardButtonBar(String id, final Wizard wizard) {
        super(id, wizard);

        super.addOrReplace(new AjaxWizardButton("cancel", wizard, "cancel", ResourceUtils.getString("general.wizard.cancel.question")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onClick(AjaxRequestTarget target) {
                wizard.onCancel();
            }

            @Override
            public final boolean isEnabled() {
                return getWizardModel().isCancelVisible();
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                wizard.onCancel();
            }

        });

    }

}

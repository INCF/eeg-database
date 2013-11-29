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
 *   AjaxWizardButton.java, 2013/11/29 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public abstract class AjaxWizardButton extends AjaxButton {

    private static final long serialVersionUID = 1L;
    private final IWizard wizard;
    private String message;

    public AjaxWizardButton(String id, IWizard wizard, String label, String message) {
        super(id, new Model<String>(label));
        this.wizard = wizard;
        this.message = message;
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
    {
        super.updateAjaxAttributes(attributes);

        AjaxCallListener ajaxCallListener = new AjaxCallListener();
        ajaxCallListener.onPrecondition("return confirm('" + message + "');");
        attributes.getAjaxCallListeners().add(ajaxCallListener);
    }

    protected final IWizard getWizard() {
        return wizard;
    }

    protected final IWizardModel getWizardModel() {
        return getWizard().getWizardModel();
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        onClick(target);
    }

    protected abstract void onClick(AjaxRequestTarget target);
}

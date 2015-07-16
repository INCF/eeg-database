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
 *   TemplateTreeViewModel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.Serializable;

/**
 * View model for template tree component.
 * 
 * Default state:
 * - definition visible
 * 
 * @author Jakub Rinkes
 *
 */
public class TemplateTreeViewModel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private boolean definitionVisible = true;

    public TemplateTreeViewModel() {
        
    }

    public boolean isDefinitionVisible() {
        return definitionVisible;
    }

    public void setDefinitionVisible(boolean definitionVisible) {
        this.definitionVisible = definitionVisible;
    }

}

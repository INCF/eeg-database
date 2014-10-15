/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * FormLayoutType.java, 15. 10. 2014 16:45:19, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * Enumeration of supported types of form templates.
 * 
 * @author Jakub Krauz
 *
 */
public enum FormLayoutType {
    
    /** Proprietary EEGBase's templates format using odML. */
    ODML_EEGBASE,
    
    /** OdML templates using the odML-GUI namespace. */
    ODML_GUI,
    
    /** Unknown templates format. */
    OTHER

}

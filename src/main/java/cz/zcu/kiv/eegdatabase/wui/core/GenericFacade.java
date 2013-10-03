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
 *   GenericFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core;

import java.io.Serializable;
import java.util.List;

public interface GenericFacade<T, PK extends Serializable> {

    PK create(T newInstance);

    T read(PK id);

    List<T> readByParameter(String parameterName, Object parameterValue);

    void update(T transientObject);

    void delete(T persistentObject);

    List<T> getAllRecords();

    List<T> getRecordsAtSides(int first, int max);

    int getCountRecords();

    List<T> getUnique(T example);
}

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
 *   FacetCategory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.search;

import java.io.Serializable;

/**
 * Represents a category of faceted search. Each category maintains its name and a number of documents in it.
 * User: Jan Koren
 * Date: 8.4.13
 * Time: 16:54
 */
public class FacetCategory implements Serializable, Comparable<FacetCategory> {

    private String name;
    private long count;

    private static final long serialVersionUID = 5458851218415844461L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public FacetCategory(String name, long count) {
        this.name = name;
        this.count = count;
    }

    /**
     * @see Comparable#compareTo(Object)
     * @param anotherCategory another facet category for comparison.
     * @return
     */
    @Override
    public int compareTo(FacetCategory anotherCategory) {

        int diff = - (int)(this.getCount() -  anotherCategory.getCount());
        if (diff == 0) {
            return this.getName().compareTo(anotherCategory.getName());
        }

        return diff;
    }
}

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
 *   ResultCategory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.search;

/**
 * Enumeration type for the categories of full text search results.
 * User: Jan Koren
 * Date: 8.4.13
 */
public enum ResultCategory {

    ARTICLE("Article"),
    EXPERIMENT("Experiment"),
    PERSON("Person"),
    RESEARCH_GROUP("Research group"),
    SCENARIO("Scenario"),
    ALL("All");

    private String value;

    private ResultCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ResultCategory getCategory(String value) {
        for(ResultCategory category : values()) {
            if(category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }
}

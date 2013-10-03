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
 *   FullTextSearchUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.search;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;
import org.apache.wicket.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class containing a set of useful methods and constants related to full-text search.
 * Date: 25.3.13
 * Time: 23:00
 */
public class FullTextSearchUtils {

    public final static int AUTOCOMPLETE_ROWS = 10;

    public static final String HIGHLIGHTED_TEXT_BEGIN = "<span class=\"hilite\">";
    public static final String HIGHLIGHTED_TEXT_END = "</span>";
    public static final String HIGHLIGHT_MERGE_SEQUENCE = HIGHLIGHTED_TEXT_END + " " + HIGHLIGHTED_TEXT_BEGIN;

    /**
     * Maps result categories to Page classes
     */
    private static Map<String, Class<? extends Page>> categoryToPageMap = initializeClassMap();
    /**
     * Maps Page classes to names of their documents in the index.
     */
    private static Map<Class<?>, String> pojoToCategoryMap = initializeResultTypes();

    /**
     * Maps result categories to their application properties

    private static Map<String, String> categoryToPropertyMap = initializeResultProperties();
     */

    /**
     * Sets the POJO - Page class mapping.
     * @return POJO classes to Page classes mapping.
     */
    private static final Map<String, Class<? extends Page>> initializeClassMap() {

        Map<String, Class<? extends Page>> map = new HashMap<String, Class<? extends Page>>();

        map.put(ResultCategory.ARTICLE.getValue(), ArticlesPage.class);
        map.put(ResultCategory.EXPERIMENT.getValue(), ExperimentsDetailPage.class);
        map.put(ResultCategory.PERSON.getValue(), PersonDetailPage.class);
        map.put(ResultCategory.RESEARCH_GROUP.getValue(), ResearchGroupsDetailPage.class);
        map.put(ResultCategory.SCENARIO.getValue(), ScenarioDetailPage.class);

        return map;
    }

    /**
     * Sets page - result type mapping.
     * @return Page - result type mapping.
     */
    private static final  Map<Class<?>, String> initializeResultTypes() {

        Map<Class<?>, String> map = new HashMap<Class<?>, String>();

        map.put(Article.class, ResultCategory.ARTICLE.getValue());
        map.put(Experiment.class, ResultCategory.EXPERIMENT.getValue());
        map.put(Person.class, ResultCategory.PERSON.getValue());
        map.put(ResearchGroup.class, ResultCategory.RESEARCH_GROUP.getValue());
        map.put(Scenario.class, ResultCategory.SCENARIO.getValue());

        return map;
    }

    /**
     * Gets a page class for a given POJO class.
     * @param type The POJO class.
     * @return The Wicket Page class.
     */
    public static Class<? extends Page> getTargetPage(String type) {

        for(String typeValue : categoryToPageMap.keySet()) {
            if(typeValue.equals(type)) {
                return categoryToPageMap.get(typeValue);
            }
        }

        return UnderConstructPage.class;
    }

    /**
     * Gets a type of the document from a given Wicket Page class.
     * @param clazz The Wicket Page class.
     * @return The document type.
     */
    public static String getDocumentType(Class<? extends Page> clazz) {

        if(pojoToCategoryMap.containsKey(clazz)) {
            return pojoToCategoryMap.get(clazz);
        }
        else {
            return "Other";
        }
    }

    /**
     * Gets a type of the document from a given fulltext result instance.
     * @param result The fulltext result.
     * @return The document type.
     */
    public static String getDocumentType(FullTextResult result) {
        if(result.getTargetPage() == null) {
            return "Other";
        }

        return getDocumentType(result.getTargetPage());
    }
}

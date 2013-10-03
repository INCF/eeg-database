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
 *   IndexField.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.indexing;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public enum IndexField {

    CLASS("class"), // document category in this context
    FILE_MIMETYPE("file_mimetype"), // Data file MIME type
    ID("id"), // document id for its identification within a single category
    NAME("name"), // e.g. data file or scenario name
    PARAM_DATATYPE("param_datatype"), // hardware/person parameter types
    SOURCE("source"), // document origin - Database/LinkedIn
    TEMPERATURE("temperature"), // experiment temperature
    TEXT("text"), // for long text, notes, description etc.
    CHILD_TEXT("child_text"),  // text of child objects
    TIMESTAMP("timestamp"), // document timestamp
    TITLE("title"), // title/full name
    CHILD_TITLE("child_title"), // title of a child object
    UUID("uuid"), // document unique id
    AUTOCOMPLETE("autocomplete"); // for phrases for autocompletition

    private String value;

    private IndexField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

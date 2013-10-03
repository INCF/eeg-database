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
 *   ScenarioTypeSchema2.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 31.5.11
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_SCHEMA2")
@PrimaryKeyJoinColumn(name = "SCENARIO_ID", referencedColumnName = "SCENARIO_ID")
public class ScenarioTypeSchema2  extends ScenarioType<Document> {
    @Column(name = "SCENARIO_XML")
    @Type(type = "cz.zcu.kiv.eegdatabase.data.datatypes.OracleXMLType")
    private Document scenarioXml;

    public ScenarioTypeSchema2() {
    }

    public Document getScenarioXml() {
      return scenarioXml;
    }

    public void setScenarioXml(Document scenarioXml) {
      this.scenarioXml = scenarioXml;
    }
}

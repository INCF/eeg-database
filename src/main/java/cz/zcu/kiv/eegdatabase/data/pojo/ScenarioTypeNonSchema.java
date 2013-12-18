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
 *   ScenarioTypeNonSchema.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import java.sql.Blob;
import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Jan Koren
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_NONSCHEMA")
@PrimaryKeyJoinColumn(name = "SCENARIO_ID", referencedColumnName = "SCENARIO_ID")
public class ScenarioTypeNonSchema extends ScenarioType<Blob> {

  public ScenarioTypeNonSchema() {
  }

  public Blob getScenarioXml() {
    return null;
  }

  public void setScenarioXml(Blob scenarioXml) {
  }
}

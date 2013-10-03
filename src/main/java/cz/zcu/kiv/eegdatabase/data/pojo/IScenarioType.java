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
 *   IScenarioType.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 6.6.11
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public interface IScenarioType<T> {
    public int getScenarioId();
    public void setScenarioId(int scenarioId);
    public Scenario getScenario();
    public void setScenario(Scenario scenario);
    public T getScenarioXml();
    public void setScenarioXml(T scenarioXml);
}

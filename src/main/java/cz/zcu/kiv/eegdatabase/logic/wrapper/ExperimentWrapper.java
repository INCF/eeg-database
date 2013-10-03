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
 *   ExperimentWrapper.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

/**
 *
 * @author Honza
 */
public class ExperimentWrapper implements Wrapper {

  private Experiment e;

  public ExperimentWrapper(Object e) {
    this.e = (Experiment) e;
  }

  @Override
  public String getTitle() {
    return e.getScenario().getTitle();
  }

  public String className() {
    return "Experiment";
  }


  @Override
  public String getPath() {
    return "/experiments/detail.html?experimentId=";
  }
}

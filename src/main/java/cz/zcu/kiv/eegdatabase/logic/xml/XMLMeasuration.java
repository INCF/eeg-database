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
 *   XMLMeasuration.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.FileMetadataType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.HardwareType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.MeasurationAddParam;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.MeasurationType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.ObjectFactory;

/**
 *
 * @author Jan Štěbeták
 */
class XMLMeasuration {

  private MeasurationType measType;

  public XMLMeasuration(MeasurationType measType) {
    this.measType = measType;

  }

  public void writeStartAndEndTime(String start, String end) {
    measType.setStartTime(start);
    measType.setEndTime(end);
  }

  public HardwareType writeHardware(Hardware hw, ObjectFactory of) {
    HardwareType hwt = of.createHardwareType();
    hwt.setTitle(hw.getTitle());
    hwt.setType(hw.getType());
    return hwt;
  }

  public MeasurationAddParam writeAdditionalParams(ExperimentOptParamVal measAddParam,
          ObjectFactory of) {
    MeasurationAddParam maddp = of.createMeasurationAddParam();
    maddp.setName(measAddParam.getExperimentOptParamDef().getParamName());
    maddp.setDataType(measAddParam.getExperimentOptParamDef().
            getParamDataType());
    maddp.setValue(measAddParam.getParamValue());
    return maddp;

  }

  public FileMetadataType writeFileMetadata(FileMetadataParamVal fileMetadata,
          ObjectFactory of) {
    FileMetadataType fmt = of.createFileMetadataType();
    fmt.setName(fileMetadata.getFileMetadataParamDef().getParamName());
    fmt.setDataType(fileMetadata.getFileMetadataParamDef().getParamDataType());
    fmt.setValue(fileMetadata.getMetadataValue());
    return fmt;
  }
}

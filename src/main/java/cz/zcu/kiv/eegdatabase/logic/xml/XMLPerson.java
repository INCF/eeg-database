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
 *   XMLPerson.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.ObjectFactory;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.PersonAddParam;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.PersonType;

/**
 *
 * @author Jan Štěbeták
 */
class XMLPerson {
private PersonType perType;

    public XMLPerson(PersonType perType) {
        this.perType = perType;
    }

    public void writeName(String name, String surname) {
        perType.setName(name);
        perType.setSurName(surname);
    }

//    public EyesDefectType writeEyesDefects(VisualImpairment eyesDefect, ObjectFactory of) {
//        EyesDefectType edeft = of.createEyesDefectType();
//        edeft.setDescription(eyesDefect.getDescription());
//        return edeft;
//
//    }
//
//    public HearingDefectType writeHearingDefects(HearingImpairment hearingDefect,
//            ObjectFactory of) {
//        HearingDefectType hdeft = of.createHearingDefectType();
//        hdeft.setDescription(hearingDefect.getDescription());
//        return hdeft;
//
//    }

    public PersonAddParam writeAdditionalParams(
            PersonOptParamVal personAddParam, ObjectFactory of) {
        PersonAddParam paddp = of.createPersonAddParam();
        paddp.setName(personAddParam.getPersonOptParamDef().getParamName());
        paddp.setDataType(personAddParam.getPersonOptParamDef().getParamDataType());
        paddp.setValue(personAddParam.getParamValue());
        return paddp;
    }
}

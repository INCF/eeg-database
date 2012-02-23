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

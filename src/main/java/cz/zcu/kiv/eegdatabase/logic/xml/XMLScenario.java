
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.xmlObjects.ScenarioType;

/**
 *
 * @author Jan Štěbeták
 */
class XMLScenario {

    private ScenarioType scType;

    public XMLScenario(ScenarioType scType) {
        this.scType = scType;
    }

    public void writeTitle(String title) {
        scType.setTitle(title);
    }

    public void writeLength(String length){
        if (Integer.parseInt(length) < 0) return;
        scType.setLenght(length);
    }

    public void writeDescription(String desc) {
        scType.setDescription(desc);
    }
}

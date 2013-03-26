package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 25.3.13
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class ExperimentAddDTO extends IdentifiDTO implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

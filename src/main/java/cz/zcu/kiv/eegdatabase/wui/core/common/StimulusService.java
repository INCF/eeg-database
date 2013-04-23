package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
public interface StimulusService extends GenericService<Stimulus, Integer> {

    boolean canSaveDescription(String description);
}

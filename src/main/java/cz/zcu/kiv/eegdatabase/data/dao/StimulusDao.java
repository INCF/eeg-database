package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 18.4.13
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public interface StimulusDao extends GenericDao<Stimulus, Integer> {

    public boolean canSaveDescription(String description);

}

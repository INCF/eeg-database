package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 10:28
 */
public interface DiseaseFacade {
    public Integer create(Disease newInstance);

    public boolean existsDisease(String name);
}

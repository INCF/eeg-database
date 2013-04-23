package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectTypeFacade extends GenericFacade<ProjectType, Integer>{

    boolean canSaveTitle(String title);
}

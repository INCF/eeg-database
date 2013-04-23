package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectTypeService extends GenericService<ProjectType, Integer>{

    boolean canSaveTitle(String title);
}

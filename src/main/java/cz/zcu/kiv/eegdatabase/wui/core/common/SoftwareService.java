package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public interface SoftwareService extends GenericService<Software, Integer>{

    void createDefaultRecord(Software software);

    boolean isDefault(int id);

    boolean canSaveDefaultTitle(String title, int swId);
}

package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public interface PharmaceuticalFacade extends GenericFacade<Pharmaceutical, Integer> {

      public boolean canSaveTitle(String title);
}

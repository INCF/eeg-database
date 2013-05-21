package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 14.5.13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class HardwareConverter implements IConverter<Hardware> {
    private HardwareFacade hardwareFacade;

    public HardwareConverter(HardwareFacade hardwareFacade) {
        this.hardwareFacade = hardwareFacade;
    }

    @Override
    public Hardware convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        Hardware hardware = new Hardware();
        hardware.setTitle(s);
        List<Hardware> hardwares = hardwareFacade.getUnique(hardware);
        return (hardwares != null && hardwares.size() > 0) ? hardwares.get(0) : hardware;

    }

    @Override
    public String convertToString(Hardware hardware, Locale locale) {
        return hardware.getTitle();
    }
}
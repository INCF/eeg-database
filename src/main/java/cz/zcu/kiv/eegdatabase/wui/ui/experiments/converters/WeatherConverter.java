package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
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
public class WeatherConverter implements IConverter<Weather> {
    private WeatherFacade weatherFacade;

    public WeatherConverter(WeatherFacade weatherFacade) {
        this.weatherFacade = weatherFacade;
    }

    @Override
    public Weather convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        List<Weather> weathers = weatherFacade.readByParameter("title", s);
        return (weathers != null && weathers.size() > 0) ? weathers.get(0) : new Weather();

    }

    @Override
    public String convertToString(Weather weather, Locale locale) {
        return weather.getTitle();
    }
}
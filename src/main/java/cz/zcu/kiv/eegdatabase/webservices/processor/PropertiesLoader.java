package cz.zcu.kiv.eegdatabase.webservices.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Petr
 * Date: 3.4.12
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesLoader {

    private final static Log log = LogFactory.getLog(PropertiesLoader.class);

    public static String getKey(String key){

        try {
            InputStream inStream = PropertiesLoader.class.getClassLoader().getResourceAsStream("settings.properties");
            Properties props = new Properties();
            props.load(inStream);
            return props.getProperty(key);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);

        }
        return null;
    }
}

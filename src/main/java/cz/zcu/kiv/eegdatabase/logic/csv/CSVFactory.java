package cz.zcu.kiv.eegdatabase.logic.csv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: pbruha
 * Date: 13.12.11
 * Time: 6:56
 * To change this template use File | Settings | File Templates.
 */
public class CSVFactory {
    private Log log = LogFactory.getLog(getClass());

    public OutputStream generateCsvFile() throws IOException {

        log.debug("Creating output stream");
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        printStream.println("test;test");
        printStream.println("test;test");

        printStream.close();

        return out;


    }
}

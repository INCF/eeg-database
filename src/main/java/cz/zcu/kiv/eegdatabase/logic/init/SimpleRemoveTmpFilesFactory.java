package cz.zcu.kiv.eegdatabase.logic.init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/***********************************************************************************************************************
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2016 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * SimpleRemoveTmpFilesFactory, 2016/04/13 13:14 petr-jezek
 **********************************************************************************************************************/

/*

This class removes tmp zip files that can remain in java.tmp dir
* */
public class SimpleRemoveTmpFilesFactory implements RemoveTmpFilesFactory {

    private Log log = LogFactory.getLog(getClass());

    @Value("${semantic.transformation.regularInterval}")
    private int interval;

    @Value("${semantic.transformation.delayFirstTransform}")
    private int delay;

    public void init() {
        log.debug("RemoveTmpFiles task reached");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DeleteTask(), delay, interval);
    }

    private class DeleteTask extends TimerTask {

        @Override
        public void run() {
            log.debug("Starting deleting tmp files.");
            String property = "java.io.tmpdir";
            String tempDir = System.getProperty(property);
            log.debug("Temp dir is: " + tempDir);
            File f = new File(tempDir);
            if (f.isDirectory()) {
                File[] files = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        String lowercaseName = name.toLowerCase();
                        return (lowercaseName.startsWith("experimentdownload") && lowercaseName.endsWith(".zip")) ||
                                (lowercaseName.startsWith("section") && lowercaseName.endsWith(".ser"));
                    }
                });
                for (File file : files) {
                    long diff = new Date().getTime() - file.lastModified();
                    long oneDay = 86400000;
                    //delete only more than one day old files.
                    //It prevents deleting the files currently created when the thread starts
                    if (diff > oneDay) {
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            log.info("file: " + file + " has been deleted");
                        } else {
                            log.warn("file: " + file + " has NOT been deleted");
                        }
                    }
                }
                log.debug("Tmp files have been deleted");
            }
        }
    }
}

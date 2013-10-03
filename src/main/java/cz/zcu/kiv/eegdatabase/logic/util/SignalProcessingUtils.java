/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SignalProcessingUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 31.5.2011
 * Time: 13:42:41
 * To change this template use File | Settings | File Templates.
 */
public class SignalProcessingUtils {

    public static List<String> getHeaders(Experiment e) throws Exception {
        List<String> headers = new ArrayList<String>();
        for (DataFile file: e.getDataFiles()) {
            if (file.getFilename().endsWith(".vhdr")) {
                int index = file.getFilename().lastIndexOf(".");
                String name = file.getFilename().substring(0, index);
                for (DataFile file2: e.getDataFiles()) {
                    if ((file2.getFilename().endsWith(".eeg")) || (file2.getFilename().endsWith(".avg"))) {
                        if (file2.getFilename().startsWith(name)) {
                            headers.add(name);
                            break;
                        }
                    }
                }
            }
        }
        return headers;
    }

    public static void splitExperimentToView(ModelAndView mav, List<Experiment> list) throws Exception {
        List<Experiment> suitable = new ArrayList<Experiment>();
        List<Experiment> notSuitable = new ArrayList<Experiment>();
        for (Experiment e : list) {
            if (getHeaders(e).size() > 0) {
                suitable.add(e);
            } else {
                notSuitable.add(e);
            }
        }
        mav.addObject("suitable", suitable);
        mav.addObject("notSuitable", notSuitable);
    }

    public static byte[] extractZipEntry(ZipInputStream zis) throws Exception {
        byte[] buff = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int data = 0;
        while ((data = zis.read(buff, 0, 1024)) != -1) {
            out.write(buff, 0, data);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }
}

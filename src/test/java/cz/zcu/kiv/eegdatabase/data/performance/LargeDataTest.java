/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * LargeDataTest.java, 2014/06/10 20:01 Jan Stebetak
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.performance;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.ExperimentGenerator;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.Blob;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Honza on 10.6.14.
 */
public class LargeDataTest extends AbstractDataAccessTest {

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private DataFileDao dataFileDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ExperimentGenerator experimentGenerator;

    @Autowired
    private SessionFactory factory;


    private File tmpFile;
    private Experiment experiment;
    private DataFile dataFile;

    private final int KB = 1024;
    private final int MEGABYTES = 300;
    private final int KB_IN_MB = 1024;
    private long startTime;


    @Before
    public void setUp() {

        try {
            createFile();
            experiment = experimentGenerator.generateExperiment(createPerson());
            experimentDao.create(experiment);
        } catch (IOException e) {
            tmpFile = null;
        }

    }

    @Test
    @Transactional
    public void SaveDataTest() {
        if (tmpFile == null) {
            fail("Data file is null");
            return;
        }
        int count = dataFileDao.getCountRecords();
        startTime = System.currentTimeMillis();
        System.out.println(tmpFile.length());
        System.out.println(tmpFile.getAbsolutePath());
        dataFile = new DataFile();
        dataFile.setDescription("testDesc");
        dataFile.setFilename(tmpFile.getName());
        dataFile.setMimetype("temp");
        dataFile.setExperiment(experiment);
        InputStream ios;
        try {
            ios = new FileInputStream(tmpFile);
            dataFile.setFileContentStream(ios);
            Blob createBlob = factory.getCurrentSession().getLobHelper().createBlob(dataFile.getFileContentStream(), dataFile.getFileContentStream().available());
            dataFile.setFileContent(createBlob);

            dataFileDao.create(dataFile);
            assertEquals(count + 1, dataFileDao.getCountRecords());

        } catch (IOException e) {
            fail("Unreadable data file");
        } finally {
            tmpFile.delete();

        }
    }

    @After
    public void stopTime() {
        System.out.println(("Data File was stored in " + (System.currentTimeMillis() - startTime) / 1000) + " seconds");
    }
    private void createFile() throws IOException {
        if ((tmpFile == null) || (!tmpFile.exists())) {
            FileOutputStream fos = null;
            try {
                tmpFile = File.createTempFile("tmpDataFile", null);
                fos = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[KB];
                Arrays.fill(buffer, (byte) 0x0C);
                for (int i = 0; i < MEGABYTES * KB_IN_MB; i++) {
                    fos.write(buffer);
                }
            } catch (IOException e) {
                tmpFile = null;
            } finally {
                if (fos != null) fos.close();
            }
        }
    }
    private Person createPerson() {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        personDao.create(person);

        return person;
    }
}

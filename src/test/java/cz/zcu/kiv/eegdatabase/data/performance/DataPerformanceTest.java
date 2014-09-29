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
* DataPerformanceTest.java, 2014/06/10 20:01 Jan Stebetak
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
import org.junit.After;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.io.*;
import java.sql.Blob;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
* Created by Honza on 10.6.14.
*/
public class DataPerformanceTest extends AbstractDataAccessTest {

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private DataFileDao dataFileDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ExperimentGenerator experimentGenerator;

    private File tmpFile;
    private Experiment experiment;
    private DataFile dataFile;

    private final int KB = 1024;
    private final int KB_IN_MB = 1024;
    private long startTime;


    @BeforeMethod(groups = "unit")
    public void setUp() {
        if (experiment == null) {
            experiment = experimentGenerator.generateExperiment(createPerson());
            experimentDao.create(experiment);
        }

    }

    @Test(groups = "unit")
    public void saveDataTest() throws IOException {
        try {
            createFile(20);
        } catch (IOException e) {
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
        InputStream ios = null;
        try {
            ios = new FileInputStream(tmpFile);
            dataFile.setFileContentStream(ios);
            Blob createBlob = dataFileDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob
                    (dataFile.getFileContentStream(), dataFile.getFileContentStream().available());
            dataFile.setFileContent(createBlob);

            dataFileDao.create(dataFile);
            assertEquals(count + 1, dataFileDao.getCountRecords());

        } catch (IOException e) {
            fail("Unreadable data file");
        } finally {
            if (ios != null) ios.close();

        }
    }

    @Test(groups = "unit")
    public void saveDataRepeatTest() throws IOException {
        int count = dataFileDao.getCountRecords();
        int repeatLimit = 10;
        startTime = System.currentTimeMillis();
        InputStream ios = null;
        try {
            createFile(100);

        } catch (IOException e) {
            fail("Data file is null");
            return;
        }
        for (int i = 0; i < repeatLimit; i++) {
            dataFile = new DataFile();
            dataFile.setDescription("testDesc");
            dataFile.setFilename(tmpFile.getName() + "" + i);
            dataFile.setMimetype("temp");
            dataFile.setExperiment(experiment);

            try {
                ios = new FileInputStream(tmpFile);
                dataFile.setFileContentStream(ios);
                Blob createBlob = dataFileDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob
                        (dataFile.getFileContentStream(), dataFile.getFileContentStream().available());
                dataFile.setFileContent(createBlob);
                dataFileDao.create(dataFile);
            } catch (IOException e) {
                fail("Unreadable data file");

            }
        }
        assertEquals(count + repeatLimit, dataFileDao.getCountRecords());
    }

    @AfterMethod
    public void stopTime() {
        System.out.println(("Data File was stored in " + (System.currentTimeMillis() - startTime) / 1000) + " seconds");
        tmpFile.delete();
    }
    private void createFile(int megabytes) throws IOException {
        if ((tmpFile == null) || (!tmpFile.exists())) {
            FileOutputStream fos = null;
            try {
                tmpFile = File.createTempFile("tmpDataFile", null);
                fos = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[KB];
                Arrays.fill(buffer, (byte) 0x0C);
                for (int i = 0; i < megabytes * KB_IN_MB; i++) {
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

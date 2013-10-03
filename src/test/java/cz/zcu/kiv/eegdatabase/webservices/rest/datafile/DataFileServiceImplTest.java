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
 *   DataFileServiceImplTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;

/**
 * Test for data file service.
 *
 * @author Petr Miko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class DataFileServiceImplTest {

    @Autowired
    private DataFileService service;
    @Autowired
    @Qualifier("dataFileDao")
    private DataFileDao dataFileDao;

    @Test
    @Transactional
    public void testCreate() throws Exception {
        File tmp = File.createTempFile("junit","test");

        //first available experiment and mock file
        int pk =  service.create(0, "junit", new MockMultipartFile("junit", "junit.txt", "application/octet-stream", new FileInputStream(tmp)));
        DataFile file = dataFileDao.read(pk);

        try {
            Assert.assertNotNull(file);
            Assert.assertEquals("junit", file.getDescription());
            Assert.assertEquals("junit.txt", file.getFilename());
            Assert.assertEquals("application/octet-stream", file.getMimetype());
        } finally {
            tmp.delete();
            if(file != null)
//          cleanup
            dataFileDao.delete(file);
        }


    }
}

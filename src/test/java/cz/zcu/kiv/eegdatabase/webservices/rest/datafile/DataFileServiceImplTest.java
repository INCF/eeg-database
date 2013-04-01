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

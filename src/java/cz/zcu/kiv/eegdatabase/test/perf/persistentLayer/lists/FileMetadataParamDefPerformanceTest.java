package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LFMD_14_WorWitFmd_L/. Contains document Testovaci scenare.docx.
 */
public class FileMetadataParamDefPerformanceTest extends PerformanceTest {

    @Autowired
    PersonDao personeDao;

    @Autowired
    FileMetadataParamDefDao fileMetadataParamDefDao;




    private FileMetadataParamDef fileMetadataParamDef;

/**
 * Method test create File Metada.
 * Identificator of test / PPT_LFMD_15_AddFmd_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createFileMetadaTest(){
        fileMetadataParamDef = new FileMetadataParamDef();
        fileMetadataParamDef.setParamName("FileName");
        fileMetadataParamDef.setParamDataType("dataTyep");
        fileMetadataParamDefDao.create(fileMetadataParamDef);
    }

/**
 * Method test edit File Metada.
 * Identificator of test / PPT_LFMD_16_EditFmd_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editFileMetadaTest(){
        fileMetadataParamDef.setParamName("FileName");
        fileMetadataParamDef.setParamDataType("dataTyep");
        fileMetadataParamDefDao.update(fileMetadataParamDef);
    }
/**
 * Method test delete File Metada.
 * Identificator of test / PPT_LFMD_17_DelFmd_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteFileMetadaTest(){
        fileMetadataParamDefDao.delete(fileMetadataParamDef);
    }
}

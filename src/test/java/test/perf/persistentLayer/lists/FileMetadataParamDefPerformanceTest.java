package test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import test.perf.persistentLayer.PerformanceTest;
import org.junit.Test;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Richard Kocman, Kabourek
 * Date: 23.5.11
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LFMD_14_WorWitFmd_L/. Contains document Testovaci scenare.docx.
 */
public class FileMetadataParamDefPerformanceTest extends PerformanceTest {

    /**
     * Constant for atribute of test data.
     */

    public static final String FILE_METADATA_PARAM_DEF_NAME = "testovaci parametr";
    public static final String FILE_METADATA_PARAM_DEF_DATA_TYPE = "testovaci datatype";

    private FileMetadataParamDef fileMetadataParamDef;
    FileMetadataParamDefDao fileMetadataParamDefDao;


    /**
* Method test create fileMetadataParamDef for next test.
*
*/

    public void createTestFileMetadataParamDef(){
        fileMetadataParamDef = new FileMetadataParamDef();
        fileMetadataParamDef.setParamName(FILE_METADATA_PARAM_DEF_NAME);
        fileMetadataParamDef.setParamDataType(FILE_METADATA_PARAM_DEF_DATA_TYPE);
    }



/**
 * Method test create File Metada.
 * Identificator of test / PPT_LFMD_15_AddFmd_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateFileMetadaTest(){
        int countRecord = fileMetadataParamDefDao.getCountRecords();

        createTestFileMetadataParamDef();
        fileMetadataParamDefDao.create(fileMetadataParamDef);

        assertEquals(fileMetadataParamDefDao.getCountRecords()-1, countRecord);
    }

/**
 * Method test edit File Metada.
 * Identificator of test / PPT_LFMD_16_EditFmd_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editFileMetadaTest(){
        List<FileMetadataParamDef> listRecords;

        fileMetadataParamDef.setParamName(FILE_METADATA_PARAM_DEF_NAME+"EDITOVANO");
        fileMetadataParamDefDao.update(fileMetadataParamDef);

        listRecords=fileMetadataParamDefDao.getAllRecords();
        assertEquals(listRecords.get(listRecords.size()-1).getParamName(), fileMetadataParamDef.getParamName());
    }
/**
 * Method test delete File Metada.
 * Identificator of test / PPT_LFMD_17_DelFmd_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteFileMetadaTest(){
        int countRecord = fileMetadataParamDefDao.getCountRecords();
        fileMetadataParamDefDao.delete(fileMetadataParamDef);

        assertEquals(fileMetadataParamDefDao.getCountRecords()+1, countRecord);
    }

     /**
     * Setter for DAO object.
     */

    public void setFileMetadataParamDefDao(FileMetadataParamDefDao fileMetadataParamDefDao) {
        this.fileMetadataParamDefDao = fileMetadataParamDefDao;
    }
}

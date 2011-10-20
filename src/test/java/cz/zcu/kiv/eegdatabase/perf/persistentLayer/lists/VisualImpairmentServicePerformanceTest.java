package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
import java.util.List;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LWI_22_WorWitWi_L/. Contains document Testovaci scenare.docx.
 */
public class VisualImpairmentServicePerformanceTest extends PerformanceTest {

    /**
     * Constant for atribute of test data.
     */
    public static final String VISUAL_IMPAIRMENT_DESCRIPTION = "popis poskozeni";



    VisualImpairmentDao visualImpairmentDao;
    private VisualImpairment visualImpairment;


        /**
* Method test create article for next test.
*
*/

     public void createTestVisualImpairment(){
        visualImpairment = new VisualImpairment();
        visualImpairment.setDescription(VISUAL_IMPAIRMENT_DESCRIPTION);

    }

/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LWI_23_AddWi_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateVisualImpairmentTest(){

      int countRecord = visualImpairmentDao.getCountRecords();

      createTestVisualImpairment();
      visualImpairmentDao.create(visualImpairment);

      assertEquals(visualImpairmentDao.getCountRecords() - 1, countRecord);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LWI_24_EditWi_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditVisualImpairmentTest(){

        List<VisualImpairment> listRecords;

        visualImpairment.setDescription(VISUAL_IMPAIRMENT_DESCRIPTION+"EDITOAVANY");

        visualImpairmentDao.update(visualImpairment);

        listRecords=visualImpairmentDao.getAllRecords();
        assertEquals(listRecords.get(listRecords.size() - 1).getDescription(), visualImpairment.getDescription());
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LWI_25_DelWi_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeleteVisualImpairmentTest(){

        int countRecord = visualImpairmentDao.getCountRecords();

        visualImpairmentDao.delete(visualImpairment);

        assertEquals(visualImpairmentDao.getCountRecords()+1, countRecord);
    }

      /**
     * Setter for DAO object.
     */
     public void setVisualImpairmentDao(VisualImpairmentDao visualImpairmentDao) {
        this.visualImpairmentDao = visualImpairmentDao;
    }
}

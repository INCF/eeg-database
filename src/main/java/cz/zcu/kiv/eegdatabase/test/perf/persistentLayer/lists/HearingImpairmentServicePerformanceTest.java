package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.helper.DateFormater;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LHI_26_WorHi_L/. Contains document Testovaci scenare.docx.
 */
public class HearingImpairmentServicePerformanceTest extends PerformanceTest {


    /**
     * Constant for atribute of test data.
     */
    public static final String HEARING_IMPAIRMENT_DESCRIPTION = "popis poskozeni";


    HearingImpairmentDao hearingImpairmentDao;
    private HearingImpairment hearingImpairment;


    /**
* Method test create article for next test.
*
*/

     public void createTestHearingImpairment(){
        hearingImpairment = new HearingImpairment();
        hearingImpairment.setDescription(DateFormater.createTestDate(HEARING_IMPAIRMENT_DESCRIPTION));

    }

/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LHI_27_AddHi_F /. Contains document Testovaci scenare.docx.
 */
    @Test
   public void testCreateHearingImpairmentTest(){
      int countRecord = hearingImpairmentDao.getCountRecords();

      createTestHearingImpairment();
      hearingImpairmentDao.create(hearingImpairment);

      assertEquals(hearingImpairmentDao.getCountRecords()-1, countRecord);
        hearingImpairmentDao.delete(hearingImpairment);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LHI_28_EditHi_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditHearingImpairmentTest(){
        List<HearingImpairment> listRecords;
        createTestHearingImpairment();
        hearingImpairment.setDescription("Edituji");

        hearingImpairmentDao.update(hearingImpairment);

        listRecords=hearingImpairmentDao.getAllRecords();
        assertEquals(listRecords.get(listRecords.size()-1).getDescription(), hearingImpairment.getDescription());
        hearingImpairmentDao.delete(hearingImpairment);

    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LHI_29_DelWi_F/. Contains document Testovaci scenare.docx.
 */
    @Test
        public void testDeleteHearingImpairmentTest(){

        int countRecord = hearingImpairmentDao.getCountRecords();

        hearingImpairmentDao.delete(hearingImpairment);

        assertEquals(hearingImpairmentDao.getCountRecords()+1, countRecord);
    }

       /**
     * Setter for DAO object.
     */

    public void setHearingImpairmentDao(HearingImpairmentDao hearingImpairmentDao) {
        this.hearingImpairmentDao = hearingImpairmentDao;
    }

}

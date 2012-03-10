package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
import java.util.List;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LOPP_6_WorWitOpp_L/. Contains document Testovaci scenare.docx.
 */
public class PersonOptParamDefPerformanceTest extends PerformanceTest {


    /**
     * Constant for atribute of test data.
     */

    public static final String PERSON_OPT_PARAM_DEF_NAME = "testovaci parametr";
    public static final String PERSON_OPT_PARAM_DEF_DATA_TYPE = "testovaci datatype";



    PersonOptParamDefDao personOptParamDefDao;

    private PersonOptParamDef personOptParamDef;


    /**
* Method test create article for next test.
*
*/

     public void createTestPersonOptParamDef(){
        personOptParamDef = new PersonOptParamDef();
        personOptParamDef.setParamName(PERSON_OPT_PARAM_DEF_NAME);
        personOptParamDef.setParamDataType(PERSON_OPT_PARAM_DEF_DATA_TYPE);
        personOptParamDefDao.create(personOptParamDef);
    }

/**
 * Method test create personOptParamDef
 * Identificator of test / PPT_LOPP_7_AddOpp_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreatePersonOptParamTest(){
        int countRecord =  personOptParamDefDao.getCountRecords();

        createTestPersonOptParamDef();

         assertEquals(personOptParamDefDao.getCountRecords()-1, countRecord);
    }

/**
 * Method test edit personOptParamDef
 * Identificator of test / PPT_LOPP_8_EdiOpp_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditPersonOptParamTest(){
        createTestPersonOptParamDef();
        List<PersonOptParamDef> listRecords;

        personOptParamDef.setParamName(PERSON_OPT_PARAM_DEF_NAME+"Editovany");
        personOptParamDefDao.update(personOptParamDef);

        listRecords=personOptParamDefDao.getAllRecords();
        assertEquals(personOptParamDefDao.read(personOptParamDef.getPersonOptParamDefId()).getParamName(), personOptParamDef.getParamName());
    }
/**
 * Method test delete personOptParamDef
 * Identificator of test / PPT_LOPP_9_DelOpp_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeletePersonOptParamTest(){
        createTestPersonOptParamDef();

        int countRecord =  personOptParamDefDao.getCountRecords();
        personOptParamDefDao.delete(personOptParamDef);

        assertEquals(personOptParamDefDao.getCountRecords()+1, countRecord);
    }

      /**
     * Setter for DAO object.
     */
    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }
}

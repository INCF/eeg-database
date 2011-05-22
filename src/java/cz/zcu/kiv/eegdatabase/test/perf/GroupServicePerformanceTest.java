package cz.zcu.kiv.eegdatabase.test.perf;

import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public class GroupServicePerformanceTest extends PerformanceTest {
    @Autowired
    ResearchGroupDao researchGroupDao;

    private ResearchGroup researchGroup;

    //@Test
    public void createGroupTest(){
      researchGroup.setDescription("popis");
        researchGroup.setTitle("skupina");

        researchGroupDao.create(researchGroup);

    }
}

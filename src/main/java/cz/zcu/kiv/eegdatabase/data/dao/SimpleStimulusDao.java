package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 18.4.13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public class SimpleStimulusDao extends SimpleGenericDao<Stimulus, Integer>
        implements StimulusDao {

    public SimpleStimulusDao() {
        super(Stimulus.class);
    }

    @Override
    public boolean canSaveDescription(String description) {
                String hqlQuery = "from Stimulus s where s.description = :description";
        List<Stimulus> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("description", description)
                .list();
        return (list.size()==0);
    }
}

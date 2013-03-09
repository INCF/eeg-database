package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 9.11.11
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServiceResultDao extends SimpleGenericDao<ServiceResult, Integer> implements ServiceResultDao {
    public SimpleServiceResultDao() {
        super(ServiceResult.class);
    }

    @Override
    public List<ServiceResult> getResultByPerson(int personId) {
        String hqlQuery = "from ServiceResult s where s.owner.personId = :personId";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "personId", personId);
    }
}

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 24.2.12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDigitizationDao <T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements DigitizationDao<T, PK> {

    public SimpleDigitizationDao(Class<T> type) {
        super(type);
    }

    @Override
    public Digitization getDigitizationByParams(float samplingRate, float gain, String filter) {
        String[] paramNames = {"samplingRate", "gain", "filter"};
        Object[] values = {samplingRate, gain, filter};
        String HQLQuery = "from Digitization d where d.samplingRate = :samplingRate and d.gain = :gain and d.filter = :filter";
        List<Digitization> list = getHibernateTemplate().findByNamedParam(HQLQuery, paramNames, values);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}

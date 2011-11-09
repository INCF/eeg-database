package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 9.11.11
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceResultDao extends GenericDao<ServiceResult, Integer> {

    public List<ServiceResult> getResultByPerson(int personId);
}

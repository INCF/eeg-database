package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 24.2.12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public interface DigitizationDao <T, PK extends Serializable> extends GenericDao<T, PK> {

    public Digitization getDigitizationByParams(float samplingRate, float gain, String filter);
}

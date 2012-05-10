package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 24.2.12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public interface DigitizationDao extends GenericListDao<Digitization> {

    public Digitization getDigitizationByParams(float samplingRate, float gain, String filter);
}

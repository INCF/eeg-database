package cz.zcu.kiv.eegdatabase.wui.core.experimentLicense;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentLicenceDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Lichous on 28.4.15.
 */
public class ExperimentLicenseServiceImpl extends GenericServiceImpl<ExperimentLicence, Integer> implements ExperimentLicenseService {

    protected Log log = LogFactory.getLog(getClass());
    private ExperimentLicenceDao dao;

    public ExperimentLicenseServiceImpl(ExperimentLicenceDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(ExperimentLicenceDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(Experiment experiment, License license) {
        dao.remove(experiment,license);
    }
}

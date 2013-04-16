package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import java.util.List;
import org.hibernate.Query;


/**
 * Hibernate implementation of ExperimentPackageConnectionDao
 *
 * @author bydga
 */
public class SimpleExperimentPackageConnectionDao extends SimpleGenericDao<ExperimentPackageConnection, Integer> implements ExperimentPackageConnectionDao {

    public SimpleExperimentPackageConnectionDao() {
	super(ExperimentPackageConnection.class);
    }

    @Override
    public List<Experiment> listExperimentsByPackage(int packageId) {
	String HQL = "select e from Experiment e, ExperimentPackageConnection epc left join fetch e.scenario "
		+ "where e.experimentId = epc.experiment.experimentId "
		+ "AND epc.experimentPackage.experimentPackageId = :packageId";

	Query query = getSessionFactory().getCurrentSession().createQuery(HQL);
	query.setParameter("packageId", packageId);
	return query.list();
    }
    
}

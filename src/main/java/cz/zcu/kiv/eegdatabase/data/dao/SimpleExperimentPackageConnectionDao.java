package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Session;


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
    @Override
    public boolean isExperimentInPackage(int experimentId, int packageId) {
	Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(type);
        criteria.add(Restrictions.eq("experiment.experimentId", experimentId));
	criteria.add(Restrictions.eq("experimentPackage.experimentPackageId", packageId));
        criteria.setProjection(Projections.rowCount());
	int count = ((Number) criteria.uniqueResult()).intValue();
	return count > 0;
    }

	@Override
	public void removeExperimentFromPackage(int experimentId, int packageId) {
		String hqlQuery = "delete from ExperimentPackageConnection epc where epc.experiment = :experiment and epc.experimentPackage = :package";
        this.getSession().createQuery(hqlQuery).setInteger("experiment", experimentId).setInteger("package", packageId).executeUpdate();
    }
    
    @Override
    public Integer create(ExperimentPackageConnection newInstance) {
	boolean present = this.isExperimentInPackage(newInstance.getExperiment().getExperimentId(),
				newInstance.getExperimentPackage().getExperimentPackageId());
	if(present) {
	    //already there
	    return -1;
	}
    
	return super.create(newInstance);
    }    
}

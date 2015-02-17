package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;

public class SimpleOrderDao extends SimpleGenericDao<Order, Integer> implements OrderDao {

    public SimpleOrderDao() {
        super(Order.class);
    }

    @Override
    public Order getOrderForDetail(int orderId) {

        String query = "from Order o "
                + "left join fetch o.person "
                + "left join fetch o.items as items "
                + "left join fetch items.experiment as exp "
                + "left join fetch items.experimentPackage as pck "
                + "left join fetch exp.scenario "
                + "left join fetch pck.researchGroup "
                + "where o.id = :orderId";
        return (Order) getSessionFactory().getCurrentSession().createQuery(query).setParameter("orderId", orderId).uniqueResult();
    }

    @Override
    public boolean isExperimentPurchased(int experimentId, int personId) {

        String query = "from Order o "
                + "left join fetch o.person "
                + "left join fetch o.items as items "
                + "left join fetch items.experiment as exp "
                + "where o.person.personId = :personId and exp.experimentId = :experimentId";
        boolean purchased = (Order) getSessionFactory().getCurrentSession().createQuery(query)
                .setParameter("personId", personId)
                .setParameter("experimentId", experimentId).uniqueResult() != null;

        String query2 = "from Order o "
                + "left join fetch o.person "
                + "left join fetch o.items as items "
                + "left join fetch items.experimentPackage as pck "
                + "left join fetch pck.experimentPackageConnections as pckCon "
                + "where o.person.personId = :personId and pckCon.experiment.experimentId = :experimentId";
        purchased = purchased || ((Order) getSessionFactory().getCurrentSession().createQuery(query2)
                .setParameter("personId", personId)
                .setParameter("experimentId", experimentId).uniqueResult() != null);

        return purchased;
    }

    @Override
    public boolean isExperimentPackagePurchased(int experimentPackageId, int personId) {

        String query = "from Order o "
                + "left join fetch o.person "
                + "left join fetch o.items as items "
                + "left join fetch items.experimentPackage as pck "
                + "where o.person.personId = :personId and pck.experimentPackageId = :packageId";
        return (Order) getSessionFactory().getCurrentSession().createQuery(query)
                .setParameter("personId", personId)
                .setParameter("packageId", experimentPackageId).uniqueResult() != null;
    }

    @Override
    public Set<Integer> getPurchasedExperimentId(int personId) {

        String queryPurchasedExperiments = "select exp.experimentId from Order o "
                + "join o.person as person "
                + "join o.items as items "
                + "join items.experiment as exp "
                + "where person.personId = :personId and exp is not null";
        List<Integer> purchasedExperimentIds = (List<Integer>) getSessionFactory().getCurrentSession().createQuery(queryPurchasedExperiments)
                .setParameter("personId", personId)
                .list();

        String queryExperimentsInPurchasedPackage = "select pckCon.experiment.experimentId from Order o "
                + "join o.person as person "
                + "join o.items as items "
                + "join items.experimentPackage as pck "
                + "join pck.experimentPackageConnections as pckCon "
                + "where person.personId = :personId and pck is not null";
        List<Integer> experimentIdsFromPurchasedPackage = (List<Integer>) getSessionFactory().getCurrentSession().createQuery(queryExperimentsInPurchasedPackage)
                .setParameter("personId", personId)
                .list();

        HashSet<Integer> set = new HashSet<Integer>(purchasedExperimentIds);
        set.addAll(experimentIdsFromPurchasedPackage);
        return set;
    }

    @Override
    public Set<Integer> getPurchasedExperimentPackageId(int personId) {

        String query = "select pck.experimentPackageId from Order o "
                + "join o.person "
                + "join o.items as items "
                + "join items.experimentPackage as pck "
                + "where o.person.personId = :personId and pck is not null";
        List<Integer> list = (List<Integer>) getSessionFactory().getCurrentSession().createQuery(query)
                .setParameter("personId", personId)
                .list();

        return new HashSet<Integer>(list);
    }

    @Override
    public OrderItem getOrderItemForExperiment(int experimentId, int personId) {

        String query = "from OrderItem oi "
                + "join fetch oi.order as o "
                + "left join fetch oi.experiment as exp "
                + "where o.person.personId = :personId and exp.experimentId = :experimentId";
        OrderItem item = (OrderItem) getSessionFactory().getCurrentSession().createQuery(query)
                .setParameter("personId", personId)
                .setParameter("experimentId", experimentId).uniqueResult();

        if (item == null) {
            String query2 = "from OrderItem oi "
                    + "join fetch oi.order as o "
                    + "left join fetch oi.experimentPackage as pck "
                    + "left join fetch pck.experimentPackageConnections as pckCon "
                    + "where o.person.personId = :personId and pckCon.experiment.experimentId = :experimentId order by o.date asc";
            List<OrderItem> list = (List<OrderItem>) getSessionFactory().getCurrentSession().createQuery(query2)
                    .setParameter("personId", personId)
                    .setParameter("experimentId", experimentId).list();

            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0);
            }

        } else {
            return item;
        }
    }
}

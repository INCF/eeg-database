package cz.zcu.kiv.eegdatabase.wui.core.order;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.OrderDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;

public class OrderServiceImpl extends GenericServiceImpl<Order, Integer> implements OrderService {

    protected Log log = LogFactory.getLog(getClass());
    private OrderDao dao;

    public OrderServiceImpl(OrderDao orderDao) {
        super(orderDao);
        setDao(orderDao);
    }

    public void setDao(OrderDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrdersForPerson(int personId) {
        return dao.readByParameter("person.personId", personId);
    }

    @Override
    @Transactional
    public Order getOrderForDetail(int orderId) {
        return dao.getOrderForDetail(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExperimentPurchased(int experimentId, int personId) {
        return dao.isExperimentPurchased(experimentId, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExperimentPackagePurchased(int experimentPackageId, int personId) {
        return dao.isExperimentPackagePurchased(experimentPackageId, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Integer> getPurchasedExperimentId(int personId) {
        return dao.getPurchasedExperimentId(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Integer> getPurchasedExperimentPackageId(int personId) {
        return dao.getPurchasedExperimentPackageId(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem getOrderItemForExperiment(int experimentId, int personId) {
        return dao.getOrderItemForExperiment(experimentId, personId);
    }
}

package cz.zcu.kiv.eegdatabase.wui.core.order;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;

public class OrderFacadeImpl extends GenericFacadeImpl<Order, Integer> implements OrderFacade {

    protected Log log = LogFactory.getLog(getClass());
    private OrderService service;

    public OrderFacadeImpl(OrderService orderService) {
        super(orderService);
        setService(orderService);
    }

    @Required
    public void setService(OrderService service) {
        this.service = service;
    }
    
    @Override
    public List<Order> getAllOrdersForPerson(int personId) {
        return service.getAllOrdersForPerson(personId);
    }

    @Override
    public Order getOrderForDetail(int orderId) {
        return service.getOrderForDetail(orderId);
    }

    @Override
    public boolean isExperimentPurchased(int experimentId, int personId) {
        return service.isExperimentPurchased(experimentId, personId);
    }

    @Override
    public boolean isExperimentPackagePurchased(int experimentPackageId, int personId) {
        return service.isExperimentPackagePurchased(experimentPackageId, personId);
    }

    @Override
    public Set<Integer> getPurchasedExperimentId(int personId) {
        return service.getPurchasedExperimentId(personId);
    }

    @Override
    public Set<Integer> getPurchasedExperimentPackageId(int personId) {
        return service.getPurchasedExperimentPackageId(personId);
    }

}

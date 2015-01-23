package cz.zcu.kiv.eegdatabase.wui.core.order;

import java.util.List;
import java.util.Set;

import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface OrderService extends GenericService<Order, Integer> {
    
    List<Order> getAllOrdersForPerson(int personId);
    
    Order getOrderForDetail(int orderId);
    
    boolean isExperimentPurchased(int experimentId, int personId);
    
    boolean isExperimentPackagePurchased(int experimentPackageId, int personId);
    
    Set<Integer> getPurchasedExperimentId(int personId);
    
    Set<Integer> getPurchasedExperimentPackageId(int personId);
    
    OrderItem getOrderItemForExperiment(int experimentId, int personId);
}

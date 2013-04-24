package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Object of ShoppingCart. Keeps track of experiments placed in an order, order's total price. Provides basic methods
 * for manipulation with its content - add/remove experiment.
 * User: jfronek
 * Date: 4.3.2013
 */
public class ShoppingCart implements Serializable {
    /** Unified price of all experiments. For prototype purposes only. */
    public static double EXPERIMENT_TEST_PRICE = 5.0;
    private List<Experiment> order = new ArrayList<Experiment>();

    public List<Experiment> getOrder() {
        return order;
    }

    public double getTotalPrice(){
        double total = 0.0;
        for(Experiment experiment : order){
            total += EXPERIMENT_TEST_PRICE;
        }
        return total;
    }

    public void addToCart(Experiment experiment){
        // Each experiment can be put into cart only once.
        if(!isInCart(experiment)){
            order.add(experiment);
        }
    }

    public boolean isInCart(Experiment experiment){
        for(Experiment ex : order){
            if(experiment.getExperimentId() == ex.getExperimentId()){
                return true;
            }
        }
        return false;
    }

    public void removeFromCart(Experiment experiment){
        for(int index = 0; index < order.size(); index++){
            if(experiment.getExperimentId() == order.get(index).getExperimentId()){
                order.remove(index);
            }
        }
    }
    public boolean isEmpty(){
        return order.isEmpty();
    }

    public int size(){
        return order.size();
    }


}

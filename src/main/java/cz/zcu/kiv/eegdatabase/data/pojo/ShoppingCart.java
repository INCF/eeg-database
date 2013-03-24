package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 4.3.13
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingCart implements Serializable {
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

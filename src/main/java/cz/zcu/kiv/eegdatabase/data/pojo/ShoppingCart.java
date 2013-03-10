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
    private List<Experiment> order = new ArrayList<Experiment>();

    public List<Experiment> getOrder() {
        return order;
    }

    public void setOrder(List<Experiment> other) {
        order = other;
    }

    public double getTotal(){
        double total = 0.0;
        for(Experiment experiment : order){
            //total += experiment.getPrice();
        }
        return total;
    }




}

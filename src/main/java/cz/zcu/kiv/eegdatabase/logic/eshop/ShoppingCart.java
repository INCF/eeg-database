/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ShoppingCart.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.eshop;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

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

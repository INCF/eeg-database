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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;

public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 9082679939344208647L;

    private Order order;

    public ShoppingCart() {

        this.order = new Order();
    }

    public Order getOrder() {
        return order;
    }

    // never return null. Always return zero or something.
    public BigDecimal getTotalPrice() {

        BigDecimal total = BigDecimal.ZERO;

        // TODO rounding should be set by global settings
        total.round(new MathContext(2, RoundingMode.HALF_UP));

        for (OrderItem item : order.getItems()) {

            // !!! add method for big decimal isn't null safe.
            total = total.add(item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO);
        }

        return total;
    }

    public void addToCart(Experiment experiment) {

        if (!isInCart(experiment)) {
            order.getItems().add(new OrderItem(experiment, order));
        }
    }

    public void addToCart(ExperimentPackage expPackage) {

        if (!isInCart(expPackage)) {
            order.getItems().add(new OrderItem(expPackage, order));
        }
    }

    public boolean isInCart(Experiment experiment) {

        for (OrderItem tmp : order.getItems()) {

            if (tmp.getExperiment() == null) {
                continue;
            } else if (tmp.getExperiment().getExperimentId() == experiment.getExperimentId()) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public boolean isInCart(ExperimentPackage expPackage) {

        for (OrderItem tmp : order.getItems()) {

            if (tmp.getExperimentPackage() == null) {
                continue;
            } else if (tmp.getExperimentPackage().getExperimentPackageId() == expPackage.getExperimentPackageId()) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public void removeFromCart(OrderItem item) {

        order.getItems().remove(item);
    }

    public boolean isEmpty() {
        return order.getItems().isEmpty();
    }

    public int size() {
        return order.getItems().size();
    }
    
    public void clear() {
        this.order = new Order();
    }

}

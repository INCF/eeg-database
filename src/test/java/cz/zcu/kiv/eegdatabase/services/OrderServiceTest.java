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
 *   OrderServiceTest.java, 2015/03/11 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.testng.Assert.assertEquals;

/**
 * Created by Honza on 11.3.2015.
 */
public class OrderServiceTest extends AbstractServicesTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private OrderService orderService;

    private Order order;
    private Person person;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);
        order = new Order();
        order.setPerson(person);
        order.setDate(new Timestamp(100));
        order.setOrderPrice(new BigDecimal(1000));
    }

    @Test(groups = "unit")
    public void testCreateOrderWithoutItems() {
        int orderCountBefore = orderService.getAllRecords().size();
        int orderID = orderService.create(order);
        assertEquals(orderCountBefore + 1, orderService.getAllRecords().size());
        assertEquals(orderID, order.getId());
    }

    @Test(groups = "unit")
    public void testGetOrderForDetails() {
        int orderCountBefore = orderService.getAllRecords().size();
        int orderID = orderService.create(order);
        assertEquals(orderCountBefore + 1, orderService.getAllRecords().size());

        Order orderForDetail = orderService.getOrderForDetail(orderID);
        assertEquals("1000", orderForDetail.getOrderPrice().toString());
        assertEquals(0, orderForDetail.getItems().size());
    }

    @Test(groups = "unit")
    public void testGetAllOrderForPerson() {
        int orderCountBefore = orderService.getAllRecords().size();
        int orderCountForPersonBefore = orderService.getAllOrdersForPerson(person.getPersonId()).size();
        orderService.create(order);
        assertEquals(orderCountBefore + 1, orderService.getAllRecords().size());

        Order order2 = new Order();
        order2.setPerson(person);
        order2.setDate(new Timestamp(100));
        order2.setOrderPrice(new BigDecimal(1000));
        orderService.create(order2);

        Person newPerson = TestUtils.createPersonForTesting("new name", Util.ROLE_USER);
        personDao.create(newPerson);
        Order order3 = new Order();
        order3.setPerson(newPerson);
        order3.setDate(new Timestamp(100));
        order3.setOrderPrice(new BigDecimal(1000));
        orderService.create(order3);

        assertEquals(orderCountForPersonBefore + 2, orderService.getAllOrdersForPerson(person.getPersonId()).size());
        assertEquals(orderCountBefore + 3, orderService.getAllRecords().size());

    }
}


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
 *   OrderDaoTest.java, 2015/02/02 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.testng.Assert.assertEquals;

/**
 * Created by stebjan on 10.2.2015.
 */
public class OrderDaoTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private OrderDao orderDao;

    private Order order;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        order = new Order();
        order.setPerson(person);
        order.setDate(new Timestamp(100));
        order.setOrderPrice(new BigDecimal(1000));
    }

    @Test(groups = "unit")
    public void testCreateOrderWithoutItems() {
        int orderCountBefore = orderDao.getAllRecords().size();
        int orderID = orderDao.create(order);
        assertEquals(orderCountBefore + 1, orderDao.getAllRecords().size());
        assertEquals(orderID, order.getId());
    }

    @Test(groups = "unit")
    public void testGetOrderForDetails() {
        int orderCountBefore = orderDao.getAllRecords().size();
        int orderID = orderDao.create(order);
        assertEquals(orderCountBefore + 1, orderDao.getAllRecords().size());

        Order orderForDetail = orderDao.getOrderForDetail(orderID);
        assertEquals("1000", orderForDetail.getOrderPrice().toString());
        assertEquals(0, orderForDetail.getItems().size());
    }
}

/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * FormLayoutDaoTest.java, 28. 3. 2014 22:21:49, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains test cases for form-layout DAO object.
 *
 * @author Jakub Krauz
 */

public class FormLayoutDaoTest extends AbstractDataAccessTest {

	@Autowired
	private FormLayoutDao formLayoutDao;
    @Autowired
    private PersonDao personDao;

	private Person testPerson;


    @Before
    public void setUp() {

        testPerson = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);
       if (personDao.getPerson(testPerson.getUsername()) == null) {
            personDao.create(testPerson);
        } else {
            testPerson = personDao.getPerson(testPerson.getUsername());
        }
    }


    @Test
	public void testFormAccess() {
		int count = formLayoutDao.getAllFormsCount();
		List<String> names = formLayoutDao.getAllFormNames();
		assertEquals(count, names.size());

		count = formLayoutDao.getFormsCount(testPerson);
		names = formLayoutDao.getFormNames(testPerson);
		assertEquals(count, names.size());
	}


	@Test
	public void testLayoutCounts() {
		int count = formLayoutDao.getAllLayoutsCount();
		List<FormLayout> list = formLayoutDao.getAllLayouts();
		assertNotNull(list);
		assertEquals(count, list.size());

		count = formLayoutDao.getLayoutsCount(testPerson);
		list = formLayoutDao.getLayouts(testPerson);
		assertNotNull(list);
		assertEquals(count, list.size());

		if (formLayoutDao.getAllFormsCount() > 0) {
			String formName = formLayoutDao.getAllFormNames().get(0);

			count = formLayoutDao.getLayoutsCount(formName);
			list = formLayoutDao.getLayouts(formName);
			assertNotNull(list);
			assertEquals(count, list.size());

			count = formLayoutDao.getLayoutsCount(testPerson, formName);
			list = formLayoutDao.getLayouts(testPerson, formName);
			assertNotNull(list);
			assertEquals(count, list.size());
		}
	}


	@Test
	public void testGetLayout() {
		List<FormLayout> list = formLayoutDao.getAllLayouts();
		assertNotNull(list);
		if (list.isEmpty())
			return;
		String formName = list.get(0).getFormName();
		String layoutName = list.get(0).getLayoutName();
		FormLayout layout = formLayoutDao.getLayout(formName, layoutName);
		assertNotNull(layout);
		assertEquals(formName, layout.getFormName());
		assertEquals(layoutName, layout.getLayoutName());
	}

    @After
    public void clean() {
        if (testPerson.getUsername() != null) {
            personDao.delete(testPerson);
        }
    }

}

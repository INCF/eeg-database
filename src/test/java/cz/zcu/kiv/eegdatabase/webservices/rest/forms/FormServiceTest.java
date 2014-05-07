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
* FormServiceTest.java, 28. 3. 2014 19:09:39, Jakub Krauz
*
**********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FormLayout;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers.RecordCountData;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableFormsDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsData;
import cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers.AvailableLayoutsDataList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
* Contains test cases for RESTful form service.
*
* @author Jakub Krauz
*/

public class FormServiceTest extends AbstractDataAccessTest {

	@Autowired
	private FormService service;

	@Autowired
	private PersonDao personDao;


    @Before
    public void setUp() throws Exception {
        // don't know another way to get logged-in
        Person testPerson = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        if (personDao.getPerson(testPerson.getUsername()) == null) {
            personDao.create(testPerson);
        } else {
            testPerson = personDao.getPerson(testPerson.getUsername());
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(testPerson.getAuthority()));
        Authentication auth = new UsernamePasswordAuthenticationToken(testPerson, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


	@Test
	public void testAvailableForms() {
		RecordCountData count = service.availableFormsCount();
		assertNotNull(count);

		// check all records
		AvailableFormsDataList list = service.availableForms(false);
		assertNotNull(list);
		assertNotNull(list.getForms());
		assertEquals(count.getPublicRecords(), list.getForms().size());

		// check my records
		list = service.availableForms(true);
		assertNotNull(list);
		assertNotNull(list.getForms());
		assertEquals(count.getMyRecords(), list.getForms().size());
	}


	@Test
	public void testAvailableLayouts() {
		RecordCountData count = service.availableLayoutsCount();
		assertNotNull(count);

		// check all records
		AvailableLayoutsDataList list = service.availableLayouts(false);
		assertNotNull(list);
		assertNotNull(list.getLayouts());
		assertEquals(count.getPublicRecords(), list.getLayouts().size());

		// check all records
		list = service.availableLayouts(true);
		assertNotNull(list);
		assertNotNull(list.getLayouts());
		assertEquals(count.getMyRecords(), list.getLayouts().size());
	}


	@Test
	public void testGetLayout() throws FormServiceException {
		AvailableLayoutsDataList list = service.availableLayouts(false);
		assertNotNull(list);
		if (list.getLayouts().isEmpty())
			return;
		AvailableLayoutsData layoutData = list.getLayouts().get(0);
		FormLayout layout = service.getLayout(layoutData.getFormName(), layoutData.getLayoutName());
		assertNotNull(layout);
		assertEquals(layoutData.getFormName(), layout.getFormName());
		assertEquals(layoutData.getLayoutName(), layout.getLayoutName());
		assertNotNull(layout.getContent());
	}


	@Test
	@Transactional
	public void testCRUDLayout() throws FormServiceException {
		final String formName = "formNameTest";
		final String layoutName = "layoutNameTest";
		final byte[] originalContent = new byte[] { 0 };
		final byte[] newContent = new byte[] { 1 };

		int originalCount = service.availableLayoutsCount().getPublicRecords();

		// create
		service.createLayout(formName, layoutName, originalContent);
		assertEquals(originalCount + 1, service.availableLayoutsCount().getPublicRecords());
		assertEquals(originalContent, service.getLayout(formName, layoutName).getContent());

		// update
		service.updateLayout(formName, layoutName, newContent);
		assertEquals(originalCount + 1, service.availableLayoutsCount().getPublicRecords());
		assertEquals(newContent, service.getLayout(formName, layoutName).getContent());

		// delete
		service.deleteLayout(formName, layoutName);
		assertEquals(originalCount, service.availableLayoutsCount().getPublicRecords());
	}


	@Test
	public void testGetOdmlData() throws FormServiceException {
		final byte[] data = service.getOdmlData(Person.class.getSimpleName());
		assertNotNull(data);
		assertTrue(data.length > 0);

		try {
			service.getOdmlData(null);
			fail("Got odml data for null entity, NullPointerException should be thrown.");
		} catch (Exception e) {
			assertTrue(e instanceof NullPointerException);
		}

		try {
			service.getOdmlData("xxxx");
			fail("Got odml data for non-existing entity, FormServiceException should be thrown.");
		} catch (Exception e) {
			assertTrue(e instanceof FormServiceException);
		}
	}


}

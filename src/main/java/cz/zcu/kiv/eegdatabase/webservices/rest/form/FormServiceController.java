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
 * FormServiceController.java, 3. 1. 2014 10:08:47, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.form;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import cz.zcu.kiv.formgen.Form;
import cz.zcu.kiv.formgen.FormGenerator;
import cz.zcu.kiv.formgen.FormNotFoundException;
import cz.zcu.kiv.formgen.Writer;
import cz.zcu.kiv.formgen.core.SimpleFormGenerator;
import cz.zcu.kiv.formgen.odml.OdmlFormProvider;
import cz.zcu.kiv.formgen.odml.OdmlWriter;


/**
 * Controller class mapping the REST form-layout service.
 *
 * @author Jakub Krauz
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/form-layout")
public class FormServiceController {
	
	// TODO logika sluzby do tridy FormService
	@RequestMapping(value = "/{name}")
	public void getForm(@PathVariable String name, HttpServletResponse response) throws ClassNotFoundException, FormNotFoundException, IOException {
		FormGenerator generator = new SimpleFormGenerator(new OdmlFormProvider());
		//generator.loadPackage("cz.zcu.kiv.eegdatabase.data.pojo");
		Class<?> cls = Class.forName("cz.zcu.kiv.eegdatabase.data.pojo.Experiment");
		generator.loadClass(cls);
		
		Form form = generator.getForm(name);
		if (form == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Writer writer = new OdmlWriter();
		response.setContentType("application/xml");
		writer.write(form, response.getOutputStream());
		response.flushBuffer();
	}
	
	
	/*@RequestMapping(value = "/pokus", produces = "application/json")
	public @ResponseBody String pokus() {
		return "99";
	}
	
	
	@RequestMapping(value = "/pokus6")
	public void pokus6(HttpServletResponse response) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for (int i : "<xml><section><name>formular</name><section><name>polozka</name></section></section></xml>".toCharArray())
			os.write(i);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		
		response.setContentType("application/xml");
        response.setContentLength(os.size());
        //response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
	}*/
	
}

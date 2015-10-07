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
 *   PersonFormPanel.java, 2015/10/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.DateTimeFieldPicker;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.PatternValidator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stebjan on 7.10.2015.
 */
public class PersonFormPanel extends FormComponentPanel<FullPersonDTO> {

    private EducationLevelFacade educationLevelFacade;

    public PersonFormPanel(String id, IModel<FullPersonDTO> model, EducationLevelFacade educationLevelFacade) throws IOException {
        super(id, model);
        this.educationLevelFacade = educationLevelFacade;
        init();
    }


    private void init() throws IOException {
        TextField<String> name = new TextField<String>("givenname");
        name.setLabel(ResourceUtils.getModel("general.name"));
        name.setRequired(true);
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(name);

        TextField<String> surname = new TextField<String>("surname");
        surname.setLabel(ResourceUtils.getModel("general.surname"));
        surname.setRequired(true);
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(surname);

        DateTimeFieldPicker date = new DateTimeFieldPicker("dateOfBirth") {

            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new TimestampConverter();
            }
        };
        date.setLabel(ResourceUtils.getModel("general.dateofbirth"));
        //date.setRequired(true);
        add(date);

        TextField<String> address = new TextField<String>("address");
        address.setLabel(ResourceUtils.getModel("label.address"));
        add(address);

        TextField<String> city = new TextField<String>("city");
        city.setLabel(ResourceUtils.getModel("label.city"));
        add(city);

        TextField<String> state = new TextField<String>("state");
        state.setLabel(ResourceUtils.getModel("label.state"));
        add(state);

        TextField<String> zipCode = new TextField<String>("zipCode");
        zipCode.setLabel(ResourceUtils.getModel("label.zipCode"));
        add(zipCode);

        TextField<String> url = new TextField<String>("url");
        url.setLabel(ResourceUtils.getModel("label.url"));
        add(url);

        TextField<String> phone = new TextField<String>("phone");
        phone.setLabel(ResourceUtils.getModel("label.phoneNumber"));
        add(phone);

        TextField<String> organization = new TextField<String>("organization");
        organization.setLabel(ResourceUtils.getModel("label.organization"));
        add(organization);

        TextField<String> jobTitle = new TextField<String>("jobTitle");
        jobTitle.setLabel(ResourceUtils.getModel("label.jobTitle"));
        add(jobTitle);

        TextField<String> orgAddress = new TextField<String>("orgAddress");
        orgAddress.setLabel(ResourceUtils.getModel("label.address"));
        add(orgAddress);

        TextField<String> orgCity = new TextField<String>("orgCity");
        orgCity.setLabel(ResourceUtils.getModel("label.city"));
        add(orgCity);

        TextField<String> orgState = new TextField<String>("orgState");
        orgState.setLabel(ResourceUtils.getModel("label.state"));
        add(orgState);

        TextField<String> orgZipCode = new TextField<String>("orgZipCode");
        orgZipCode.setLabel(ResourceUtils.getModel("label.zipCode"));
        add(orgZipCode);

        TextField<String> orgUrl = new TextField<String>("orgUrl");
        orgUrl.setLabel(ResourceUtils.getModel("label.url"));
        add(orgUrl);

        TextField<String> orgPhone = new TextField<String>("orgPhone");
        orgPhone.setLabel(ResourceUtils.getModel("label.phoneNumber"));
        add(orgPhone);

        TextField<String> VAT = new TextField<String>("VAT");
        VAT.setLabel(ResourceUtils.getModel("label.VAT"));
        add(VAT);


        List<String> listOfTitles = new ArrayList<String>();
        listOfTitles.add("Mr.");
        listOfTitles.add("Mrs.");
        listOfTitles.add("Ms.");

        DropDownChoice<String> title = new DropDownChoice<String>("title", listOfTitles,
                new ChoiceRenderer<String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(String object) {
                        return object;
                    }

                });

        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.title"));
        add(title);

        File file = ResourceUtils.getFile("countries.txt");
        List<String> countries = FileUtils.getFileLines(file);

        DropDownChoice<String> country = new DropDownChoice<String>("country", countries,
                new ChoiceRenderer<String>("country") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(String object) {
                        return object;
                    }

                });

        country.setRequired(true);
        country.setLabel(ResourceUtils.getModel("label.country"));
        add(country);

        DropDownChoice<String> orgCountry = new DropDownChoice<String>("orgCountry", countries,
                new ChoiceRenderer<String>("orgCountry") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(String object) {
                        return object;
                    }

                });

        //orgCountry.setRequired(true);
        orgCountry.setLabel(ResourceUtils.getModel("label.country"));
        add(orgCountry);

        List<String> listOfOrgTypes = new ArrayList<String>();
        listOfOrgTypes.add("Commercial");
        listOfOrgTypes.add("Non-Commercial");

        DropDownChoice<String> organizationType = new DropDownChoice<String>("organizationType", listOfOrgTypes,
                new ChoiceRenderer<String>("organizationType") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(String object) {
                        return object;
                    }

                });

        organizationType.setRequired(true);
        organizationType.setLabel(ResourceUtils.getModel("label.organizationType"));
        add(organizationType);

        DropDownChoice<EducationLevel> educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationLevelFacade.getAllRecords(),
                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(EducationLevel object) {
                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
                    }

                });

        educationLevel.setLabel(ResourceUtils.getModel("general.educationlevel"));
        add(educationLevel);
    }


    @Override
    protected void convertInput() {

        this.setConvertedInput(this.getModelObject());
        //System.out.println(this.getModelObject().getSurname());

    }
}

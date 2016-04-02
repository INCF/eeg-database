package cz.zcu.kiv.eegdatabase.data.nosql;

import odml.core.Section;
import odml.core.Property;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2016 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * odmlBP, 2016/03/12 16:00 administrator
 * <p/>
 * ********************************************************************************************************************
 */

public class MobioMetadata {

    private Date convertStringToDate(String dateString) {
        Date date = null;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try{
            date = df.parse(dateString);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return date;
    }

    public Section createMetaData(JSONObject data)throws Exception{
        Section metadata = new Section();

        metadata.setDocumentAuthor(data.getJSONObject("odML").getString("author"));
        metadata.setDocumentVersion(data.getJSONObject("odML").get("version").toString());
        metadata.setDocumentDate(convertStringToDate(data.getJSONObject("odML").getString("date")));

        JSONArray sectionArr =  data.getJSONObject("odML").getJSONArray("section");

        for(int i = 0; i < sectionArr.length(); i++) {
            Section newS = new Section();

            newS.setName(sectionArr.getJSONObject(i).getString("name"));
            newS.setType(sectionArr.getJSONObject(i).getString("type"));


            if(sectionArr.getJSONObject(i).has("section")) {
                    JSONArray measurements = sectionArr.getJSONObject(i).getJSONArray("section");

                    for(int j = 0; j < measurements.length(); j++) {
                        Section measurement = new Section();
                        measurement.setName(measurements.getJSONObject(i).getString("name"));
                        measurement.setType(measurements.getJSONObject(i).getString("type"));

                        newS.add(measurement);

                        JSONArray properties = measurements.getJSONObject(j).getJSONArray("property");

                        for(int k = 0; k < properties.length(); k++) {
                            Property property = new Property(properties.getJSONObject(k).getString("name"), new Object());

                            property.setValue(properties.getJSONObject(k).getJSONObject("value").get("content"));
                            measurement.add(property);
                        }
                    }



                } else if(sectionArr.getJSONObject(i).has("property")) {
                    JSONArray properties = sectionArr.getJSONObject(i).getJSONArray("property");

                    for(int j = 0; j < properties.length(); j++) {
                        Property property = new Property(properties.getJSONObject(j).getString("name"), new Object());

                        property.setValue(properties.getJSONObject(j).getJSONObject("value").get("content"));
                        newS.add(property);
                    }

                }


            metadata.add(newS);
        }
        return metadata;
    }

}

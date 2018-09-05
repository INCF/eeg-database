package cz.zcu.kiv.eegdatabase.webservices.rest;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * AdapterCDATA, 2018/09/05 10:50 petr-jezek
 *
 **********************************************************************************************************************/
public class AdapterCDATA extends XmlAdapter<String, String> {

    @Override
    public String marshal(String arg0) throws Exception {
        return "<![CDATA[" + arg0 + "]]>";
    }
    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }

}

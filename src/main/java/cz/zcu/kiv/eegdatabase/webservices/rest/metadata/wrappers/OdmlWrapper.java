package cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers;

import com.sun.xml.txw2.annotation.XmlCDATA;
import cz.zcu.kiv.eegdatabase.webservices.rest.AdapterCDATA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.OutputStream;

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
 * OdmlWrapper, 2018/09/05 09:59 petr-jezek
 *
 **********************************************************************************************************************/
@XmlRootElement(name = "OdmlWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class OdmlWrapper {


    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

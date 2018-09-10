package cz.zcu.kiv.eegdatabase.webservices.rest.common.utils;

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

    private static final String CDATA_START = "";
    private static final String CDATA_STOP  = "]]>";

    /**
     * Check whether a string is a CDATA string
     * @param s the string to check
     * @return
     */
    public static boolean isCdata(String s) {
        s = s.trim();
        boolean test = (s.startsWith(CDATA_START) && s.endsWith(CDATA_STOP));
        return test;
    }

    /**
     * Parse a CDATA String.<br />
     * If is a CDATA, removes leading and trailing string<br />
     * Otherwise does nothing
     * @param s the string to parse
     * @return the parsed string
     */
    public static String parse(String s)  {

        StringBuilder sb = null;
        s = s.trim();

        if(isCdata(s)) {
            sb = new StringBuilder(s);
            sb.replace(0, CDATA_START.length(), "");

            s = sb.toString();
            sb = new StringBuilder(s);
            sb.replace(s.lastIndexOf(CDATA_STOP), s.lastIndexOf(CDATA_STOP)+CDATA_STOP.length(),"");
            s = sb.toString();
        }
        return s;
    }

    /**
     * Add CDATA leading and trailing to a string if not already a CDATA
     * @param s
     * @return
     */
    public static String print(String s) {
        if(isCdata(s)) {
            return s;
        } else {
            return CDATA_START + s + CDATA_STOP;
        }
    }

}

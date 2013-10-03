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
 *   ChannelInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.signal;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 10.4.2011
 * Time: 17:37:21
 * To change this template use File | Settings | File Templates.
 */
public class ChannelInfo {

    protected String name;
    protected int number;
    protected float resolution;
    protected String units;

    public final String getName() {
        return name;
    }
    public final int getNumber() {
        return number;
    }

    public final double getResolution() {
        return resolution;
    }

    public final String getUnits() {
        return units;
    }

    /**
    Create new info from defined string

    @param strData Ch<Channel number>=<Name>,<Reference channel name>,
    <Resolution in "Unit">,<Unit>
     */
    public ChannelInfo(int number, String strData) {

        this.number = number;
        String[] arr = strData.split("[,]", -1);
        name = arr[0];
        resolution = Float.parseFloat(arr[2]);
        units = arr[3];
    }
}

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
 *   UserDataServiceException.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.datadownload;

/**
 * User: Petr Miko - miko.petr (at) gmail.com
 * Date: 3.10.11
 * Time: 10:20
 * <p/>
 * This class is for exception purposes within web services.
 */
public class UserDataServiceException extends Exception {

    /**
     * Constructor, adds custom message.
     *
     * @param throwable original throwable
     */
    public UserDataServiceException(Throwable throwable) {
        super("Error occured during working with web service.", throwable);
    }
}

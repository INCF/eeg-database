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
 *   ServicesList.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.service;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 17.9.11
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public enum ServicesList {
    MATCHING_PURSUIT("Matching Pursuit"),
    DISCRETE_WAVELET("Discrete Wavelet Transformation"),
    CONTINUOUS_WAVELET("Continuous Wavelet Transformation"),
    FAST_FOURIER("Fast Fourier");

    private final String name;

    ServicesList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

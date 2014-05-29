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
 *   StringUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.utils;

/**
 * Utilities class for string.
 * 
 * @author Jakub Rinkes
 *
 */
public class StringUtils {
    
    public static String REGEX_ONLY_LETTERS = "[a-zA-Z][a-zA-Z\\s]*";
    public static String DATE_TIME_FORMAT_PATTER = "dd.MM.yyyy, HH:mm:ss";
    public static String DATE_FORMAT_PATTER = "dd.MM.yyyy";
    public static String DATE_TIME_FORMAT_PATTER_ONLY_YEAR = "yyyy";
    
    private static int randomInt(int min, int max)
    {
        return (int)(Math.random() * (max - min) + min);
    }

    public static String randomString(int min, int max)
    {
        int num = randomInt(min, max);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++)
            b[i] = (byte)randomInt('a', 'z');
        return new String(b);
    }
    
    public static String getCaptchaString(){
        return randomString(3, 8);
    }
}

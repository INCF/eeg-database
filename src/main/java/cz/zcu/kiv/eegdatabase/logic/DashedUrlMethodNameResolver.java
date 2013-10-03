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
 *   DashedUrlMethodNameResolver.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 *
 * @author Jindra
 */
public class DashedUrlMethodNameResolver implements MethodNameResolver {

  public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
    String url = request.getRequestURL().toString();
    int lastSlash = url.lastIndexOf("/") + 1;
    int extensionPosition = url.lastIndexOf(".");

    String fileName = null;
    if (extensionPosition > lastSlash) {
      // Dot of the extension must be behind last slash
      fileName = url.substring(lastSlash, extensionPosition);
    } else {
      // without extension
      fileName = url.substring(lastSlash);
    }

    // Remove slashes in file name and convert the beginnings of the words to upper case
    boolean toUpperCase = false;
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fileName.length(); i++) {
      char c = fileName.charAt(i);
      if (c == '-') {
        toUpperCase = true;
      } else {
        if (toUpperCase) {
          c = Character.toUpperCase(c);
          toUpperCase = false;
        }
        result.append(c);
      }
    }

    return result.toString();
  }
}

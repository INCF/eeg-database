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
 *   OracleDialectWithXmlSupport.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dialect;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 27.5.11
 * Time: 1:13
 * To change this template use File | Settings | File Templates.
 */
public class OracleDialectWithXmlSupport extends Oracle10gDialect {

   public OracleDialectWithXmlSupport() {
      super();
      registerHibernateType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
      registerColumnType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
   }
}

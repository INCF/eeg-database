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
 *   WeatherGroupRelId.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.Embeddable;

/**
 * @author František Liška
 */
@Embeddable
public class WeatherGroupRelId implements java.io.Serializable {

     private int weatherId;
     private int researchGroupId;

    public WeatherGroupRelId() {
    }

    public WeatherGroupRelId(int weatherId, int researchGroupId) {
       this.weatherId = weatherId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getWeatherId() {
        return this.weatherId;
    }
    
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
    public int getResearchGroupId() {
        return this.researchGroupId;
    }
    
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof WeatherGroupRelId) ) return false;
		 WeatherGroupRelId castOther = (WeatherGroupRelId) other;
         
		 return (this.getWeatherId()==castOther.getWeatherId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getWeatherId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}

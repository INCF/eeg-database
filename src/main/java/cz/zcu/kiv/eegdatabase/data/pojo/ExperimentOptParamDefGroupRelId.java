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
 *   ExperimentOptParamDefGroupRelId.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.Embeddable;

/**
 * @author František Liška
 */
@Embeddable
public class ExperimentOptParamDefGroupRelId implements java.io.Serializable {
     private int experimentOptParamDefId;
     private int researchGroupId;

    public ExperimentOptParamDefGroupRelId() {
    }

    public ExperimentOptParamDefGroupRelId(int experimentOptParamDefId, int researchGroupId) {
       this.experimentOptParamDefId = experimentOptParamDefId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getExperimentOptParamDefId() {
        return this.experimentOptParamDefId;
    }
    
    public void setExperimentOptParamDefId(int experimentOptParamDefId) {
        this.experimentOptParamDefId = experimentOptParamDefId;
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
		 if ( !(other instanceof ExperimentOptParamDefGroupRelId) ) return false;
		 ExperimentOptParamDefGroupRelId castOther = (ExperimentOptParamDefGroupRelId) other;
         
		 return (this.getExperimentOptParamDefId()==castOther.getExperimentOptParamDefId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getExperimentOptParamDefId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}

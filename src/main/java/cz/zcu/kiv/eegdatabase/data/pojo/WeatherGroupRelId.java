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

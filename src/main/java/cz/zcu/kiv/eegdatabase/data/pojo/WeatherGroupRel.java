package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author František Liška
 */
@Entity
@javax.persistence.Table(name="WEATHER_GROUP_REL")
public class WeatherGroupRel implements java.io.Serializable {
    @EmbeddedId
  private WeatherGroupRelId id;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
  private ResearchGroup researchGroup;
    @ManyToOne
    @JoinColumn(name = "WEATHER_ID")
  private Weather weather;

  public WeatherGroupRel() {
  }

  public WeatherGroupRel(WeatherGroupRelId id, ResearchGroup researchGroup, Weather weather) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.weather = weather;
  }

  public WeatherGroupRelId getId() {
    return this.id;
  }

  public void setId(WeatherGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public Weather getWeather() {
    return this.weather;
  }

  public void setWeather(Weather weather) {
    this.weather = weather;
  }

}


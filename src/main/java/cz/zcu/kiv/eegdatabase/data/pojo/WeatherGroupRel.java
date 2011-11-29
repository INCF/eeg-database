package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class WeatherGroupRel implements java.io.Serializable {
  private WeatherGroupRelId id;
  private ResearchGroup researchGroup;
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


package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.*;

import javax.jws.WebService;
import java.util.List;

/**
 * @author František Liška
 */
@WebService
public interface ClientService {
    boolean isServiceAvailable();
    public List<PersonInfo> getPersonList();
    public List<EducationLevelInfo> getEducationLevelList();
    public List<ResearchGroupInfo> getResearchGroupList();
    public List<ScenarioSchemasInfo> getScenarioSchemasList();
    public List<ScenarioInfo> getScenarioList();
    public List<PersonOptParamValInfo> getPersonOptParamValList();

    public int createHardware(HardwareInfo info);
    public int createWeather(WeatherInfo info);
    public int createPersonOptParamDef(PersonOptParamDefInfo info);
    public int createExperimentOptParamDef(ExperimentOptParamDefInfo info);
    public int createFileMetadataParamDef(FileMetadataParamDefInfo info);
}

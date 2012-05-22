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

}

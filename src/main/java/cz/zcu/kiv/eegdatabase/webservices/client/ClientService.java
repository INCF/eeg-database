package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.*;
import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;
import java.util.List;

/**
 * @author František Liška
 */
@MTOM
@WebService
public interface ClientService {
    boolean isServiceAvailable();
    public List<PersonInfo> getPersonList();
    public List<EducationLevelInfo> getEducationLevelList();
    public List<ResearchGroupInfo> getResearchGroupList();
    public List<ScenarioInfo> getScenarioList();
    public List<PersonOptParamValInfo> getPersonOptParamValList();

    public int addHardware(HardwareInfo info);
    public int addWeather(WeatherInfo info);
    public int addResearchGroup(ResearchGroupInfo info);
    public void addResearchGroupMembership(ResearchGroupMembershipInfo info);
    public int addPerson(PersonInfo info);
    public int addScenario(ScenarioInfo info);
    public int addDataFile(DataFileInfo info, @XmlMimeType("application/octet-stream") DataHandler inputData) throws ClientServiceException;
    public int addPersonOptParamDef(PersonOptParamDefInfo info);
    public void addPersonOptParamVal(PersonOptParamValInfo info);
    public int addExperimentOptParamDef(ExperimentOptParamDefInfo info);
    public void addExperimentOptParamVal(ExperimentOptParamValInfo info);
    public int addFileMetadataParamDef(FileMetadataParamDefInfo info);
    public void addFileMetadataParamVal(FileMetadataParamValInfo info);
}

package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ExperimentsService extends GenericService<Experiment, Integer> {

    Experiment getExperimentForDetail(int experimentId);

    List<DataFile> getDataFilesWhereExpId(int experimentId);

    List<DataFile> getDataFilesWhereId(int dataFileId);

    List<Experiment> getAllExperimentsForUser(Person person, int start, int count);

    List<Experiment> getExperimentsWhereOwner(Person person, int limit);

    List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit);

    List<Experiment> getExperimentsWhereSubject(Person person, int limit);

    List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit);

    int getCountForExperimentsWhereOwner(Person loggedUser);

    int getCountForExperimentsWhereSubject(Person person);

    int getCountForAllExperimentsForUser(Person person);

    List<Experiment> getRecordsNewerThan(long oracleScn, int personId);

    List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId);

    /**
     * Returns list of experiments belonging to the package.
     * @param packageId id of the package
     * @return list of experiments or empty list
     */
    public List<Experiment> getExperimentsByPackage(int packageId);

    /**
     * Returns list of experiments that are not members of the given package.
     * @param packageId id of the package
     * @return list of experiments or empty list
     */
    List<Experiment> getExperimentsWithoutPackage(int researchGroupId, int packageId);
}

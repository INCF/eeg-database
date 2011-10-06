package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.io.Serializable;
import java.util.List;

/**
 * DAO for fetching and saving experiments.
 * 
 * @author Jindrich Pergler
 */
public interface ExperimentDao<T, PK extends Serializable> extends GenericDao<T, PK> {

  public List<DataFile> getDataFilesWhereExpId(int experimentId);

  public List<DataFile> getDataFilesWhereId(int dataFileId);

  public List<Experiment> getExperimentsWhereSubject(int personId);

  public List<Experiment> getExperimentsWhereOwner(int personId);

  public List getExperimentsWhereOwner(int personId, int i);

  public List getExperimentsWhereSubject(int personId, int i);

  public List<Experiment> getAllExperimentsForUser(int personId);

  public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests);
}

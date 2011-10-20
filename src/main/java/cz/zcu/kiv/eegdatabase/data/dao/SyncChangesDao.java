package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.SyncChanges;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO interface for getting information about changes important for server-client synchronization.
 *
 * @author: Petr Miko
 */
public interface SyncChangesDao extends GenericDao<SyncChanges, Integer> {

}

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.SyncChanges;

/**
 * Implementation class of SyncChanges DAO.
 *
 * @author: Petr Miko
 */
public class SimpleSyncChangesDao extends SimpleGenericDao<SyncChanges, Integer> implements SyncChangesDao {


    public SimpleSyncChangesDao(Class<SyncChanges> type) {
        super(type);
    }

}

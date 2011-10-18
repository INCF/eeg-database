package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

import java.sql.Timestamp;

/**
 * Wrapper class for holding last changes important for server-client synchronization.
 *
 * @author: Petr Miko - miko.petr (at) gmail.com
 */
public class SyncChangesInfo {

    private String tableName;
    private long lastChangeInMillis;

    /**
     * Getter of monitored table name.
     *
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Setter of monitored table name.
     *
     * @param tableName table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Getter of time of last change.
     *
     * @return milliseconds of last change
     */
    public long getLastChangeInMillis() {
        return lastChangeInMillis;
    }

    /**
     * Setter of time of last change.
     *
     * @param lastChangeInMillis milliseconds of last change
     */
    public void setLastChangeInMillis(long lastChangeInMillis) {
        this.lastChangeInMillis = lastChangeInMillis;
    }
}

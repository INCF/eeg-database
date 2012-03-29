package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Petr
 * Date: 18.10.11
 * Time: 9:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SYNC_CHANGES")
public class SyncChanges implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TABLE_NAME")
    private String tableName;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @Column(name = "LAST_CHANGE")
    private Timestamp lastChange;


    public Timestamp getLastChange() {
        return lastChange;
    }

    public void setLastChange(Timestamp lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyncChanges that = (SyncChanges) o;

        if (lastChange != null ? !lastChange.equals(that.lastChange) : that.lastChange != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (lastChange != null ? lastChange.hashCode() : 0);
        return result;
    }
}

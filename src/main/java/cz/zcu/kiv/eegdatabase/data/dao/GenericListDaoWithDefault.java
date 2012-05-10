package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public interface GenericListDaoWithDefault<T> extends GenericListDao<T> {

    public void createDefaultRecord(T persistent);

    public List<T> getDefaultRecords();

    public boolean isDefault(int id);

}

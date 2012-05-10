package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public interface GenericListDao<T> extends GenericDao<T, Integer> {

    public void createGroupRel(T persistent, ResearchGroup researchGroup);

    public List<T> getItemsForList();

  //  public List<T> getRecordsNewerThan(long oracleScn);

    public List<T> getRecordsByGroup(int groupId);

    public boolean canDelete(int id);

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(T persistent, ResearchGroup researchGroup);
}

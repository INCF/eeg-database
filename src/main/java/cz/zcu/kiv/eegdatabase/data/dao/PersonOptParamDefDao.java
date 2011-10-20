package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;

import java.util.List;

public interface PersonOptParamDefDao extends GenericDao<PersonOptParamDef, Integer> {
    public List<PersonOptParamDef> getItemsForList();

    public boolean canDelete(int id);
}

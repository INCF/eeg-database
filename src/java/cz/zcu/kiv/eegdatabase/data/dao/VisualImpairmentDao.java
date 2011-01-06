package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;

import java.util.List;

public interface VisualImpairmentDao extends GenericDao<VisualImpairment, Integer> {
    public List<VisualImpairment> getItemsForList();

    public boolean canSaveDescription(String description, int id);
}

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;

public class SimpleVisualImpairmentDao extends SimpleGenericDao<VisualImpairment, Integer> implements VisualImpairmentDao {
    public SimpleVisualImpairmentDao() {
        super(VisualImpairment.class);
    }
}

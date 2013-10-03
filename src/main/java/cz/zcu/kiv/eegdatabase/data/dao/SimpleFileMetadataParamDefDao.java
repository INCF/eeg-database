/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SimpleFileMetadataParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public class SimpleFileMetadataParamDefDao extends SimpleGenericDao<FileMetadataParamDef, Integer> implements FileMetadataParamDefDao {
    public SimpleFileMetadataParamDefDao() {
        super(FileMetadataParamDef.class);
    }

    public List<FileMetadataParamDef> getItemsForList() {
        String hqlQuery = "from FileMetadataParamDef d order by d.paramName";
        List<FileMetadataParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select def.fileMetadataParamVals from FileMetadataParamDef def where def.fileMetadataParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<FileMetadataParamDef> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public List<FileMetadataParamDef> getRecordsByGroup(int groupId) {
        String hqlQuery = "from FileMetadataParamDef h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef) {
        fileMetadataParamDef.setDefaultNumber(1);
        create(fileMetadataParamDef);
    }

    public List<FileMetadataParamDef> getDefaultRecords() {
        String hqlQuery = "from FileMetadataParamDef h where h.defaultNumber = 1";
        List<FileMetadataParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean hasGroupRel(int id) {
        String hqlQuery = "from FileMetadataParamDefGroupRel r where r.id.fileMetadataParamDefId = :id";
        List<FileMetadataParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() > 0);
    }

    public void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        getHibernateTemplate().delete(fileMetadataParamDefGroupRel);
    }

    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId) {
        String hqlQuery = "from FileMetadataParamDefGroupRel r where r.id.fileMetadataParamDefId = :fileMetadataParamDefId and r.id.researchGroupId = :researchGroupId";
        List<FileMetadataParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("fileMetadataParamDefId", fileMetadataParamDefId)
                .setParameter("researchGroupId", researchGroupId)
                .list();
        return list.get(0);
    }

    public void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        fileMetadataParamDefGroupRel.getFileMetadataParamDef().setDefaultNumber(0);
        getHibernateTemplate().save(fileMetadataParamDefGroupRel);
    }

    public void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup) {
        fileMetadataParamDef.getResearchGroups().add(researchGroup);
        researchGroup.getFileMetadataParamDefs().add(fileMetadataParamDef);
    }

    public boolean isDefault(int id) {
        String hqlQuery = "select h.defaultNumber from FileMetadataParamDef h where h.fileMetadataParamDefId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        if (list.get(0) == 1) {
            return true;
        } else {
            return false;
        }

    }


}

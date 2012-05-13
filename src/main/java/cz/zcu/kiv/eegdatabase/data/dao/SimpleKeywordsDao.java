/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.io.Serializable;
import java.util.List;
import org.springframework.dao.support.DataAccessUtils;

/**
 * Simple Dao for class Keywords
 * Class is determined only for Keywords.
 * @author Ladislav Janák
 */
public class SimpleKeywordsDao extends SimpleGenericDao<Keywords, Integer> {
    
    public SimpleKeywordsDao() {
        super(Keywords.class);
    }
    
    /**
     * Selects keyword from DB for specific research group
     * @param groupId ID of the research group
     * @return keywords for researchgroup
     */
    public String getKeywords(int groupId){
        String hqlQuery = "select k.keywordsText " +"from Keywords k " + "left join k.researchGroup rg " + "where rg.researchGroupId = :groupId" ;     
        List<String> list = getHibernateTemplate().findByNamedParam(hqlQuery, "groupId", groupId);
        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return "nofilter";
        }
    }
    
    /**
     * Gets ID of specific record in Keywords table by research group ID
     * @param groupId ID of the research group
     * @return 
     */
    public int getID(int groupId){
        String hqlQuery = "select k.keywordsId " +"from Keywords k " + "left join k.researchGroup rg " + "where rg.researchGroupId = :groupId" ;     
        List<Integer> list = getHibernateTemplate().findByNamedParam(hqlQuery, "groupId", groupId);
        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return -1;
        }
    } 
}


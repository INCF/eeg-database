/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import java.io.Serializable;

/**
 * DAO for fetching and saving download history.
 * @author pbruha
 */
public interface HistoryDao<T, PK extends Serializable> extends GenericDao<T, PK>{

}

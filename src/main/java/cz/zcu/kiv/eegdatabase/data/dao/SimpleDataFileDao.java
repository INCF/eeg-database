package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.io.InputStream;
import java.sql.Blob;

/**
 * @author František Liška
 */
public class SimpleDataFileDao extends SimpleGenericDao<DataFile,Integer> implements DataFileDao {

    public SimpleDataFileDao(){
        super(DataFile.class);
    }

    public Blob createBlob(byte[] input){
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input);
    }

    public Blob createBlob(InputStream input, int length){
        return getHibernateTemplate().getSessionFactory().getCurrentSession().getLobHelper().createBlob(input,length);
    }
}

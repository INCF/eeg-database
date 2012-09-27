package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import java.io.InputStream;
import java.sql.Blob;

/**
 * @author František Liška
 */
public interface DataFileDao extends GenericDao<DataFile, Integer> {
    public Blob createBlob(byte[] input);
    public Blob createBlob(InputStream input, int length);

}

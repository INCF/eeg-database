package cz.zcu.kiv.eegdatabase.wui.core.file;

import java.io.InputStream;
import java.sql.Blob;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface FileFacade extends GenericFacade<DataFile, Integer>{
    
    Blob createBlob(byte[] input);
    Blob createBlob(InputStream input, int length);
    
    DataFileDTO getFile(int fileId);
}

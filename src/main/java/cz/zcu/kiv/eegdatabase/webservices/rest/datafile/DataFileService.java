package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public interface DataFileService {

    public int create(int experimentId, String description, MultipartFile file) throws IOException;

    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException;
}

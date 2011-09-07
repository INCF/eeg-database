/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.zip;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Jan Štěbeták
 */
public interface Generator {

  public OutputStream generate(Experiment exp, boolean scenName) throws Exception, SQLException, IOException;
}

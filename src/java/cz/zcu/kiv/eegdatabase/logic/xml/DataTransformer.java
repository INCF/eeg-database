/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Jan Štěbeták
 */
public interface DataTransformer {

  public OutputStream transform(Experiment m, boolean scenarioName) throws JAXBException, IOException;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.xml;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Jan Štěbeták
 */
public interface DataTransformer {

  public OutputStream transform(Experiment m, MetadataCommand mc, Set<DataFile> datas) throws JAXBException, IOException;
}
